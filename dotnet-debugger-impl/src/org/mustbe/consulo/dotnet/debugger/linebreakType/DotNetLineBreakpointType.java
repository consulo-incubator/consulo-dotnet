/*
 * Copyright 2013-2014 must-be.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mustbe.consulo.dotnet.debugger.linebreakType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.RequiredReadAction;
import org.mustbe.consulo.dotnet.debugger.DotNetDebugThread;
import org.mustbe.consulo.dotnet.debugger.DotNetDebuggerProviders;
import org.mustbe.consulo.dotnet.debugger.DotNetDebuggerUtil;
import org.mustbe.consulo.dotnet.debugger.DotNetVirtualMachineUtil;
import org.mustbe.consulo.dotnet.module.DotNetAssemblyUtil;
import org.mustbe.consulo.dotnet.psi.DotNetCodeBlockOwner;
import org.mustbe.consulo.dotnet.psi.DotNetTypeDeclaration;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.breakpoints.XBreakpointManager;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import lombok.val;
import mono.debugger.AssemblyMirror;
import mono.debugger.Location;
import mono.debugger.LocationImpl;
import mono.debugger.MethodMirror;
import mono.debugger.TypeMirror;
import mono.debugger.VMDisconnectedException;
import mono.debugger.VirtualMachine;
import mono.debugger.protocol.Method_GetDebugInfo;
import mono.debugger.request.BreakpointRequest;
import mono.debugger.request.EventRequestManager;

/**
 * @author VISTALL
 * @since 10.04.14
 */
public class DotNetLineBreakpointType extends DotNetAbstractBreakpointType
{
	enum BreakpointResult
	{
		INVALID,
		WRONG_TYPE,
		OK
	}

	private static final Pair<BreakpointResult, Location> INVALID = Pair.create(BreakpointResult.INVALID, null);
	private static final Pair<BreakpointResult, Location> WRONG_TYPE = Pair.create(BreakpointResult.WRONG_TYPE, null);

	@NotNull
	public static DotNetLineBreakpointType getInstance()
	{
		return EXTENSION_POINT_NAME.findExtension(DotNetLineBreakpointType.class);
	}

	public DotNetLineBreakpointType()
	{
		super("dotnet-linebreapoint", ".NET Line Breakpoint", null);
	}

	@Override
	public boolean canPutAt(@NotNull VirtualFile file, int line, @NotNull Project project)
	{
		return DotNetDebuggerProviders.findByVirtualFile(project, file) != null;
	}

	@Override
	@RequiredReadAction
	public boolean createRequest(@NotNull Project project,
			@NotNull VirtualMachine virtualMachine,
			@NotNull XLineBreakpoint breakpoint,
			@Nullable TypeMirror typeMirror)
	{
		XBreakpointManager breakpointManager = XDebuggerManager.getInstance(project).getBreakpointManager();

		Pair<BreakpointResult, Location> pair = findLocationImpl(project, virtualMachine, breakpoint, typeMirror);
		switch(pair.getFirst())
		{
			case WRONG_TYPE:
				return false;
			default:
			case INVALID:
				breakpointManager.updateBreakpointPresentation(breakpoint, AllIcons.Debugger.Db_invalid_breakpoint, null);
				return false;
			case OK:
				EventRequestManager eventRequestManager = virtualMachine.eventRequestManager();
				BreakpointRequest breakpointRequest = eventRequestManager.createBreakpointRequest(pair.getSecond());
				breakpointRequest.enable();

				breakpointManager.updateBreakpointPresentation(breakpoint, AllIcons.Debugger.Db_verified_breakpoint, null);
				breakpoint.putUserData(DotNetDebugThread.EVENT_REQUEST, breakpointRequest);
				return true;
		}
	}

	@NotNull
	@RequiredReadAction
	public Pair<BreakpointResult, Location> findLocationImpl(@NotNull Project project,
			@NotNull VirtualMachine virtualMachine,
			@NotNull XLineBreakpoint<?> lineBreakpoint,
			@Nullable TypeMirror typeMirror)
	{
		VirtualFile fileByUrl = VirtualFileManager.getInstance().findFileByUrl(lineBreakpoint.getFileUrl());
		if(fileByUrl == null)
		{
			return INVALID;
		}

		PsiElement psiElement = DotNetDebuggerUtil.findPsiElement(project, fileByUrl, lineBreakpoint.getLine());
		if(psiElement == null)
		{
			return INVALID;
		}
		DotNetCodeBlockOwner codeBlockOwner = PsiTreeUtil.getParentOfType(psiElement, DotNetCodeBlockOwner.class, false);
		if(codeBlockOwner == null)
		{
			return INVALID;
		}
		PsiElement codeBlock = codeBlockOwner.getCodeBlock();
		if(codeBlock == null)
		{
			return INVALID;
		}
		if(!PsiTreeUtil.isAncestor(codeBlock, psiElement, false))
		{
			return INVALID;
		}
		DotNetTypeDeclaration typeDeclaration = PsiTreeUtil.getParentOfType(codeBlockOwner, DotNetTypeDeclaration.class);
		if(typeDeclaration == null)
		{
			return INVALID;
		}

		val vmQualifiedName = DotNetVirtualMachineUtil.toVMQualifiedName(typeDeclaration);
		if(typeMirror != null && !Comparing.equal(vmQualifiedName, typeMirror.qualifiedName()))
		{
			return WRONG_TYPE;
		}

		TypeMirror mirror = typeMirror == null ? findTypeMirror(vmQualifiedName, virtualMachine, fileByUrl, typeDeclaration) : typeMirror;

		if(mirror == null)
		{
			return INVALID;
		}

		MethodMirror targetMirror = null;
		int index = -1;
		highLoop:
		for(MethodMirror methodMirror : mirror.methods())
		{
			for(Method_GetDebugInfo.Entry entry : methodMirror.debugInfo())
			{
				if(entry.line == (lineBreakpoint.getLine() + 1))
				{
					targetMirror = methodMirror;
					index = entry.offset;
					break highLoop;
				}
			}
		}

		if(targetMirror == null)
		{
			return INVALID;
		}

		return Pair.<BreakpointResult, Location>create(BreakpointResult.OK, new LocationImpl(virtualMachine, targetMirror, index));
	}

	@RequiredReadAction
	private TypeMirror findTypeMirror(final String vmQualifiedName,
			VirtualMachine virtualMachine,
			VirtualFile virtualFile,
			DotNetTypeDeclaration parent)
	{
		try
		{
			if(virtualMachine.isAtLeastVersion(2, 9))
			{
				TypeMirror[] typesByQualifiedName = virtualMachine.findTypesByQualifiedName(vmQualifiedName, false);
				return typesByQualifiedName.length == 0 ? null : typesByQualifiedName[0];
			}
			else if(virtualMachine.isAtLeastVersion(2, 7))
			{
				TypeMirror[] typesBySourcePath = virtualMachine.findTypesBySourcePath(virtualFile.getPath(), SystemInfo.isFileSystemCaseSensitive);
				return ContainerUtil.find(typesBySourcePath, new Condition<TypeMirror>()
				{
					@Override
					public boolean value(TypeMirror typeMirror)
					{
						return Comparing.equal(typeMirror.qualifiedName(), vmQualifiedName);
					}
				});
			}
			else
			{
				AssemblyMirror[] assemblies = virtualMachine.rootAppDomain().assemblies();

				TypeMirror typeMirror = findTypeMirrorFromAssemblies(vmQualifiedName, assemblies, parent);
				if(typeMirror != null)
				{
					return typeMirror;
				}
				return null;
			}
		}
		catch(VMDisconnectedException e)
		{
			return null;
		}
	}

	@RequiredReadAction
	public static TypeMirror findTypeMirrorFromAssemblies(String vmQualifiedName,
			@NotNull AssemblyMirror[] assemblies,
			@NotNull DotNetTypeDeclaration typeDeclaration)
	{
		String assemblyTitle = DotNetAssemblyUtil.getAssemblyTitle(typeDeclaration);
		if(assemblyTitle == null)
		{
			return null;
		}

		for(AssemblyMirror assembly : assemblies)
		{
			String name = assembly.name();
			int i = name.indexOf(',');
			if(i == -1)
			{
				continue;
			}

			String assemblyShortName = name.substring(0, i);
			if(assemblyTitle.equals(assemblyShortName))
			{
				return assembly.findTypeByQualifiedName(vmQualifiedName, false);
			}
		}
		return null;
	}
}

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

package org.mustbe.consulo.dotnet.debugger.nodes;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.dotnet.dll.vfs.builder.util.XStubUtil;
import org.mustbe.consulo.dotnet.psi.DotNetTypeDeclaration;
import org.mustbe.consulo.dotnet.resolve.DotNetPsiFacade;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.xdebugger.XDebuggerUtil;
import com.intellij.xdebugger.frame.XNamedValue;
import com.intellij.xdebugger.frame.XNavigatable;
import mono.debugger.TypeMirror;

/**
 * @author VISTALL
 * @since 18.04.14
 */
public abstract class AbstractTypedMirrorNode extends XNamedValue
{
	@NotNull
	protected final Project myProject;

	public AbstractTypedMirrorNode(@NotNull String name, @NotNull Project project)
	{
		super(name);
		myProject = project;
	}

	@NotNull
	public abstract TypeMirror getTypeOfVariable();

	@Override
	public void computeTypeSourcePosition(@NotNull XNavigatable navigatable)
	{
		DotNetTypeDeclaration type = findTypeByQualifiedName(getTypeOfVariable());
		if(type == null)
		{
			return;
		}
		PsiElement nameIdentifier = type.getNameIdentifier();
		if(nameIdentifier == null)
		{
			return;
		}
		navigatable.setSourcePosition(XDebuggerUtil.getInstance().createPositionByOffset(type.getContainingFile().getVirtualFile(),
				nameIdentifier.getTextOffset()));
	}

	@Nullable
	public DotNetTypeDeclaration findTypeByQualifiedName(@NotNull TypeMirror typeMirror)
	{
		String qualifiedName = typeMirror.originalQualifiedName();
		int index = qualifiedName.indexOf(XStubUtil.GENERIC_MARKER_IN_NAME);

		int genericCount = 0;
		if(index != -1)
		{
			genericCount = Integer.parseInt(qualifiedName.substring(index + 1, qualifiedName.length()));
			qualifiedName = qualifiedName.substring(0, index);
		}

		return DotNetPsiFacade.getInstance(myProject).findType(qualifiedName, GlobalSearchScope.allScope(myProject), genericCount);
	}

	@Override
	public boolean canNavigateToTypeSource()
	{
		return true;
	}
}

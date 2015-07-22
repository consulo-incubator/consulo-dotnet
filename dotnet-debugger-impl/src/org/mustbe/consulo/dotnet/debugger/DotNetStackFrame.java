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

package org.mustbe.consulo.dotnet.debugger;

import java.io.File;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.dotnet.debugger.nodes.DotNetLocalVariableMirrorNode;
import org.mustbe.consulo.dotnet.debugger.nodes.DotNetMethodParameterMirrorNode;
import org.mustbe.consulo.dotnet.debugger.nodes.DotNetObjectValueMirrorNode;
import org.mustbe.consulo.dotnet.debugger.nodes.objectReview.ObjectReviewer;
import org.mustbe.consulo.dotnet.debugger.nodes.objectReview.YieldObjectReviewer;
import org.mustbe.dotnet.msil.decompiler.textBuilder.util.XStubUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ColoredTextContainer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.xdebugger.XDebuggerUtil;
import com.intellij.xdebugger.XExpression;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator;
import com.intellij.xdebugger.frame.XCompositeNode;
import com.intellij.xdebugger.frame.XStackFrame;
import com.intellij.xdebugger.frame.XValueChildrenList;
import com.intellij.xdebugger.impl.ui.XDebuggerUIConstants;
import mono.debugger.*;

/**
 * @author VISTALL
 * @since 11.04.14
 */
public class DotNetStackFrame extends XStackFrame
{
	private static final ObjectReviewer[] ourObjectReviewers = new ObjectReviewer[]{
			new YieldObjectReviewer()
	};

	private final DotNetDebugContext myDebuggerContext;
	private final StackFrameMirror myFrame;

	public DotNetStackFrame(DotNetDebugContext debuggerContext, StackFrameMirror frame)
	{
		myDebuggerContext = debuggerContext;
		myFrame = frame;
	}

	@Nullable
	@Override
	public XSourcePosition getSourcePosition()
	{
		String fileName = myFrame.location().sourcePath();
		if(fileName == null)
		{
			return null;
		}
		VirtualFile fileByPath = LocalFileSystem.getInstance().findFileByPath(fileName);
		if(fileByPath == null)
		{
			return null;
		}
		return XDebuggerUtil.getInstance().createPosition(fileByPath, myFrame.location().lineNumber() - 1);
	}

	@Nullable
	@Override
	public Object getEqualityObject()
	{
		return myFrame.location().method().id();
	}

	@Nullable
	@Override
	public XDebuggerEvaluator getEvaluator()
	{
		return new XDebuggerEvaluator()
		{
			@Override
			public void evaluate(@NotNull XExpression expression, @NotNull XEvaluationCallback callback,
					@Nullable XSourcePosition expressionPosition)
			{
				for(DotNetDebuggerProvider dotNetDebuggerProvider : DotNetDebuggerProvider.EP_NAME.getExtensions())
				{
					if(dotNetDebuggerProvider.getEditorLanguage() == expression.getLanguage())
					{
						dotNetDebuggerProvider.evaluate(myFrame, myDebuggerContext, expression.getExpression(), null, callback);
						break;
					}
				}
			}

			@Override
			public void evaluate(@NotNull String expression, @NotNull XEvaluationCallback callback, @Nullable XSourcePosition expressionPosition)
			{

			}
		};
	}

	@Override
	public void customizePresentation(ColoredTextContainer component)
	{
		Location location = myFrame.location();
		MethodMirror method = location.method();

		String name = method.name();
		Icon icon = AllIcons.Nodes.Method;
		if(name.equals(XStubUtil.CONSTRUCTOR_NAME))
		{
			name = method.declaringType().name() + "()";
		}
		else if(name.equals(XStubUtil.STATIC_CONSTRUCTOR_NAME))
		{
			name = method.declaringType().name();
			icon = AllIcons.Nodes.Static;
		}
		else
		{
			name = method.name() + "()";
		}

		component.setIcon(icon);
		component.append(name, SimpleTextAttributes.REGULAR_ATTRIBUTES);

		StringBuilder builder = new StringBuilder();
		String fileName = location.sourcePath();
		if(fileName != null)
		{
			builder.append(":");
			builder.append(new File(fileName).getName());
		}

		builder.append(":");
		builder.append(location.lineNumber());
		builder.append(":");
		builder.append(location.columnNumber());
		builder.append(", ");
		builder.append(DotNetVirtualMachineUtil.formatNameWithGeneric(location.method().declaringType()));

		component.append(builder.toString(), SimpleTextAttributes.GRAY_ATTRIBUTES);
	}

	@Override
	public void computeChildren(@NotNull XCompositeNode node)
	{
		MethodMirror method = myFrame.location().method();

		XValueChildrenList childrenList = new XValueChildrenList();

		try
		{
			final Value value = myFrame.thisObject();

			for(ObjectReviewer objectReviewer : ourObjectReviewers)
			{
				if(objectReviewer.reviewObject(myDebuggerContext, value, getFrame(), childrenList))
				{
					node.addChildren(childrenList, true);
					return;
				}
			}

			if(value instanceof ObjectValueMirror)
			{
				TypeMirror type = value.type();
				assert type != null;

				childrenList.add(new DotNetObjectValueMirrorNode(myDebuggerContext, myFrame.thread(), type, (ObjectValueMirror) value));
			}
			else
			{
				childrenList.add(new DotNetObjectValueMirrorNode(myDebuggerContext, myFrame.thread(), myFrame.location().declaringType(),
						(ObjectValueMirror) null));
			}
		}
		catch(AbsentInformationException e)
		{
			node.setMessage("Stack frame info is not available", XDebuggerUIConstants.INFORMATION_MESSAGE_ICON,
					XDebuggerUIConstants.VALUE_NAME_ATTRIBUTES, null);
			return;
		}
		catch(InvalidStackFrameException e)
		{
			node.setErrorMessage("Stack frame info is not valid");
			return;
		}

		MethodParameterMirror[] parameters = method.parameters();

		for(MethodParameterMirror parameter : parameters)
		{
			DotNetMethodParameterMirrorNode parameterMirrorNode = new DotNetMethodParameterMirrorNode(myDebuggerContext, parameter, myFrame);

			childrenList.add(parameterMirrorNode);
		}

		try
		{
			LocalVariableMirror[] locals = method.locals(myFrame.location().codeIndex());
			for(LocalVariableMirror local : locals)
			{
				if(StringUtil.isEmpty(local.name()))
				{
					continue;
				}
				DotNetLocalVariableMirrorNode localVariableMirrorNode = new DotNetLocalVariableMirrorNode(myDebuggerContext, local, myFrame);

				childrenList.add(localVariableMirrorNode);
			}
		}
		catch(IllegalArgumentException ignored)
		{
		}
		node.addChildren(childrenList, true);
	}

	@NotNull
	public StackFrameMirror getFrame()
	{
		return myFrame;
	}
}

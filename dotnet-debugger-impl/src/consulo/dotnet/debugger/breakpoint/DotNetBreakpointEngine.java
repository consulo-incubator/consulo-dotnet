/*
 * Copyright 2013-2016 must-be.org
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

package consulo.dotnet.debugger.breakpoint;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XExpression;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator;
import com.intellij.xdebugger.frame.XValue;
import com.intellij.xdebugger.impl.XSourcePositionImpl;
import consulo.dotnet.debugger.DotNetDebugContext;
import consulo.dotnet.debugger.DotNetDebuggerProvider;
import consulo.dotnet.debugger.DotNetDebuggerSearchUtil;
import consulo.dotnet.debugger.nodes.DotNetAbstractVariableMirrorNode;
import consulo.dotnet.debugger.proxy.DotNetStackFrameProxy;
import consulo.dotnet.debugger.proxy.DotNetThreadProxy;
import consulo.dotnet.debugger.proxy.value.DotNetBooleanValueProxy;
import consulo.dotnet.debugger.proxy.value.DotNetValueProxy;

/**
 * @author VISTALL
 * @since 30.04.2016
 */
public class DotNetBreakpointEngine
{
	@Nullable
	private XValue evaluateBreakpointExpression(@NotNull final DotNetStackFrameProxy frameProxy,
			@NotNull final XLineBreakpoint<?> breakpoint,
			@Nullable final XExpression conditionExpression,
			@NotNull final DotNetDebugContext debugContext)
	{
		if(conditionExpression == null)
		{
			return null;
		}

		final VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl(breakpoint.getFileUrl());
		if(virtualFile == null)
		{
			return null;
		}

		final DotNetDebuggerProvider provider = DotNetDebuggerProvider.getProvider(conditionExpression.getLanguage());
		if(provider != null)
		{
			return ApplicationManager.getApplication().runReadAction(new Computable<XValue>()
			{
				@Override
				public XValue compute()
				{
					Document document = virtualFile.isValid() ? FileDocumentManager.getInstance().getDocument(virtualFile) : null;
					if(document == null)
					{
						return null;
					}
					int line = breakpoint.getLine();

					int offset = line < document.getLineCount() ? document.getLineStartOffset(line) : -1;
					PsiFile file = PsiManager.getInstance(debugContext.getProject()).findFile(virtualFile);
					if(file == null)
					{
						return null;
					}
					PsiElement elementAt = offset >= 0 ? file.findElementAt(offset) : null;
					if(elementAt == null)
					{
						return null;
					}
					final Ref<XValue> valueRef = Ref.create();
					provider.evaluate(frameProxy, debugContext, conditionExpression.getExpression(), elementAt, new XDebuggerEvaluator.XEvaluationCallback()
					{
						@Override
						public void evaluated(@NotNull XValue result)
						{
							valueRef.set(result);
						}

						@Override
						public void errorOccurred(@NotNull String errorMessage)
						{
						}
					}, XSourcePositionImpl.createByElement(elementAt));
					return valueRef.get();
				}
			});
		}
		return null;
	}

	public void tryEvaluateBreakpointLogMessage(@NotNull DotNetThreadProxy threadProxy, final XLineBreakpoint<?> breakpoint, final DotNetDebugContext debugContext)
	{
		XExpression logExpressionObject = breakpoint.getLogExpressionObject();
		if(logExpressionObject == null)
		{
			return;
		}

		XDebugSession session = debugContext.getSession();
		ConsoleView consoleView = session.getConsoleView();
		if(consoleView == null)
		{
			return;
		}

		final DotNetStackFrameProxy frame = threadProxy.getFrame(0);
		if(frame == null)
		{
			return;
		}

		XValue value = evaluateBreakpointExpression(frame, breakpoint, logExpressionObject, debugContext);
		if(value instanceof DotNetAbstractVariableMirrorNode)
		{
			DotNetValueProxy valueOfVariableSafe = ((DotNetAbstractVariableMirrorNode) value).getValueOfVariableSafe();
			if(valueOfVariableSafe != null)
			{
				String toStringValue = DotNetDebuggerSearchUtil.toStringValue(frame, valueOfVariableSafe);
				if(toStringValue != null)
				{
					consoleView.print(toStringValue, ConsoleViewContentType.NORMAL_OUTPUT);
				}
			}
		}
	}

	public boolean tryEvaluateBreakpointCondition(@NotNull DotNetThreadProxy threadProxy, final XLineBreakpoint<?> breakpoint, final DotNetDebugContext debugContext) throws Exception
	{
		final XExpression conditionExpression = breakpoint.getConditionExpression();
		if(conditionExpression == null)
		{
			return true;
		}

		DotNetStackFrameProxy frame = threadProxy.getFrame(0);
		if(frame == null)
		{
			return true;
		}

		XValue value = evaluateBreakpointExpression(frame, breakpoint, conditionExpression, debugContext);
		if(value instanceof DotNetAbstractVariableMirrorNode)
		{
			DotNetValueProxy valueOfVariableSafe = ((DotNetAbstractVariableMirrorNode) value).getValueOfVariableSafe();
			if(valueOfVariableSafe instanceof DotNetBooleanValueProxy)
			{
				return ((DotNetBooleanValueProxy) valueOfVariableSafe).getValue();
			}
		}
		return true;
	}
}
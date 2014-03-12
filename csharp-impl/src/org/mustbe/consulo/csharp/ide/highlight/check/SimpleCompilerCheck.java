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

package org.mustbe.consulo.csharp.ide.highlight.check;

import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.csharp.ide.CSharpErrorBundle;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.Processor;

/**
 * @author VISTALL
 * @since 09.03.14
 */
public class SimpleCompilerCheck<T extends PsiElement> extends AbstractCompilerCheck<T>
{
	@NotNull
	public static <T extends PsiElement> CompilerCheck<T> of(@NotNull HighlightInfoType type, @NotNull Processor<T> processor)
	{
		return new SimpleCompilerCheck<T>(type, processor);
	}

	public SimpleCompilerCheck(HighlightInfoType type, Processor<T> processor)
	{
		super(type, processor);
	}

	@Override
	protected TextRange makeRange(@NotNull T element)
	{
		return element.getTextRange();
	}

	@Override
	protected String makeMessage(@NotNull PsiElement element)
	{
		return CSharpErrorBundle.message(myId);
	}
}
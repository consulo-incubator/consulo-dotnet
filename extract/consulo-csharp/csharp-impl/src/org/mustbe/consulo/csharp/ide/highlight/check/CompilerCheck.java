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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.csharp.ide.CSharpErrorBundle;
import org.mustbe.consulo.csharp.module.extension.CSharpLanguageVersion;
import com.intellij.codeInsight.daemon.impl.HighlightInfoType;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 09.03.14
 */
public abstract class CompilerCheck<T extends PsiElement>
{
	public static class CompilerCheckResult
	{
		private String myText;
		private TextRange myTextRange;
		private HighlightInfoType myHighlightInfoType;

		private List<IntentionAction> myQuickFixes = Collections.emptyList();

		public TextRange getTextRange()
		{
			return myTextRange;
		}

		public void setTextRange(TextRange textRange)
		{
			myTextRange = textRange;
		}

		public String getText()
		{
			return myText;
		}

		public void setText(String text)
		{
			myText = text;
		}

		public HighlightInfoType getHighlightInfoType()
		{
			return myHighlightInfoType;
		}

		public void setHighlightInfoType(HighlightInfoType highlightInfoType)
		{
			myHighlightInfoType = highlightInfoType;
		}

		public void addQuickFix(IntentionAction a)
		{
			if(myQuickFixes.isEmpty())
			{
				myQuickFixes = new ArrayList<IntentionAction>(3);
			}
			myQuickFixes.add(a);
		}

		public List<IntentionAction> getQuickFixes()
		{
			return myQuickFixes;
		}
	}

	@NotNull
	public List<CompilerCheckResult> check(@NotNull CSharpLanguageVersion languageVersion, @NotNull T element)
	{
		CompilerCheckResult check = checkImpl(languageVersion, element);
		if(check == null)
		{
			return Collections.emptyList();
		}
		return Collections.singletonList(check);
	}

	@Nullable
	public CompilerCheckResult checkImpl(@NotNull CSharpLanguageVersion languageVersion, @NotNull T element)
	{
		return null;
	}

	@NotNull
	public CompilerCheckResult result(@NotNull PsiElement range, String... args)
	{
		return resultImpl(getClass(), range, args);
	}

	@NotNull
	public CompilerCheckResult result(@NotNull TextRange range, String... args)
	{
		return resultImpl(getClass(), range, args);
	}

	@NotNull
	public static CompilerCheckResult resultImpl(@NotNull Class<?> clazz, @NotNull PsiElement range, String... args)
	{
		return resultImpl(clazz, range.getTextRange(), args);
	}

	@NotNull
	public static CompilerCheckResult resultImpl(@NotNull Class<?> clazz, @NotNull TextRange range, String... args)
	{
		CompilerCheckResult result = new CompilerCheckResult();
		result.setText(message(clazz, args));
		result.setTextRange(range);
		return result;
	}

	@NotNull
	public static String message(@NotNull Class<?> aClass, String... args)
	{
		String id = aClass.getSimpleName();
		String message = CSharpErrorBundle.message(id, args);
		if(ApplicationManager.getApplication().isInternal())
		{
			message = id + ": " + message;
		}
		return message;
	}
}
/*
 * Copyright 2013-2015 must-be.org
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

package org.mustbe.consulo.csharp.cfs.lang;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.csharp.cfs.lang.lexer.CfsLexer;
import org.mustbe.consulo.csharp.cfs.lang.parser.CfsParser;
import com.intellij.lang.Language;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @since 12.03.2015
 */
public abstract class BaseExpressionCfsLanguageVersion extends BaseCfsLanguageVersion
{
	private IElementType myExpressionElementType;

	public BaseExpressionCfsLanguageVersion(@NotNull Language baseLanguage)
	{
		super(baseLanguage.getName() + "_EXPRESSION", CfsLanguage.INSTANCE);
	}

	public abstract IElementType createExpressionElementType();

	@NotNull
	@Override
	public PsiParser createParser(@Nullable Project project)
	{
		if(myExpressionElementType == null)
		{
			myExpressionElementType = createExpressionElementType();
		}
		return new CfsParser(myExpressionElementType);
	}

	@NotNull
	@Override
	public Lexer createInnerLexer()
	{
		if(myExpressionElementType == null)
		{
			myExpressionElementType = createExpressionElementType();
		}
		return new CfsLexer(myExpressionElementType);
	}
}

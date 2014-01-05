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

package org.mustbe.consulo.csharp.lang.parser;

import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.csharp.lang.parser.decl.DeclarationParsing;
import org.mustbe.consulo.csharp.lang.parser.macro.MacroParsing;
import org.mustbe.consulo.csharp.lang.parser.macro.MacroesInfo;
import com.intellij.lang.ASTNode;
import com.intellij.lang.LanguageVersion;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.resolve.FileContextUtil;
import com.intellij.psi.tree.IElementType;
import lombok.val;

/**
 * @author VISTALL
 * @since 28.11.13.
 */
public class CSharpParser extends SharingParsingHelpers implements PsiParser
{
	@NotNull
	@Override
	public ASTNode parse(@NotNull IElementType elementType, @NotNull PsiBuilder builder, @NotNull LanguageVersion languageVersion)
	{

		builder.setDebugMode(true);
		val builderWrapper = new CSharpBuilderWrapper(builder);

		val marker = builderWrapper.mark();

		MacroesInfo macroesInfo = new MacroesInfo();

		PsiFile psiFile = builder.getUserDataUnprotected(FileContextUtil.CONTAINING_FILE_KEY);
		if(psiFile != null)
		{
			psiFile = psiFile.getOriginalFile();
			psiFile.putUserData(MacroesInfo.MACROES_INFO_KEY, macroesInfo);
		}

		while(!builder.eof())
		{
			if(!MacroParsing.parse(builderWrapper, macroesInfo) && !DeclarationParsing.parse(builderWrapper, macroesInfo, false))
			{
				builder.advanceLexer();
			}
		}

		marker.done(elementType);
		return builder.getTreeBuilt();
	}
}

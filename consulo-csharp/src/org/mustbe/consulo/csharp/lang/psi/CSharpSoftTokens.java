/*
 * Copyright 2013 must-be.org
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

package org.mustbe.consulo.csharp.lang.psi;

import org.mustbe.consulo.csharp.lang.CSharpLanguage;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author VISTALL
 * @since 28.11.13.
 */
public interface CSharpSoftTokens extends CSharpTokens
{
	IElementType PARTIAL_KEYWORD = new IElementType("PARTIAL_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType WHERE_KEYWORD = new IElementType("WHERE_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType GLOBAL_KEYWORD = new IElementType("GLOBAL_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType ADD_KEYWORD = new IElementType("ADD_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType REMOVE_KEYWORD = new IElementType("REMOVE_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType SET_KEYWORD = new IElementType("SET_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType GET_KEYWORD = new IElementType("GET_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType ASYNC_KEYWORD = new IElementType("ASYNC_KEYWORD", CSharpLanguage.INSTANCE);

	TokenSet ALL = TokenSet.create(PARTIAL_KEYWORD, WHERE_KEYWORD, GLOBAL_KEYWORD, ADD_KEYWORD, REMOVE_KEYWORD, SET_KEYWORD, GET_KEYWORD,
			ASYNC_KEYWORD);
}

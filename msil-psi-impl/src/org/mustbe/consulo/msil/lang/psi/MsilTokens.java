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

package org.mustbe.consulo.msil.lang.psi;

import org.mustbe.consulo.msil.MsilLanguage;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @since 21.05.14
 */
public interface MsilTokens extends TokenType
{
	IElementType _CLASS_KEYWORD = new IElementType("_CLASS_KEYWORD", MsilLanguage.INSTANCE);

	IElementType _METHOD_KEYWORD = new IElementType("_METHOD_KEYWORD", MsilLanguage.INSTANCE);

	IElementType _FIELD_KEYWORD = new IElementType("_FIELD_KEYWORD", MsilLanguage.INSTANCE);

	IElementType _PROPERTY_KEYWORD = new IElementType("_PROPERTY_KEYWORD", MsilLanguage.INSTANCE);

	IElementType _EVENT_KEYWORD = new IElementType("_EVENT_KEYWORD", MsilLanguage.INSTANCE);

	IElementType _ASSEMBLY_KEYWORD = new IElementType("_ASSEMBLY_KEYWORD", MsilLanguage.INSTANCE);

	IElementType _CUSTOM_KEYWORD = new IElementType("_CUSTOM_KEYWORD", MsilLanguage.INSTANCE);

	IElementType CLASS_KEYWORD = new IElementType("CLASS_KEYWORD", MsilLanguage.INSTANCE);

	IElementType VALUETYPE_KEYWORD = new IElementType("CLASS_KEYWORD", MsilLanguage.INSTANCE);

	IElementType INT32_KEYWORD = new IElementType("INT32_KEYWORD", MsilLanguage.INSTANCE);

	IElementType INT64_KEYWORD = new IElementType("INT64_KEYWORD", MsilLanguage.INSTANCE);

	IElementType LBRACE = new IElementType("LBRACE", MsilLanguage.INSTANCE);

	IElementType RBRACE = new IElementType("RBRACE", MsilLanguage.INSTANCE);

	IElementType BLOCK_COMMENT = new IElementType("BLOCK_COMMENT", MsilLanguage.INSTANCE);

	IElementType LINE_COMMENT = new IElementType("LINE_COMMENT", MsilLanguage.INSTANCE);
}

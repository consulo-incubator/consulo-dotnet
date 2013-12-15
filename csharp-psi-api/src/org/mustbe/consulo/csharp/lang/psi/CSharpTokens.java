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
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @since 22.11.13.
 */
public interface CSharpTokens extends TokenType
{
	IElementType STRING_KEYWORD = new IElementType("STRING_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType USING_KEYWORD = new IElementType("USING_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType VOID_KEYWORD = new IElementType("VOID_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType INT_KEYWORD = new IElementType("INT_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType BYTE_KEYWORD = new IElementType("BYTE_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType BOOL_KEYWORD = new IElementType("BOOL_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType USHORT_KEYWORD = new IElementType("USHORT_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType DYNAMIC_KEYWORD = new IElementType("DYNAMIC_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType VAR_KEYWORD = new IElementType("VAR_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType DECIMAL_KEYWORD = new IElementType("DECIMAL_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType DOUBLE_KEYWORD = new IElementType("DOUBLE_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType LONG_KEYWORD = new IElementType("LONG_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType ULONG_KEYWORD = new IElementType("ULONG_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType FLOAT_KEYWORD = new IElementType("FLOAT_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType OBJECT_KEYWORD = new IElementType("OBJECT_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType SBYTE_KEYWORD = new IElementType("SBYTE_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType SHORT_KEYWORD = new IElementType("SHORT_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType CHAR_KEYWORD = new IElementType("CHAR_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType UINT_KEYWORD = new IElementType("UINT_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType STATIC_KEYWORD = new IElementType("STATIC_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType PUBLIC_KEYWORD = new IElementType("PUBLIC_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType NAMESPACE_KEYWORD = new IElementType("NAMESPACE_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType EVENT_KEYWORD = new IElementType("EVENT_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType DELEGATE_KEYWORD = new IElementType("DELEGATE_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType CONST_KEYWORD = new IElementType("CONST_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType CLASS_KEYWORD = new IElementType("CLASS_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType INTERFACE_KEYWORD = new IElementType("INTERFACE_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType STRUCT_KEYWORD = new IElementType("STRUCT_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType ENUM_KEYWORD = new IElementType("ENUM_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType NEW_KEYWORD = new IElementType("NEW_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType TYPEOF_KEYWORD = new IElementType("TYPEOF_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType IN_KEYWORD = new IElementType("IN_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType OUT_KEYWORD = new IElementType("OUT_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType INTERNAL_KEYWORD = new IElementType("INTERNAL_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType ABSTRACT_KEYWORD = new IElementType("ABSTRACT_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType PRIVATE_KEYWORD = new IElementType("PRIVATE_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType SEALED_KEYWORD = new IElementType("SEALED_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType UNSAFE_KEYWORD = new IElementType("UNSAFE_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType OVERRIDE_KEYWORD = new IElementType("OVERRIDE_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType REF_KEYWORD = new IElementType("REF_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType EXTERN_KEYWORD = new IElementType("EXTERN_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType VIRTUAL_KEYWORD = new IElementType("VIRTUAL_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType PROTECTED_KEYWORD = new IElementType("PROTECTED_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType VOLATILE_KEYWORD = new IElementType("VOLATILE_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType PARAMS_KEYWORD = new IElementType("PARAMS_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType READONLY_KEYWORD = new IElementType("READONLY_KEYWORD", CSharpLanguage.INSTANCE);

	IElementType LBRACE = new IElementType("LBRACE", CSharpLanguage.INSTANCE);

	IElementType RBRACE = new IElementType("RBRACE", CSharpLanguage.INSTANCE);

	IElementType LPAR = new IElementType("LPAR", CSharpLanguage.INSTANCE);

	IElementType RPAR = new IElementType("RPAR", CSharpLanguage.INSTANCE);

	IElementType LT = new IElementType("LT", CSharpLanguage.INSTANCE);

	IElementType GT = new IElementType("GT", CSharpLanguage.INSTANCE);

	IElementType EQ = new IElementType("EQ", CSharpLanguage.INSTANCE);

	IElementType COLON = new IElementType("COLON", CSharpLanguage.INSTANCE);

	IElementType COLONCOLON = new IElementType("COLONCOLON", CSharpLanguage.INSTANCE);

	IElementType LBRACKET = new IElementType("LBRACKET", CSharpLanguage.INSTANCE);

	IElementType RBRACKET = new IElementType("RBRACKET", CSharpLanguage.INSTANCE);

	IElementType COMMA = new IElementType("COMMA", CSharpLanguage.INSTANCE);

	IElementType SEMICOLON = new IElementType("SEMICOLON", CSharpLanguage.INSTANCE);

	IElementType DOT = new IElementType("DOT", CSharpLanguage.INSTANCE);

	IElementType MUL = new IElementType("MUL", CSharpLanguage.INSTANCE);

	IElementType LINE_COMMENT = new IElementType("LINE_COMMENT", CSharpLanguage.INSTANCE);

	IElementType LINE_DOC_COMMENT = new IElementType("LINE_DOC_COMMENT", CSharpLanguage.INSTANCE);

	IElementType BLOCK_COMMENT = new IElementType("BLOCK_COMMENT", CSharpLanguage.INSTANCE);

	IElementType INTEGER_LITERAL = new IElementType("INTEGER_LITERAL", CSharpLanguage.INSTANCE);

	IElementType LONG_LITERAL = new IElementType("LONG_LITERAL", CSharpLanguage.INSTANCE);

	IElementType FLOAT_LITERAL = new IElementType("FLOAT_LITERAL", CSharpLanguage.INSTANCE);

	IElementType DOUBLE_LITERAL = new IElementType("DOUBLE_LITERAL", CSharpLanguage.INSTANCE);

	IElementType CHARACTER_LITERAL = new IElementType("CHARACTER_LITERAL", CSharpLanguage.INSTANCE);

	IElementType STRING_LITERAL = new IElementType("STRING_LITERAL", CSharpLanguage.INSTANCE);

	IElementType VERBATIM_STRING_LITERAL = new IElementType("VERBATIM_STRING_LITERAL", CSharpLanguage.INSTANCE);

	IElementType IDENTIFIER = new IElementType("IDENTIFIER", CSharpLanguage.INSTANCE);
}
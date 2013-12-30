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

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author VISTALL
 * @since 22.11.13.
 */
public interface CSharpTokenSets extends CSharpSoftTokens
{
	TokenSet PRIMITIVE_TYPES = TokenSet.create(STRING_KEYWORD, VOID_KEYWORD, INT_KEYWORD, BOOL_KEYWORD, BYTE_KEYWORD, CHAR_KEYWORD, DECIMAL_KEYWORD,
			DOUBLE_KEYWORD, FLOAT_KEYWORD, LONG_KEYWORD, OBJECT_KEYWORD, SBYTE_KEYWORD, SHORT_KEYWORD, UINT_KEYWORD, ULONG_KEYWORD, USHORT_KEYWORD,
			DYNAMIC_KEYWORD, VAR_KEYWORD);

	TokenSet OVERLOADING_OPERATORS = TokenSet.create(LTEQ, GTEQ, EQEQ, PLUS, MINUS, DIV, MUL, LT, GT, PERC, AND, OR, TILDE, NTEQ, EXCL, CARET, GTGT,
			LTLT, PLUSPLUS, MINUSMINUS);

	TokenSet BINARY_OPERATORS = TokenSet.create(NULL_COALESCING, LTEQ, GTEQ, EQEQ, PLUS, MINUS, DIV, MUL, LT, GT, PERC, AND, OR, NTEQ, EXCL, CARET,
			GTGT, LTLT, EQ);

	TokenSet CONSTANT_LITERALS = TokenSet.create(INTEGER_LITERAL, STRING_LITERAL, DOUBLE_LITERAL, FLOAT_LITERAL, LONG_LITERAL, BOOL_LITERAL, NULL_LITERAL);

	TokenSet ATTRIBUTE_TARGETS = TokenSet.create(ASSEMBLY_KEYWORD, MODULE_KEYWORD, FIELD_KEYWORD, EVENT_KEYWORD, METHOD_KEYWORD, PARAM_KEYWORD,
			PROPERTY_KEYWORD, RETURN_KEYWORD, TYPE_KEYWORD);

	TokenSet TYPE_DECLARATION_START = TokenSet.create(CLASS_KEYWORD, INTERFACE_KEYWORD, STRUCT_KEYWORD, ENUM_KEYWORD);

	TokenSet EVENT_ACCESSOR_START = TokenSet.create(ADD_KEYWORD, REMOVE_KEYWORD);

	TokenSet PROPERTY_ACCESSOR_START = TokenSet.create(SET_KEYWORD, GET_KEYWORD);

	TokenSet XXX_ACCESSOR_START = TokenSet.orSet(EVENT_ACCESSOR_START, PROPERTY_ACCESSOR_START);

	TokenSet MODIFIERS = TokenSet.create(STATIC_KEYWORD, PUBLIC_KEYWORD, PARTIAL_KEYWORD, IN_KEYWORD, OUT_KEYWORD, INTERNAL_KEYWORD,
			ABSTRACT_KEYWORD, PRIVATE_KEYWORD, SEALED_KEYWORD, UNSAFE_KEYWORD, OVERRIDE_KEYWORD, REF_KEYWORD, EXTERN_KEYWORD, VIRTUAL_KEYWORD,
			PROTECTED_KEYWORD, VOLATILE_KEYWORD, PARAMS_KEYWORD, READONLY_KEYWORD, ASYNC_KEYWORD);

	TokenSet KEYWORDS = TokenSet.create(STRING_KEYWORD, STATIC_KEYWORD, CLASS_KEYWORD, USING_KEYWORD, VOID_KEYWORD, NAMESPACE_KEYWORD, NEW_KEYWORD,
			TYPEOF_KEYWORD, PUBLIC_KEYWORD, INTERFACE_KEYWORD, STRUCT_KEYWORD, ENUM_KEYWORD, INT_KEYWORD, DELEGATE_KEYWORD, IN_KEYWORD, OUT_KEYWORD,
			WHERE_KEYWORD, EVENT_KEYWORD, GLOBAL_KEYWORD, ADD_KEYWORD, REMOVE_KEYWORD, SET_KEYWORD, GET_KEYWORD, BOOL_KEYWORD, BYTE_KEYWORD,
			CHAR_KEYWORD, DECIMAL_KEYWORD, DOUBLE_KEYWORD, FLOAT_KEYWORD, LONG_KEYWORD, OBJECT_KEYWORD, SBYTE_KEYWORD, SHORT_KEYWORD, UINT_KEYWORD,
			ULONG_KEYWORD, USHORT_KEYWORD, INTERNAL_KEYWORD, ABSTRACT_KEYWORD, PRIVATE_KEYWORD, SEALED_KEYWORD, UNSAFE_KEYWORD, OVERRIDE_KEYWORD,
			REF_KEYWORD, EXTERN_KEYWORD, VIRTUAL_KEYWORD, PROTECTED_KEYWORD, VOLATILE_KEYWORD, PARAMS_KEYWORD, READONLY_KEYWORD, DYNAMIC_KEYWORD,
			CONST_KEYWORD, BOOL_KEYWORD, NULL_LITERAL, MACRO_DEFINE_KEYWORD, MACRO_ENDIF_KEYWORD, MACRO_IF_KEYWORD, OPERATOR_KEYWORD, BOOL_LITERAL,
			RETURN_KEYWORD, LOCK_KEYWORD, BREAK_KEYWORD, CONTINUE_KEYWORD);

	TokenSet COMMENTS = TokenSet.create(LINE_COMMENT, LINE_DOC_COMMENT, BLOCK_COMMENT);

	TokenSet LITERALS = TokenSet.create(CHARACTER_LITERAL, STRING_LITERAL, VERBATIM_STRING_LITERAL, INTEGER_LITERAL, LONG_LITERAL, BOOL_LITERAL,
			NULL_LITERAL);

	TokenSet STRINGS = TokenSet.create(CHARACTER_LITERAL, STRING_LITERAL, VERBATIM_STRING_LITERAL);

	TokenSet WHITESPACES = TokenSet.create(TokenType.WHITE_SPACE);
}

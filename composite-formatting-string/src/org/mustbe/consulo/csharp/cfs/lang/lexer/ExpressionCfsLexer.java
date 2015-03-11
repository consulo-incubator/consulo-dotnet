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

package org.mustbe.consulo.csharp.cfs.lang.lexer;

import org.jetbrains.annotations.NotNull;
import com.intellij.psi.tree.IElementType;

/**
 * @author VISTALL
 * @since 12.03.2015
 */
public class ExpressionCfsLexer extends BaseCfsLexer
{
	private final IElementType myExpressionElementType;

	public ExpressionCfsLexer(IElementType expressionElementType)
	{
		myExpressionElementType = expressionElementType;
	}

	@NotNull
	@Override
	public IElementType stepArgument(char c, IElementType to, int newState)
	{
		int i = 0;

		loop:while(true)
		{
			int newIndex = myIndex + i;
			if(newIndex >= myBufferEnd)
			{
				break;
			}

			c = myBuffer.charAt(newIndex);
			switch(c)
			{
				case ':':
				case ',':
				case '}':
					break loop;
				default:
					i++;
					break;
			}
		}

		myStopIndex = myIndex + i;
		myState = newState;
		return myExpressionElementType;
	}
}

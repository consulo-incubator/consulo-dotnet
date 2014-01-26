/*
 * Copyright 2000-2009 JetBrains s.r.o.
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

/*
 * @author max
 */
package org.mustbe.consulo.csharp.lang.psi;

import org.jetbrains.annotations.Nullable;
import com.intellij.lexer.Lexer;
import com.intellij.lexer.LexerPosition;
import com.intellij.psi.tree.IElementType;

public class TemplateBlackAndWhiteLexer extends Lexer
{
	private final Lexer myBaseLexer;
	private final Lexer myTemplateLexer;
	private final IElementType myTemplateElementType;
	private final IElementType myOuterElementType;
	private int myTemplateState = 0;

	public TemplateBlackAndWhiteLexer(Lexer baseLexer, Lexer templateLexer, IElementType templateElementType, IElementType outerElementType)
	{
		myTemplateLexer = templateLexer;
		myBaseLexer = baseLexer;
		myTemplateElementType = templateElementType;
		myOuterElementType = outerElementType;
	}

	@Override
	public void start(final CharSequence buffer, final int startOffset, final int endOffset, final int initialState)
	{
		myBaseLexer.start(buffer, startOffset, endOffset, initialState);
		setupTemplateToken();
	}

	@Override
	public CharSequence getBufferSequence()
	{
		return myBaseLexer.getBufferSequence();
	}

	@Override
	public int getState()
	{
		return myBaseLexer.getState();
	}

	@Override
	@Nullable
	public IElementType getTokenType()
	{
		IElementType tokenType = myBaseLexer.getTokenType();
		if(tokenType == null)
		{
			return null;
		}

		return tokenType == myTemplateElementType ? myTemplateElementType : myOuterElementType;
	}

	@Override
	public int getTokenStart()
	{
		IElementType tokenType = myBaseLexer.getTokenType();
		if(tokenType == myTemplateElementType)
		{
			return myTemplateLexer.getTokenStart();
		}
		else
		{
			return myBaseLexer.getTokenStart();
		}
	}

	@Override
	public int getTokenEnd()
	{
		IElementType tokenType = myBaseLexer.getTokenType();
		if(tokenType == myTemplateElementType)
		{
			return myTemplateLexer.getTokenEnd();
		}
		else
		{
			return myBaseLexer.getTokenEnd();
		}
	}

	@Override
	public void advance()
	{
		IElementType tokenType = myBaseLexer.getTokenType();
		if(tokenType == myTemplateElementType)
		{
			myTemplateLexer.advance();
			myTemplateState = myTemplateLexer.getState();
			if(myTemplateLexer.getTokenType() != null)
			{
				return;
			}
		}
		myBaseLexer.advance();
		setupTemplateToken();
	}

	private void setupTemplateToken()
	{
		while(true)
		{
			IElementType tokenType = myBaseLexer.getTokenType();
			if(tokenType != myTemplateElementType)
			{
				return;
			}

			myTemplateLexer.start(myBaseLexer.getBufferSequence(), myBaseLexer.getTokenStart(), myBaseLexer.getTokenEnd(), myTemplateState);
			if(myTemplateLexer.getTokenType() != null)
			{
				return;
			}
			myBaseLexer.advance();

		}
	}


	private static class Position implements LexerPosition
	{
		private final LexerPosition myTemplatePosition;
		private final LexerPosition myBasePosition;

		public Position(LexerPosition templatePosition, LexerPosition jspPosition)
		{
			myTemplatePosition = templatePosition;
			myBasePosition = jspPosition;
		}

		@Override
		public int getOffset()
		{
			return Math.max(myBasePosition.getOffset(), myTemplatePosition.getOffset());
		}

		public LexerPosition getTemplatePosition()
		{
			return myTemplatePosition;
		}

		public LexerPosition getBasePosition()
		{
			return myBasePosition;
		}

		@Override
		public int getState()
		{
			throw new UnsupportedOperationException("Method getState is not yet implemented in " + getClass().getName());
		}
	}

	@Override
	public LexerPosition getCurrentPosition()
	{
		return new Position(myTemplateLexer.getCurrentPosition(), myBaseLexer.getCurrentPosition());
	}

	@Override
	public void restore(LexerPosition position)
	{
		final Position p = (Position) position;
		myBaseLexer.restore(p.getBasePosition());
		final LexerPosition templatePos = p.getTemplatePosition();
		if(templatePos != null && templatePos.getOffset() < myTemplateLexer.getBufferEnd())
		{
			myTemplateLexer.restore(templatePos);
		}
		else
		{
			setupTemplateToken();
		}
	}

	@Override
	public int getBufferEnd()
	{
		return myBaseLexer.getBufferEnd();
	}

}

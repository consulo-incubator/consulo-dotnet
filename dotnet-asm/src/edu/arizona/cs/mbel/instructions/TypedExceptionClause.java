/* MBEL: The Microsoft Bytecode Engineering Library
 * Copyright (C) 2003 The University of Arizona
 * http://www.cs.arizona.edu/mbel/license.html
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */


package edu.arizona.cs.mbel.instructions;

/**
 * An exception clause that only catches exceptions of the given type.
 *
 * @author Michael Stepp
 */
public class TypedExceptionClause extends StructuredExceptionClause
{
	private edu.arizona.cs.mbel.mbel.AbstractTypeReference exceptionType;

	/**
	 * Creates a TypedException clause with the given instruction range, that accepts exceptions of type 'type'.
	 *
	 * @param type the type of exception this clause targets
	 * @param ts   the 'try' block start point
	 * @param te   the 'try' block end point
	 * @param hs   the handler block start point
	 * @param he   the handler end point
	 */
	public TypedExceptionClause(edu.arizona.cs.mbel.mbel.AbstractTypeReference type, InstructionHandle ts, InstructionHandle te, InstructionHandle hs,
			InstructionHandle he)
	{
		super(ts, te, hs, he);
		exceptionType = type;
	}

	/**
	 * Returns the type of exception this clause looks for
	 */
	public edu.arizona.cs.mbel.mbel.AbstractTypeReference getExceptionType()
	{
		return exceptionType;
	}
   
/*
   public void output(){
      System.out.print("TypedExceptionClause[\n  ExceptionType=");
      exceptionType.output();
      System.out.print("\n  TryStart=[");
      getTryStart().output();
      System.out.print("]\n  TryEnd=[");
      getTryEnd().output();
      System.out.print("]\n  HandlerStart=[");
      getHandlerStart().output();
      System.out.print("]\n  HandlerEnd=[");
      getHandlerEnd().output();
      System.out.print("]\n]");
   }
*/
}

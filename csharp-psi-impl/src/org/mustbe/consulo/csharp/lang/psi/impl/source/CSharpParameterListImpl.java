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

package org.mustbe.consulo.csharp.lang.psi.impl.source;

import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.csharp.lang.psi.CSharpElementVisitor;
import org.mustbe.consulo.dotnet.psi.DotNetParameter;
import org.mustbe.consulo.dotnet.psi.DotNetParameterList;
import org.mustbe.consulo.dotnet.resolve.DotNetRuntimeType;
import com.intellij.lang.ASTNode;

/**
 * @author VISTALL
 * @since 28.11.13.
 */
public class CSharpParameterListImpl extends CSharpElementImpl implements DotNetParameterList
{
	public CSharpParameterListImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public void accept(@NotNull CSharpElementVisitor visitor)
	{
		visitor.visitParameterList(this);
	}

	@NotNull
	@Override
	public DotNetParameter[] getParameters()
	{
		return findChildrenByClass(DotNetParameter.class);
	}

	@NotNull
	@Override
	public DotNetRuntimeType[] getParameterTypes()
	{
		DotNetParameter[] parameters = getParameters();
		if(parameters.length == 0)
		{
			return DotNetRuntimeType.EMPTY_ARRAY;
		}
		DotNetRuntimeType[] dotNetRuntimeTypes = new DotNetRuntimeType[parameters.length];
		for(int i = 0; i < dotNetRuntimeTypes.length; i++)
		{
			dotNetRuntimeTypes[i] = parameters[i].toRuntimeType();
		}
		return dotNetRuntimeTypes;
	}
}

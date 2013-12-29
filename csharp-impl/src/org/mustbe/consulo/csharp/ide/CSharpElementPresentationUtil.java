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

package org.mustbe.consulo.csharp.ide;

import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.dotnet.psi.DotNetConstructorDeclaration;
import org.mustbe.consulo.dotnet.psi.DotNetFieldDeclaration;
import org.mustbe.consulo.dotnet.psi.DotNetGenericParameter;
import org.mustbe.consulo.dotnet.psi.DotNetGenericParameterListOwner;
import org.mustbe.consulo.dotnet.psi.DotNetMethodDeclaration;
import org.mustbe.consulo.dotnet.psi.DotNetParameter;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.Function;

/**
 * @author VISTALL
 * @since 29.12.13.
 */
public class CSharpElementPresentationUtil
{
	@NotNull
	public static String formatMethod(@NotNull DotNetMethodDeclaration methodDeclaration)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(methodDeclaration.getName());
		formatParameters(methodDeclaration, builder);
		return builder.toString();
	}

	@NotNull
	public static String formatField(@NotNull DotNetFieldDeclaration fieldDeclaration)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(fieldDeclaration.getName());
		builder.append(":");
		builder.append(fieldDeclaration.getType().toRuntimeType().getPresentableText());
		return builder.toString();
	}

	private static void formatParameters(@NotNull DotNetMethodDeclaration methodDeclaration, @NotNull StringBuilder builder)
	{
		DotNetParameter[] parameterTypesForRuntime = methodDeclaration.getParameters();
		if(parameterTypesForRuntime.length == 0)
		{
			builder.append("()");
		}
		else
		{
			builder.append("(");
			builder.append(StringUtil.join(parameterTypesForRuntime, new Function<DotNetParameter, String>()
			{
				@Override
				public String fun(DotNetParameter parameter)
				{
					return parameter.getName() + ":" + parameter.toRuntimeType().getPresentableText();
				}
			}, ", "));
			builder.append(")");
		}

		if(!(methodDeclaration instanceof DotNetConstructorDeclaration))
		{
			builder.append(":").append(methodDeclaration.getReturnTypeForRuntime().getPresentableText());
		}
	}

	@NotNull
	public static String formatGenericParameters(@NotNull DotNetGenericParameterListOwner owner)
	{
		DotNetGenericParameter[] genericParameters = owner.getGenericParameters();
		if(genericParameters.length == 0)
		{
			return "";
		}
		return "<" + StringUtil.join(genericParameters, new Function<DotNetGenericParameter, String>()
		{
			@Override
			public String fun(DotNetGenericParameter dotNetRuntimeType)
			{
				return dotNetRuntimeType.getName();
			}
		}, ", ") + ">";
	}
}
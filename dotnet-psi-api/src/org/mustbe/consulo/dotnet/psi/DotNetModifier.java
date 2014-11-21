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

package org.mustbe.consulo.dotnet.psi;

/**
 * @author VISTALL
 * @since 12.03.14
 */
public interface DotNetModifier
{
	DotNetModifier STATIC = new DotNetModifier()
	{
		@Override
		public String getPresentableText()
		{
			return "static";
		}
	};

	DotNetModifier SEALED = new DotNetModifier()
	{
		@Override
		public String getPresentableText()
		{
			return "sealed";
		}
	};

	DotNetModifier PRIVATE = new DotNetModifier()
	{
		@Override
		public String getPresentableText()
		{
			return "private";
		}
	};

	DotNetModifier PUBLIC = new DotNetModifier()
	{
		@Override
		public String getPresentableText()
		{
			return "public";
		}
	};

	DotNetModifier PROTECTED = new DotNetModifier()
	{
		@Override
		public String getPresentableText()
		{
			return "protected";
		}
	};

	DotNetModifier INTERNAL = new DotNetModifier()
	{
		@Override
		public String getPresentableText()
		{
			return "internal";
		}
	};

	DotNetModifier ABSTRACT = new DotNetModifier()
	{
		@Override
		public String getPresentableText()
		{
			return "abstract";
		}
	};

	DotNetModifier COVARIANT = new DotNetModifier()
	{
		@Override
		public String getPresentableText()
		{
			return "covariant";
		}
	};

	DotNetModifier CONTRAVARIANT = new DotNetModifier()
	{
		@Override
		public String getPresentableText()
		{
			return "contravariant";
		}
	};

	String getPresentableText();
}

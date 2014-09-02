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

package org.mustbe.consulo.dotnet.externalAttributes;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import com.intellij.util.SmartList;

/**
 * @author VISTALL
 * @since 02.09.14
 */
public class ExternalAttributeSimpleNode
{
	private List<ExternalAttributeNode> myAttributes = new SmartList<ExternalAttributeNode>();

	private final String myName;

	public ExternalAttributeSimpleNode(String name)
	{
		myName = name;
	}

	public void addAttribute(@NotNull ExternalAttributeNode a)
	{
		myAttributes.add(a);
	}

	@NotNull
	public List<ExternalAttributeNode> getAttributes()
	{
		return myAttributes;
	}

	public String getName()
	{
		return myName;
	}
}

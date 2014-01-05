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

package org.mustbe.consulo.csharp.ide.projectView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.csharp.lang.psi.impl.source.CSharpFileImpl;
import org.mustbe.consulo.dotnet.psi.DotNetMemberOwner;
import org.mustbe.consulo.dotnet.psi.DotNetNamedElement;
import org.mustbe.consulo.dotnet.psi.DotNetNamespaceDeclaration;
import com.intellij.ide.projectView.SelectableTreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 09.12.13.
 */
public class CSharpProjectViewProvider implements SelectableTreeStructureProvider, DumbAware
{
	@Nullable
	@Override
	public PsiElement getTopLevelElement(PsiElement element)
	{
		return element.getContainingFile();
	}

	@Override
	public Collection<AbstractTreeNode> modify(AbstractTreeNode abstractTreeNode, Collection<AbstractTreeNode> abstractTreeNodes, ViewSettings
			settings)
	{
		if(!settings.isShowMembers())
		{
			return abstractTreeNodes;
		}

		List<AbstractTreeNode> nodes = new ArrayList<AbstractTreeNode>(abstractTreeNodes.size());
		for(AbstractTreeNode treeNode : abstractTreeNodes)
		{
			Object value = treeNode.getValue();
			if(value instanceof CSharpFileImpl)
			{
				DotNetNamedElement singleElement = findSingleElement((CSharpFileImpl) value);
				if(singleElement != null)
				{
					nodes.add(new CSharpQElementTreeNode(singleElement, settings));
					continue;
				}
			}

			if(value instanceof DotNetMemberOwner)
			{
				nodes.add(new CSharpElementTreeNode((DotNetMemberOwner) value, settings));
			}
			else
			{
				nodes.add(treeNode);
			}
		}
		return nodes;
	}

	@Nullable
	private static DotNetNamedElement findSingleElement(CSharpFileImpl file)
	{
		DotNetNamedElement[] members = file.getMembers();
		if(members.length != 1)
		{
			return null;
		}

		DotNetNamedElement member = members[0];
		if(member instanceof DotNetNamespaceDeclaration)
		{
			DotNetNamedElement[] namespacesDeclarations = ((DotNetNamespaceDeclaration) member).getMembers();
			if(namespacesDeclarations.length != 1)
			{
				return null;
			}
			return namespacesDeclarations[0];
		}
		else
		{
			return member;
		}
	}

	@Nullable
	@Override
	public Object getData(Collection<AbstractTreeNode> abstractTreeNodes, String s)
	{
		return null;
	}
}

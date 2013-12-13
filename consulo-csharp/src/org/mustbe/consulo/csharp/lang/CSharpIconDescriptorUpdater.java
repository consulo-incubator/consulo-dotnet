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

package org.mustbe.consulo.csharp.lang;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.csharp.lang.psi.CSharpTokens;
import org.mustbe.consulo.dotnet.psi.*;
import com.intellij.icons.AllIcons;
import com.intellij.ide.IconDescriptor;
import com.intellij.ide.IconDescriptorUpdater;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

/**
 * @author VISTALL
 * @since 09.12.13.
 */
public class CSharpIconDescriptorUpdater implements IconDescriptorUpdater
{
	@Override
	public void updateIcon(@NotNull IconDescriptor iconDescriptor, @NotNull PsiElement element, int i)
	{
		PsiFile containingFile = element.getContainingFile();
		if(containingFile == null)
		{
			return;
		}
		FileType fileType = containingFile.getFileType();
		if(fileType != CSharpFileType.INSTANCE)
		{
			return;
		}

		if(element instanceof DotNetMethodDeclaration)
		{
			iconDescriptor.setMainIcon(((DotNetMethodDeclaration) element).hasModifier(CSharpTokens.ABSTRACT_KEYWORD) ? AllIcons.Nodes
					.AbstractMethod : AllIcons.Nodes.Method);

			processModifierListOwner(element, iconDescriptor);
		}
		else if(element instanceof DotNetTypeDeclaration)
		{
			Icon main = null;
			if(((DotNetTypeDeclaration) element).isInterface())
			{
				main = AllIcons.Nodes.Interface;
			}
			else if(((DotNetTypeDeclaration) element).isEnum())
			{
				main = AllIcons.Nodes.Enum;
			}
			else
			{
				main = ((DotNetTypeDeclaration) element).hasModifier(CSharpTokens.ABSTRACT_KEYWORD) ? AllIcons.Nodes.AbstractClass : AllIcons.Nodes
						.Class;
			}
			iconDescriptor.setMainIcon(main);

			processModifierListOwner(element, iconDescriptor);
		}
		else if(element instanceof DotNetGenericParameter)
		{
			iconDescriptor.setMainIcon(AllIcons.Nodes.TypeAlias);
		}
		else if(element instanceof DotNetParameter)
		{
			iconDescriptor.setMainIcon(AllIcons.Nodes.Parameter);
		}
		else if(element instanceof DotNetFieldDeclaration)
		{
			iconDescriptor.setMainIcon(AllIcons.Nodes.Field);

			processModifierListOwner(element, iconDescriptor);
		}
		else if(element instanceof DotNetPropertyDeclaration)
		{
			iconDescriptor.setMainIcon(AllIcons.Nodes.Property);

			processModifierListOwner(element, iconDescriptor);
		}
		else if(element instanceof DotNetEventDeclaration)
		{
			iconDescriptor.setMainIcon(AllIcons.Nodes.Property);  //TODO [VISTALL] icon

			processModifierListOwner(element, iconDescriptor);
		}
		else if(element instanceof DotNetNamespaceDeclaration)
		{
			iconDescriptor.setMainIcon(AllIcons.Nodes.Package);  //TODO [VISTALL] icon
		}
		else if(element instanceof DotNetEventAccessor)
		{
			iconDescriptor.setMainIcon(AllIcons.Nodes.Method);  //TODO [VISTALL] icon

			processModifierListOwner(element, iconDescriptor);
		}
		else if(element instanceof DotNetPropertyAccessor)
		{
			iconDescriptor.setMainIcon(AllIcons.Nodes.Method);  //TODO [VISTALL] icon

			processModifierListOwner(element, iconDescriptor);
		}
	}

	private static void processModifierListOwner(PsiElement element, IconDescriptor iconDescriptor)
	{
		DotNetModifierListOwner owner = (DotNetModifierListOwner) element;
		if(owner.hasModifier(CSharpTokens.PRIVATE_KEYWORD))
		{
			iconDescriptor.setRightIcon(AllIcons.Nodes.C_private);
		}
		else if(owner.hasModifier(CSharpTokens.PUBLIC_KEYWORD))
		{
			iconDescriptor.setRightIcon(AllIcons.Nodes.C_public);
		}
		else if(owner.hasModifier(CSharpTokens.PROTECTED_KEYWORD))
		{
			iconDescriptor.setRightIcon(AllIcons.Nodes.C_protected);
		}
		else
		{
			iconDescriptor.setRightIcon(AllIcons.Nodes.C_plocal);
		}
	}
}
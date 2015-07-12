package org.mustbe.consulo.msil.ide;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.RequiredReadAction;
import org.mustbe.consulo.dotnet.DotNetTypes;
import org.mustbe.consulo.dotnet.psi.DotNetInheritUtil;
import org.mustbe.consulo.dotnet.psi.DotNetModifier;
import org.mustbe.consulo.dotnet.psi.DotNetModifierList;
import org.mustbe.consulo.dotnet.psi.DotNetModifierListOwner;
import org.mustbe.consulo.msil.lang.psi.MsilClassEntry;
import org.mustbe.consulo.msil.lang.psi.MsilTokens;
import org.mustbe.consulo.msil.lang.psi.impl.MsilNamespaceAsElementImpl;
import com.intellij.icons.AllIcons;
import com.intellij.ide.IconDescriptor;
import com.intellij.ide.IconDescriptorUpdater;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiElement;
import com.intellij.util.BitUtil;

/**
 * @author VISTALL
 * @since 05.07.2015
 */
public class MsilIconDescriptorUpdater implements IconDescriptorUpdater
{
	@Override
	@RequiredReadAction
	public void updateIcon(@NotNull IconDescriptor iconDescriptor, @NotNull PsiElement element, int flags)
	{
		if(element instanceof MsilNamespaceAsElementImpl)
		{
			iconDescriptor.setMainIcon(AllIcons.Nodes.Package);
			return;
		}

		if(element instanceof MsilClassEntry)
		{
			Icon main = null;

			MsilClassEntry typeDeclaration = (MsilClassEntry) element;
			if(!DumbService.getInstance(element.getProject()).isDumb())
			{
				if(DotNetInheritUtil.isAttribute(typeDeclaration))
				{
					main = typeDeclaration.hasModifier(MsilTokens.ABSTRACT_KEYWORD) ? AllIcons.Nodes.AbstractAttribute : AllIcons.Nodes.Attribute;
				}
				else if(DotNetInheritUtil.isException(typeDeclaration))
				{
					main = typeDeclaration.hasModifier(MsilTokens.ABSTRACT_KEYWORD) ? AllIcons.Nodes.AbstractException : AllIcons.Nodes.ExceptionClass;
				}
				else if(DotNetInheritUtil.isInheritor(typeDeclaration, DotNetTypes.System.MulticastDelegate, true))
				{
					main = AllIcons.Nodes.Method;
				}
			}

			if(main == null)
			{
				if(typeDeclaration.isInterface())
				{
					main = AllIcons.Nodes.Interface;
				}
				else if(typeDeclaration.isEnum())
				{
					main = AllIcons.Nodes.Enum;
				}
				else if(typeDeclaration.isStruct())
				{
					main = AllIcons.Nodes.Struct;
				}
				else
				{
					main = typeDeclaration.hasModifier(MsilTokens.ABSTRACT_KEYWORD) ? AllIcons.Nodes.AbstractClass : AllIcons.Nodes.Class;
				}
			}

			iconDescriptor.setMainIcon(main);

			processModifierListOwner(element, iconDescriptor, flags);
		}
	}

	@RequiredReadAction
	private static void processModifierListOwner(PsiElement element, IconDescriptor iconDescriptor, int flags)
	{
		DotNetModifierListOwner owner = (DotNetModifierListOwner) element;
		if(BitUtil.isSet(flags, Iconable.ICON_FLAG_VISIBILITY))
		{
			if(owner.hasModifier(MsilTokens.PRIVATE_KEYWORD))
			{
				iconDescriptor.setRightIcon(AllIcons.Nodes.C_private);
			}
			else if(owner.hasModifier(MsilTokens.PUBLIC_KEYWORD))
			{
				iconDescriptor.setRightIcon(AllIcons.Nodes.C_public);
			}
			else if(owner.hasModifier(MsilTokens.PROTECTED_KEYWORD))
			{
				iconDescriptor.setRightIcon(AllIcons.Nodes.C_protected);
			}
			else
			{
				iconDescriptor.setRightIcon(AllIcons.Nodes.C_plocal);
			}
		}

		DotNetModifierList modifierList = owner.getModifierList();
		if(modifierList != null && modifierList.hasModifierInTree(DotNetModifier.STATIC))
		{
			iconDescriptor.addLayerIcon(AllIcons.Nodes.StaticMark);
		}

		if(owner.hasModifier(MsilTokens.SEALED_KEYWORD))
		{
			iconDescriptor.addLayerIcon(AllIcons.Nodes.FinalMark);
		}
	}
}
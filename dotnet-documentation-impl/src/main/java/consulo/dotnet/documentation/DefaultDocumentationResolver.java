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

package consulo.dotnet.documentation;

import java.util.List;

import javax.annotation.Nonnull;

import org.emonic.base.documentation.IDocumentation;
import org.emonic.base.documentation.XMLDocumentationParser;

import javax.annotation.Nullable;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ArrayUtil;
import com.intellij.util.Function;
import consulo.annotation.access.RequiredReadAction;
import consulo.dotnet.psi.*;
import consulo.dotnet.resolve.DotNetArrayTypeRef;
import consulo.dotnet.resolve.DotNetPointerTypeRef;
import consulo.dotnet.resolve.DotNetTypeRef;

/**
 * @author VISTALL
 * @since 14.05.14
 */
public class DefaultDocumentationResolver implements DotNetDocumentationResolver
{
	@RequiredReadAction
	@Nullable
	@Override
	public IDocumentation resolveDocumentation(@Nonnull List<VirtualFile> orderEntryFiles, @Nonnull PsiElement element)
	{
		for(VirtualFile orderEntryFile : orderEntryFiles)
		{
			IDocumentation iDocumentation = resolveDocumentation(orderEntryFile, element);
			if(iDocumentation != null)
			{
				return iDocumentation;
			}
		}
		return null;
	}

	@Nullable
	private IDocumentation resolveDocumentation(
			@Nonnull VirtualFile virtualFile, @Nonnull PsiElement element)
	{
		if(virtualFile.getFileType() != XmlFileType.INSTANCE)
		{
			return null;
		}
		String docName = getDocName(element);

		return XMLDocumentationParser.findTypeDocumentation(virtualFile, docName);
	}

	private String getDocName0(PsiElement element)
	{
		if(element instanceof DotNetPropertyDeclaration ||
				element instanceof DotNetEventDeclaration ||
				element instanceof DotNetFieldDeclaration)
		{
			return getQNameForDoc(element, ((DotNetNamedElement) element).getName());
		}
		else if(element instanceof DotNetTypeDeclaration)
		{
			String name = ((DotNetTypeDeclaration) element).getName();
			int genericParametersCount = ((DotNetTypeDeclaration) element).getGenericParametersCount();
			if(genericParametersCount > 0)
			{
				name = name + '`' + genericParametersCount;
			}

			String presentableParentQName = ((DotNetTypeDeclaration) element).getPresentableParentQName();
			if(StringUtil.isEmpty(presentableParentQName))
			{
				return name;
			}
			else
			{
				return presentableParentQName + "." + name;
			}
		}
		else if(element instanceof DotNetLikeMethodDeclaration)
		{
			String name = ((DotNetLikeMethodDeclaration) element).getName();
			int genericParametersCount = ((DotNetLikeMethodDeclaration) element).getGenericParametersCount();
			if(genericParametersCount > 0)
			{
				name = name + '`' + genericParametersCount;
			}

			String fullName = getQNameForDoc(element, name);

			DotNetParameter[] parameters = ((DotNetLikeMethodDeclaration) element).getParameters();
			if(parameters.length > 0)
			{
				fullName = fullName + "(";
				fullName = fullName + StringUtil.join(parameters, new Function<DotNetParameter, String>()
				{
					@Override
					public String fun(DotNetParameter dotNetParameter)
					{
						DotNetTypeRef dotNetTypeRef = dotNetParameter.toTypeRef(true);

						return typeToDocName(dotNetParameter, dotNetTypeRef);
					}
				}, ",");
				fullName = fullName + ")";
			}
			return fullName;
		}
		else
		{
			return "test";
		}
	}

	private String getQNameForDoc(PsiElement element, String name)
	{
		String fullName;
		PsiElement parent = element.getParent();
		if(parent instanceof DotNetTypeDeclaration)
		{
			fullName = getDocName0(parent) + "." + name;
		}
		else
		{
			String presentableParentQName = ((DotNetQualifiedElement) element).getPresentableParentQName();
			if(StringUtil.isEmpty(presentableParentQName))
			{
				fullName = name;
			}
			else
			{
				fullName = presentableParentQName + "." + name;
			}
		}
		return fullName;
	}

	@RequiredReadAction
	private String typeToDocName(PsiElement element, DotNetTypeRef typeRef)
	{
		if(typeRef instanceof DotNetArrayTypeRef)
		{
			return typeToDocName(element, ((DotNetArrayTypeRef) typeRef).getInnerTypeRef()) + "[]";
		}
		else if(typeRef instanceof DotNetPointerTypeRef)
		{
			return typeToDocName(element, ((DotNetPointerTypeRef) typeRef).getInnerTypeRef()) + "*";
		}
		else
		{
			PsiElement resolve = typeRef.resolve().getElement();
			if(resolve == null)
			{
				return "<error>";
			}

			if(resolve instanceof DotNetGenericParameter)
			{
				DotNetGenericParameterListOwner generic = PsiTreeUtil.getParentOfType(resolve, DotNetGenericParameterListOwner.class);
				if(element == generic)
				{
					return "`" + ArrayUtil.indexOf(generic.getGenericParameters(), resolve);
				}
				else
				{
					// handle class parameter - in method
					return ((DotNetGenericParameter) resolve).getName();
				}
			}
			else
			{
				return getDocName0(resolve);
			}
		}
	}

	private String getDocName(PsiElement element)
	{
		String docQName = getDocName0(element);
		if(element instanceof DotNetPropertyDeclaration)
		{
			return "P:" + docQName;
		}
		else if(element instanceof DotNetEventDeclaration)
		{
			return "E:" + docQName;
		}
		else if(element instanceof DotNetTypeDeclaration)
		{
			return "T:" + docQName;
		}
		else if(element instanceof DotNetFieldDeclaration)
		{
			return "F:" + docQName;
		}
		else if(element instanceof DotNetLikeMethodDeclaration)
		{
			return "M:" + docQName;
		}
		return docQName;
	}
}

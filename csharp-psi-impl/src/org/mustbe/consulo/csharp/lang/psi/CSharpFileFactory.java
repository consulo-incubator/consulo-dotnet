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

package org.mustbe.consulo.csharp.lang.psi;

import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.csharp.lang.CSharpFileType;
import org.mustbe.consulo.csharp.lang.psi.impl.source.CSharpFileImpl;
import org.mustbe.consulo.csharp.lang.psi.impl.source.CSharpFragmentedFileImpl;
import org.mustbe.consulo.csharp.lang.psi.impl.source.CSharpUsingNamespaceListImpl;
import org.mustbe.consulo.csharp.lang.psi.impl.source.CSharpUsingNamespaceStatementImpl;
import org.mustbe.consulo.dotnet.psi.DotNetType;
import org.mustbe.consulo.dotnet.psi.DotNetTypeDeclaration;
import org.mustbe.consulo.dotnet.psi.DotNetVariable;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.SingleRootFileViewProvider;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.testFramework.LightVirtualFile;
import lombok.val;

/**
 * @author VISTALL
 * @since 30.12.13.
 */
public class CSharpFileFactory
{
	public static CSharpUsingNamespaceListImpl createUsingList(@NotNull Project project, @NotNull String qName)
	{
		val fileFromText = (CSharpFileImpl) PsiFileFactory.getInstance(project).createFileFromText("dummy.cs", CSharpFileType.INSTANCE,
				"using " + qName + ";");

		return (CSharpUsingNamespaceListImpl) fileFromText.getFirstChild();
	}

	public static CSharpUsingNamespaceListImpl createUsingListFromText(@NotNull Project project, @NotNull String text)
	{
		val fileFromText = (CSharpFileImpl) PsiFileFactory.getInstance(project).createFileFromText("dummy.cs", CSharpFileType.INSTANCE,
				text);

		return (CSharpUsingNamespaceListImpl) fileFromText.getFirstChild();
	}

	public static CSharpUsingNamespaceStatementImpl createUsingStatement(@NotNull Project project, @NotNull String qName)
	{
		val fileFromText = (CSharpFileImpl) PsiFileFactory.getInstance(project).createFileFromText("dummy.cs", CSharpFileType.INSTANCE,
				"using " + qName + ";");

		CSharpUsingNamespaceListImpl firstChild = (CSharpUsingNamespaceListImpl) fileFromText.getFirstChild();
		return firstChild.getStatements()[0];
	}

	public static DotNetType createType(@NotNull Project project, @NotNull GlobalSearchScope scope, @NotNull String typeText)
	{
		val clazz = "class _Dummy { " + typeText + " _dummy; }";

		CSharpFragmentedFileImpl psiFile = createTypeDeclarationWithScope(project, scope, clazz);

		DotNetTypeDeclaration typeDeclaration = (DotNetTypeDeclaration) psiFile.getMembers()[0];
		DotNetVariable dotNetNamedElement = (DotNetVariable) typeDeclaration.getMembers()[0];
		return dotNetNamedElement.getType();
	}

	public static DotNetTypeDeclaration createTypeDeclaration(@NotNull Project project, @NotNull GlobalSearchScope scope, @NotNull String text)
	{
		CSharpFragmentedFileImpl psiFile = createTypeDeclarationWithScope(project, scope, text);

		return (DotNetTypeDeclaration) psiFile.getMembers()[0];
	}

	private static CSharpFragmentedFileImpl createTypeDeclarationWithScope(Project project, GlobalSearchScope scope, String clazz)
	{
		val virtualFile = new LightVirtualFile("dummy.cs", CSharpFileType.INSTANCE, clazz, System.currentTimeMillis());
		val viewProvider = new SingleRootFileViewProvider(PsiManager.getInstance(project), virtualFile, false);
		val psiFile = new CSharpFragmentedFileImpl(viewProvider);
		psiFile.forceResolveScope(scope);
		return psiFile;
	}
}

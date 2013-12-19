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

package org.mustbe.consulo.packageSupport;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.QualifiedName;

/**
 * @author VISTALL
 * @since 16.12.13.
 */
public interface PackageDescriptor
{
	boolean canCreate(@NotNull String nodes, Project project, GlobalSearchScope searchScope);

	@Nullable
	PsiElement getNavigationItem(@NotNull QualifiedName qualifiedName, @NotNull Project project);

	@NotNull
	String fromQName(@NotNull QualifiedName name);

	@NotNull
	Collection<? extends PsiElement> getChildren(@NotNull QualifiedName qualifiedName, @NotNull GlobalSearchScope globalSearchScope,
		@NotNull Project project);

	@NotNull
	QualifiedName toQName(@NotNull String name);
}

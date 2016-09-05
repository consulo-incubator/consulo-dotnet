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

package org.mustbe.consulo.dotnet.run.filters;

import org.jetbrains.annotations.NotNull;
import consulo.dotnet.module.extension.DotNetSimpleModuleExtension;
import com.intellij.execution.filters.ConsoleFilterProviderEx;
import com.intellij.execution.filters.Filter;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import consulo.module.extension.ModuleExtensionHelper;

/**
 * @author VISTALL
 * @since 16.01.14
 */
public class DotNetConsoleFilterProvider implements ConsoleFilterProviderEx
{
	@Override
	public Filter[] getDefaultFilters(@NotNull Project project, @NotNull GlobalSearchScope searchScope)
	{
		if(!ModuleExtensionHelper.getInstance(project).hasModuleExtension(DotNetSimpleModuleExtension.class))
		{
			return Filter.EMPTY_ARRAY;
		}

		return new Filter[]{new DotNetExceptionFilter(project, searchScope)};
	}

	@NotNull
	@Override
	public Filter[] getDefaultFilters(@NotNull Project project)
	{
		return getDefaultFilters(project, GlobalSearchScope.allScope(project));
	}
}

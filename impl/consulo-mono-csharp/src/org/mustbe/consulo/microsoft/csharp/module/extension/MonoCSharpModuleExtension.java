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

package org.mustbe.consulo.microsoft.csharp.module.extension;

import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.csharp.module.extension.CSharpModuleExtension;
import org.mustbe.consulo.dotnet.compiler.DotNetCompilerOptionsBuilder;
import org.mustbe.consulo.dotnet.compiler.MSBaseDotNetCompilerOptionsBuilder;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;

/**
 * @author VISTALL
 * @since 26.11.13.
 */
public class MonoCSharpModuleExtension extends CSharpModuleExtension<MonoCSharpModuleExtension>
{
	public MonoCSharpModuleExtension(@NotNull String id, @NotNull Module module)
	{
		super(id, module);
	}

	@NotNull
	@Override
	public DotNetCompilerOptionsBuilder createCompilerOptionsBuilder(@NotNull Sdk dotNetSdk)
	{
		MSBaseDotNetCompilerOptionsBuilder optionsBuilder = new MSBaseDotNetCompilerOptionsBuilder(dotNetSdk);
		if(isUnsafeEnabled())
		{
			optionsBuilder.addArgument("/unsafe");
		}
		optionsBuilder.setExecutableFromSdk("mcs.exe");
		return optionsBuilder;
	}
}

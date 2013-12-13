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

import org.consulo.module.extension.impl.ModuleExtensionImpl;
import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.csharp.lang.CSharpFileType;
import org.mustbe.consulo.dotnet.compiler.DotNetCompilerOptionsBuilder;
import org.mustbe.consulo.dotnet.module.extension.DotNetModuleLangExtension;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.module.Module;

/**
 * @author VISTALL
 * @since 26.11.13.
 */
public class MicrosoftCSharpModuleExtension extends ModuleExtensionImpl<MicrosoftCSharpModuleExtension> implements
		DotNetModuleLangExtension<MicrosoftCSharpModuleExtension>

{
	public MicrosoftCSharpModuleExtension(@NotNull String id, @NotNull Module module)
	{
		super(id, module);
	}

	@Override
	public LanguageFileType getFileType()
	{
		return CSharpFileType.INSTANCE;
	}

	@Override
	public void setupCompilerOptions(DotNetCompilerOptionsBuilder optionsBuilder)
	{
		optionsBuilder.addArgument("/utf8output"); // utf8 output
		optionsBuilder.setExecutableFromSdk("csc.exe");
	}
}

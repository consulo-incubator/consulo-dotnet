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

package org.mustbe.consulo.microsoft.dotnet.module.extension2;

import javax.swing.JComponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.dotnet.module.extension.DotNetMutableModuleExtension;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;

/**
 * @author VISTALL
 * @since 20.11.13.
 */
public class MicrosoftDotNetMutableModuleExtension2 extends MicrosoftDotNetModuleExtension2 implements
		DotNetMutableModuleExtension<MicrosoftDotNetModuleExtension2>
{
	public MicrosoftDotNetMutableModuleExtension2(@NotNull String id, @NotNull Module module)
	{
		super(id, module);
	}

	@Nullable
	@Override
	public JComponent createConfigurablePanel(@NotNull ModifiableRootModel modifiableRootModel, @Nullable Runnable runnable)
	{
		return createConfigurablePanelImpl(modifiableRootModel, runnable);
	}

	@Override
	public boolean isModified(@NotNull MicrosoftDotNetModuleExtension2 extension2)
	{
		return isModifiedImpl(extension2);
	}
}
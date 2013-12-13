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

import javax.swing.JComponent;

import org.consulo.module.extension.MutableModuleExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;

/**
 * @author VISTALL
 * @since 26.11.13.
 */
public class MicrosoftCSharpMutableModuleExtension extends MicrosoftCSharpModuleExtension implements MutableModuleExtension<MicrosoftCSharpModuleExtension>
{
	private MicrosoftCSharpModuleExtension myOriginal;

	public MicrosoftCSharpMutableModuleExtension(@NotNull String id, @NotNull Module module, @NotNull MicrosoftCSharpModuleExtension original)
	{
		super(id, module);
		myOriginal = original;
	}

	@Nullable
	@Override
	public JComponent createConfigurablePanel(@NotNull ModifiableRootModel modifiableRootModel, @Nullable Runnable runnable)
	{
		return null;
	}

	@Override
	public void setEnabled(boolean b)
	{
		myIsEnabled = b;
	}

	@Override
	public boolean isModified()
	{
		return myIsEnabled != myOriginal.isEnabled();
	}

	@Override
	public void commit()
	{
		myOriginal.commit(this);
	}
}

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

package org.mustbe.consulo.csharp.module.extension;

import javax.swing.JComponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.csharp.lang.CSharpFileType;
import org.mustbe.consulo.csharp.module.CSharpConfigurationLayer;
import org.mustbe.consulo.dotnet.module.extension.DotNetModuleExtension;
import org.mustbe.consulo.dotnet.module.extension.DotNetModuleLangExtension;
import org.mustbe.consulo.module.extension.ChildLayeredModuleExtensionImpl;
import org.mustbe.consulo.module.extension.ConfigurationLayer;
import org.mustbe.consulo.module.extension.LayeredModuleExtension;
import org.mustbe.consulo.module.ui.ConfigurationProfilePanel;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.roots.ModifiableRootModel;

/**
 * @author VISTALL
 * @since 15.12.13.
 */
public abstract class CSharpModuleExtension<T extends CSharpModuleExtension<T>> extends ChildLayeredModuleExtensionImpl<T> implements
		DotNetModuleLangExtension<T>
{
	public CSharpModuleExtension(@NotNull String id, @NotNull ModifiableRootModel module)
	{
		super(id, module);
	}

	protected JComponent createConfigurablePanelImpl(@Nullable Runnable runnable)
	{
		return new ConfigurationProfilePanel(myRootModel, runnable, this);
	}

	public boolean isAllowUnsafeCode()
	{
		CSharpConfigurationLayer currentLayer = (CSharpConfigurationLayer) getCurrentLayer();
		return currentLayer.isAllowUnsafeCode();
	}

	@NotNull
	@Override
	public Class<? extends LayeredModuleExtension> getHeadClass()
	{
		return DotNetModuleExtension.class;
	}

	@NotNull
	@Override
	protected ConfigurationLayer createLayer()
	{
		return new CSharpConfigurationLayer();
	}

	@NotNull
	@Override
	public LanguageFileType getFileType()
	{
		return CSharpFileType.INSTANCE;
	}
}

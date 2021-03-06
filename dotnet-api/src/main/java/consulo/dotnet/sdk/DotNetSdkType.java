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

package consulo.dotnet.sdk;

import java.io.File;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.NonNls;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.roots.OrderRootType;
import consulo.dotnet.externalAttributes.ExternalAttributesRootOrderType;

/**
 * @author VISTALL
 * @since 20.12.13.
 */
public abstract class DotNetSdkType extends SdkType
{
	public DotNetSdkType(@NonNls String name)
	{
		super(name);
	}

	@Override
	public boolean isRootTypeApplicable(OrderRootType type)
	{
		return type == ExternalAttributesRootOrderType.getInstance();
	}

	@Nonnull
	public File getLoaderFile(@Nonnull Sdk sdk)
	{
		return getLoaderFile(getClass(), "loader.exe");
	}

	@Nonnull
	protected static File getLoaderFile(Class<?> clazz, String fileName)
	{
		return new File(new File(PluginManager.getPluginPath(clazz), "loader"), fileName);
	}
}

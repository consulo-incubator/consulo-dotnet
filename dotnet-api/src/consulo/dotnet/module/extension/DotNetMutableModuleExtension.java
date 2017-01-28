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

package consulo.dotnet.module.extension;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import consulo.dotnet.DotNetTarget;

/**
 * @author VISTALL
 * @since 01.02.14
 */
public interface DotNetMutableModuleExtension<T extends DotNetModuleExtension<T>> extends DotNetModuleExtension<T>,
		DotNetSimpleMutableModuleExtension<T>
{
	void setFileName(@NotNull String name);

	void setNamespacePrefix(@NotNull String prefix);

	void setOutputDir(@NotNull String name);

	void setAllowSourceRoots(boolean val);

	void setMainType(@Nullable String qName);

	void setTarget(@NotNull DotNetTarget target);

	void setAllowDebugInfo(boolean allowDebugInfo);
}
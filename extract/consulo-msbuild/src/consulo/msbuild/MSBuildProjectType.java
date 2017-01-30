/*
 * Copyright 2013-2017 consulo.io
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

package consulo.msbuild;

import com.intellij.openapi.extensions.ExtensionPointName;

/**
 * @author VISTALL
 * @since 28-Jan-17
 */
public interface MSBuildProjectType
{
	public static class Default implements MSBuildProjectType
	{
	}

	ExtensionPointName<MSBuildProjectTypeEP<MSBuildProjectType>> EP_NAME = ExtensionPointName.create("consulo.msbuild.projectType");
}

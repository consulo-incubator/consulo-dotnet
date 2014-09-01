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

package org.mustbe.consulo.dotnet.dll;

import org.jetbrains.annotations.NotNull;
import com.intellij.ide.highlighter.ArchiveFileType;

/**
 * @author VISTALL
 * @since 28.11.13.
 */
public class DotNetModuleFileType extends ArchiveFileType
{
	public static final DotNetModuleFileType INSTANCE = new DotNetModuleFileType();
	public static final String PROTOCOL = "netdll";

	@Override
	public String getProtocol()
	{
		return PROTOCOL;
	}

	@NotNull
	@Override
	public String getDescription()
	{
		return ".NET libraries";
	}

	@NotNull
	@Override
	public String getName()
	{
		return "DLL_ARCHIVE";
	}

	@NotNull
	@Override
	public String getDefaultExtension()
	{
		return "dll";
	}
}
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

package consulo.csharp.cfs.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import consulo.ui.image.Image;

/**
 * @author VISTALL
 * @since 31.08.14
 */
public class CfsFileType extends LanguageFileType
{
	public static final CfsFileType INSTANCE = new CfsFileType();

	public CfsFileType()
	{
		super(CfsLanguage.INSTANCE);
	}

	@Nonnull
	@Override
	public String getId()
	{
		return "CFS";
	}

	@Nonnull
	@Override
	public String getDescription()
	{
		return getDefaultExtension();
	}

	@Nonnull
	@Override
	public String getDefaultExtension()
	{
		return "cfs";
	}

	@Nullable
	@Override
	public Image getIcon()
	{
		return AllIcons.FileTypes.Text;
	}
}

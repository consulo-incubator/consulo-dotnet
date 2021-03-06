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

package consulo.msil.representation.fileSystem;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectLocator;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFilePathWrapper;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.testFramework.LightVirtualFile;
import consulo.application.AccessRule;
import consulo.msil.lang.psi.MsilFile;
import consulo.msil.representation.MsilFileRepresentationProvider;
import consulo.util.io.URLUtil;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * @author VISTALL
 * @since 27.05.14
 */
public class MsilFileRepresentationVirtualFile extends LightVirtualFile implements VirtualFilePathWrapper
{
	private final String myPath;
	private final String myIlFileUrl;
	private final MsilFileRepresentationProvider myMsilFileRepresentationProvider;
	private CharSequence myContent;

	private final Supplier<String> myPresentablePath;

	public MsilFileRepresentationVirtualFile(String name, String path, FileType fileType, VirtualFile ilFile, MsilFileRepresentationProvider msilFileRepresentationProvider)
	{
		super(name, fileType, "");
		myPath = path;
		myIlFileUrl = ilFile.getUrl();
		myMsilFileRepresentationProvider = msilFileRepresentationProvider;
		setWritable(false);

		myPresentablePath = NotNullLazyValue.createValue(() -> {
			String temp = myPath.substring(myPath.indexOf(URLUtil.ARCHIVE_SEPARATOR) + 2, myPath.length());
			temp = temp.substring(0, temp.indexOf(MsilFileRepresentationVirtualFileSystem.SEPARATOR));
			temp = temp.substring(0, temp.lastIndexOf("."));
			temp = temp + "." + myMsilFileRepresentationProvider.getFileType().getDefaultExtension();
			return temp;
		});
	}

	@Nonnull
	@Override
	public String getPresentablePath()
	{
		return myPresentablePath.get();
	}

	@Nonnull
	@Override
	public String getPath()
	{
		return myPath;
	}

	@Nonnull
	public CharSequence getContent()
	{
		if(myContent == null)
		{
			CharSequence content = buildText();
			myContent = content;
			return content;
		}
		return myContent;
	}

	@Nonnull
	private CharSequence buildText()
	{
		VirtualFile fileByUrl = VirtualFileManager.getInstance().findFileByUrl(myIlFileUrl);
		if(fileByUrl == null || !fileByUrl.isValid())
		{
			return "";
		}

		Project project = ReadAction.compute(() -> ProjectLocator.getInstance().guessProjectForFile(fileByUrl));

		if(project == null)
		{
			return "";
		}

		PsiFile file = AccessRule.read(() -> PsiManager.getInstance(project).findFile(fileByUrl));

		if(file == null)
		{
			return "";
		}

		return AccessRule.read(() -> myMsilFileRepresentationProvider.buildContent(getName(), (MsilFile) file));
	}

	@Nonnull
	@Override
	public VirtualFileSystem getFileSystem()
	{
		return MsilFileRepresentationVirtualFileSystem.getInstance();
	}
}

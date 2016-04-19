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

package consulo.dotnet.debugger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.RequiredReadAction;
import org.mustbe.consulo.dotnet.psi.DotNetTypeDeclarationUtil;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.util.containers.ContainerUtil;
import consulo.dotnet.debugger.proxy.DotNetFieldOrPropertyProxy;
import consulo.dotnet.debugger.proxy.DotNetPropertyProxy;
import consulo.dotnet.debugger.proxy.DotNetTypeProxy;

/**
 * @author VISTALL
 * @since 25.04.14
 */
public class DotNetDebuggerUtil
{
	public static DotNetFieldOrPropertyProxy[] getFieldAndProperties(@NotNull DotNetTypeProxy proxy, boolean deep)
	{
		List<DotNetFieldOrPropertyProxy> proxies = new ArrayList<DotNetFieldOrPropertyProxy>();

		collectFieldsAndProperties(proxy, proxies);

		Collections.sort(proxies, new Comparator<DotNetFieldOrPropertyProxy>()
		{
			@Override
			public int compare(DotNetFieldOrPropertyProxy o1, DotNetFieldOrPropertyProxy o2)
			{
				return weight(o1) - weight(o2);
			}

			private int weight(DotNetFieldOrPropertyProxy p)
			{
				return p instanceof DotNetPropertyProxy ? 2 : 1;
			}
		});

		return ContainerUtil.toArray(proxies, DotNetFieldOrPropertyProxy.ARRAY_FACTORY);
	}

	private static void collectFieldsAndProperties(DotNetTypeProxy proxy, List<DotNetFieldOrPropertyProxy> list)
	{
		Collections.addAll(list, proxy.getFields());
		Collections.addAll(list, proxy.getProperties());

		DotNetTypeProxy baseType = proxy.getBaseType();
		if(baseType != null)
		{
			collectFieldsAndProperties(baseType, list);
		}
	}

	@NotNull
	public static String getVmQName(@NotNull DotNetTypeProxy typeMirror)
	{
		String fullName = typeMirror.getFullName();

		// System.List`1[String]
		int i = fullName.indexOf('[');
		if(i != -1)
		{
			fullName = fullName.substring(0, i);
		}

		// change + to / separator
		fullName = fullName.replace('+', DotNetTypeDeclarationUtil.NESTED_SEPARATOR_IN_NAME);
		return fullName;
	}

	@Nullable
	@RequiredReadAction
	public static PsiElement findPsiElement(@NotNull PsiFile file, final int line)
	{
		final Document doc = FileDocumentManager.getInstance().getDocument(file.getVirtualFile());
		final PsiFile psi = doc == null ? null : PsiDocumentManager.getInstance(file.getProject()).getPsiFile(doc);
		if(psi == null)
		{
			return null;
		}

		int offset = doc.getLineStartOffset(line);
		int endOffset = doc.getLineEndOffset(line);
		for(int i = offset + 1; i < endOffset; i++)
		{
			PsiElement el = psi.findElementAt(i);
			if(el != null && !(el instanceof PsiWhiteSpace))
			{
				return el;
			}
		}

		return null;
	}

	@Nullable
	@RequiredReadAction
	public static PsiElement findPsiElement(@NotNull PsiFile file, final int line, int column)
	{
		final Document doc = FileDocumentManager.getInstance().getDocument(file.getVirtualFile());
		final PsiFile psi = doc == null ? null : PsiDocumentManager.getInstance(file.getProject()).getPsiFile(doc);
		if(psi == null)
		{
			return null;
		}

		int offset = doc.getLineStartOffset(line);
		return psi.findElementAt(offset + column);
	}
}

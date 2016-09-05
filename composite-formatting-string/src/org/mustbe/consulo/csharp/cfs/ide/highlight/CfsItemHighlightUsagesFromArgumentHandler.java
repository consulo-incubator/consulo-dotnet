/*
 * Copyright 2013-2016 must-be.org
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

package org.mustbe.consulo.csharp.cfs.ide.highlight;

import java.util.List;

import consulo.annotations.RequiredReadAction;
import org.mustbe.consulo.csharp.cfs.psi.CfsFile;
import org.mustbe.consulo.csharp.cfs.psi.CfsItem;
import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.Consumer;
import com.intellij.util.SmartList;

/**
 * @author VISTALL
 * @since 06.03.2016
 */
public class CfsItemHighlightUsagesFromArgumentHandler extends HighlightUsagesHandlerBase<PsiElement>
{
	private final PsiElement myCallArgument;
	private final CfsFile myCfsFile;
	private int myIndex;

	public CfsItemHighlightUsagesFromArgumentHandler(Editor editor, PsiFile file, PsiElement callArgument, CfsFile cfsFile, int index)
	{
		super(editor, file);
		myCallArgument = callArgument;
		myCfsFile = cfsFile;
		myIndex = index;
	}

	@Override
	public List<PsiElement> getTargets()
	{
		List<PsiElement> list = new SmartList<PsiElement>();
		for(CfsItem cfsItem : myCfsFile.getItems())
		{
			if(cfsItem.getIndex() == myIndex)
			{
				list.add(cfsItem);
			}
		}
		return list;
	}

	@Override
	protected void selectTargets(List<PsiElement> targets, Consumer<List<PsiElement>> selectionConsumer)
	{
		selectionConsumer.consume(targets);
	}

	@Override
	@RequiredReadAction
	public void computeUsages(List<PsiElement> targets)
	{
		for(PsiElement target : targets)
		{
			CfsItemHighlightUsagesHandlerFactory.addOccurrence(myReadUsages, target);
		}
		CfsItemHighlightUsagesHandlerFactory.addOccurrence(myWriteUsages, myCallArgument);
	}

	@Override
	public boolean highlightReferences()
	{
		return true;
	}
}

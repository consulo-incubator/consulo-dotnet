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

package org.mustbe.consulo.csharp.ide.highlight;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.csharp.lang.psi.impl.source.CSharpFileImpl;
import com.intellij.codeHighlighting.Pass;
import com.intellij.codeHighlighting.TextEditorHighlightingPass;
import com.intellij.codeHighlighting.TextEditorHighlightingPassFactory;
import com.intellij.codeHighlighting.TextEditorHighlightingPassRegistrar;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;

/**
 * @author VISTALL
 * @since 19.05.14
 */
public class CSharpTextEditorHighlightingPassFactory extends AbstractProjectComponent implements TextEditorHighlightingPassFactory
{
	public CSharpTextEditorHighlightingPassFactory(Project project, TextEditorHighlightingPassRegistrar registrar)
	{
		super(project);
		registrar.registerTextEditorHighlightingPass(this, new int[] {Pass.LOCAL_INSPECTIONS}, null, false, -1);
	}

	@Nullable
	@Override
	public TextEditorHighlightingPass createHighlightingPass(
			@NotNull PsiFile file, @NotNull Editor editor)
	{
		if(!(file instanceof CSharpFileImpl))
		{
			return null;
		}
		return new CSharpTextEditorHighlightingPass((CSharpFileImpl)file, editor.getDocument());
	}
}

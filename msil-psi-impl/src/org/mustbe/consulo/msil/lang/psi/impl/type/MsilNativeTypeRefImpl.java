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

package org.mustbe.consulo.msil.lang.psi.impl.type;

import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.dotnet.resolve.DotNetPsiSearcher;
import com.intellij.psi.PsiElement;

/**
 * @author VISTALL
 * @since 31.05.14
 */
public class MsilNativeTypeRefImpl extends MsilReferenceTypeRefImpl
{
	public MsilNativeTypeRefImpl(@NotNull PsiElement element, @NotNull String ref, @NotNull DotNetPsiSearcher.TypeResoleKind typeResoleKind)
	{
		super(element, ref, typeResoleKind);
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof MsilNativeTypeRefImpl && myRef.equals(((MsilNativeTypeRefImpl) obj).myRef) && myTypeResoleKind == ((MsilNativeTypeRefImpl) obj).myTypeResoleKind;
	}
}

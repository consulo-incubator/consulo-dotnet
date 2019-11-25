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

package consulo.msil.lang.psi.impl;

import javax.annotation.Nonnull;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.stubs.StubElement;
import consulo.annotation.access.RequiredReadAction;
import consulo.dotnet.psi.DotNetNamedElement;
import consulo.msil.MsilFileType;
import consulo.msil.MsilLanguage;
import consulo.msil.lang.psi.MsilFile;
import consulo.msil.lang.psi.MsilStubTokenSets;

/**
 * @author VISTALL
 * @since 21.05.14
 */
public class MsilFileImpl extends PsiFileBase implements MsilFile
{
	public MsilFileImpl(@Nonnull FileViewProvider viewProvider)
	{
		super(viewProvider, MsilLanguage.INSTANCE);
	}

	@Nonnull
	@Override
	public FileType getFileType()
	{
		return MsilFileType.INSTANCE;
	}

	@RequiredReadAction
	@Nonnull
	@Override
	public DotNetNamedElement[] getMembers()
	{
		StubElement<?> stub = getStub();
		if(stub != null)
		{
			return stub.getChildrenByType(MsilStubTokenSets.MEMBER_STUBS, DotNetNamedElement.ARRAY_FACTORY);
		}
		return findChildrenByClass(DotNetNamedElement.class);
	}
}

/*
 * Copyright 2013-2015 must-be.org
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
import javax.annotation.Nullable;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import consulo.annotation.access.RequiredReadAction;
import consulo.msil.lang.psi.MsilCustomAttribute;
import consulo.msil.lang.psi.MsilStubElements;
import consulo.msil.lang.psi.MsilTokens;
import consulo.msil.lang.psi.MsilTypeParameterAttributeList;
import consulo.msil.lang.psi.impl.elementType.stub.MsilTypeParameterAttributeListStub;

/**
 * @author VISTALL
 * @since 12.06.2015
 */
public class MsilTypeParameterAttributeListImpl extends MsilStubElementImpl<MsilTypeParameterAttributeListStub> implements MsilTypeParameterAttributeList
{
	public MsilTypeParameterAttributeListImpl(@Nonnull ASTNode node)
	{
		super(node);
	}

	public MsilTypeParameterAttributeListImpl(@Nonnull MsilTypeParameterAttributeListStub stub, @Nonnull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public void accept(MsilVisitor visitor)
	{
		visitor.visitTypeParameterAttributeList(this);
	}

	@Nullable
	@Override
	@RequiredReadAction
	public String getGenericParameterName()
	{
		MsilTypeParameterAttributeListStub stub = getGreenStub();
		if(stub != null)
		{
			return stub.getGenericParameterName();
		}
		PsiElement element = findChildByType(MsilTokens.IDENTIFIER);
		return element == null ? null : element.getText();
	}

	@Nonnull
	@Override
	@RequiredReadAction
	public MsilCustomAttribute[] getAttributes()
	{
		return getStubOrPsiChildren(MsilStubElements.CUSTOM_ATTRIBUTE, MsilCustomAttribute.ARRAY_FACTORY);
	}
}

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

package org.mustbe.consulo.msil.lang.psi.impl;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import consulo.annotations.RequiredReadAction;
import org.mustbe.consulo.dotnet.psi.DotNetModifier;
import org.mustbe.consulo.dotnet.psi.DotNetModifierList;
import org.mustbe.consulo.dotnet.psi.DotNetNamedElement;
import org.mustbe.consulo.dotnet.psi.DotNetParameter;
import org.mustbe.consulo.dotnet.psi.DotNetType;
import org.mustbe.consulo.dotnet.resolve.DotNetTypeRef;
import org.mustbe.consulo.msil.lang.psi.MsilClassEntry;
import org.mustbe.consulo.msil.lang.psi.MsilMethodEntry;
import org.mustbe.consulo.msil.lang.psi.MsilParameterList;
import org.mustbe.consulo.msil.lang.psi.MsilStubTokenSets;
import org.mustbe.consulo.msil.lang.psi.MsilTokenSets;
import org.mustbe.consulo.msil.lang.psi.MsilTokens;
import org.mustbe.consulo.msil.lang.psi.MsilXXXAcessor;
import org.mustbe.consulo.msil.lang.psi.impl.elementType.stub.MsilXXXAccessorStub;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.util.IncorrectOperationException;

/**
 * @author VISTALL
 * @since 24.05.14
 */
public class MsilXXXAccessorImpl extends MsilStubElementImpl<MsilXXXAccessorStub> implements MsilXXXAcessor
{
	private class CacheValueProvider implements CachedValueProvider<MsilMethodEntry>
	{
		@Nullable
		@Override
		public Result<MsilMethodEntry> compute()
		{
			DotNetType targetType = getStubOrPsiChildByIndex(MsilStubTokenSets.TYPE_STUBS, DotNetType.ARRAY_FACTORY, 1);
			if(targetType == null)
			{
				return null;
			}
			String name = getMethodName();
			if(name == null)
			{
				return null;
			}

			PsiElement element = targetType.toTypeRef().resolve().getElement();
			if(!(element instanceof MsilClassEntry))
			{
				return null;
			}

			DotNetTypeRef[] parameterTypeRefs = getParameterTypeRefs();

			MsilMethodEntry method = null;
			for(DotNetNamedElement namedElement : ((MsilClassEntry) element).getMembers())
			{
				if(namedElement instanceof MsilMethodEntry && Comparing.equal(((MsilMethodEntry) namedElement).getNameFromBytecode(),
						name) && Comparing.equal(((MsilMethodEntry) namedElement).getParameterTypeRefs(), parameterTypeRefs))
				{
					method = (MsilMethodEntry) namedElement;
					break;
				}
			}

			if(method == null)
			{
				return null;
			}
			return Result.create(method, PsiModificationTracker.OUT_OF_CODE_BLOCK_MODIFICATION_COUNT);
		}
	}

	private CacheValueProvider myCacheValueProvider = new CacheValueProvider();

	public MsilXXXAccessorImpl(@NotNull ASTNode node)
	{
		super(node);
	}

	public MsilXXXAccessorImpl(@NotNull MsilXXXAccessorStub stub, @NotNull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public void accept(MsilVisitor visitor)
	{

	}

	@Nullable
	@Override
	public PsiElement getCodeBlock()
	{
		return null;
	}

	@RequiredReadAction
	@Override
	public boolean hasModifier(@NotNull DotNetModifier modifier)
	{
		MsilMethodEntry msilMethodEntry = resolveToMethod();
		return msilMethodEntry != null && msilMethodEntry.hasModifier(modifier);
	}

	@RequiredReadAction
	@Nullable
	@Override
	public DotNetModifierList getModifierList()
	{
		MsilMethodEntry msilMethodEntry = resolveToMethod();
		return msilMethodEntry != null ? msilMethodEntry.getModifierList() : null;
	}

	@Nullable
	@Override
	public PsiElement getNameIdentifier()
	{
		return null;
	}

	@Override
	public PsiElement setName(@NonNls @NotNull String s) throws IncorrectOperationException
	{
		return null;
	}

	@Nullable
	@Override
	public DotNetTypeRef getReturnType()
	{
		DotNetType type = getFirstStubOrPsiChild(MsilStubTokenSets.TYPE_STUBS, DotNetType.ARRAY_FACTORY);
		if(type == null)
		{
			return DotNetTypeRef.ERROR_TYPE;
		}
		return type.toTypeRef();
	}

	@Nullable
	@Override
	public String getMethodName()
	{
		MsilXXXAccessorStub stub = getStub();
		if(stub != null)
		{
			return stub.getMethodName();
		}
		PsiElement childByType = findChildByType(MsilTokenSets.IDENTIFIERS);
		return childByType == null ? null : StringUtil.unquoteString(childByType.getText());
	}

	@NotNull
	@Override
	public DotNetParameter[] getParameters()
	{
		MsilParameterList stubOrPsiChild = getStubOrPsiChild(MsilStubTokenSets.PARAMETER_LIST);
		if(stubOrPsiChild == null)
		{
			return DotNetParameter.EMPTY_ARRAY;
		}
		return stubOrPsiChild.getParameters();
	}

	@NotNull
	@Override
	public DotNetTypeRef[] getParameterTypeRefs()
	{
		MsilParameterList stubOrPsiChild = getStubOrPsiChild(MsilStubTokenSets.PARAMETER_LIST);
		if(stubOrPsiChild == null)
		{
			return DotNetTypeRef.EMPTY_ARRAY;
		}
		return stubOrPsiChild.getParameterTypeRefs();
	}

	@Nullable
	@Override
	public MsilMethodEntry resolveToMethod()
	{
		return CachedValuesManager.getCachedValue(this, myCacheValueProvider);
	}

	@Nullable
	@Override
	public PsiElement getAccessorElement()
	{
		return null;
	}

	@Nullable
	@Override
	public Kind getAccessorKind()
	{
		MsilXXXAccessorStub stub = getStub();
		if(stub != null)
		{
			return stub.getAccessorType();
		}

		IElementType elementType = findNotNullChildByFilter(MsilTokenSets.XXX_ACCESSOR_START).getNode().getElementType();
		if(elementType == MsilTokens._GET_KEYWORD)
		{
			return Kind.GET;
		}
		else if(elementType == MsilTokens._SET_KEYWORD)
		{
			return Kind.SET;
		}
		else if(elementType == MsilTokens._ADDON_KEYWORD)
		{
			return Kind.ADD;
		}
		else if(elementType == MsilTokens._REMOVEON_KEYWORD)
		{
			return Kind.REMOVE;
		}
		return null;
	}
}

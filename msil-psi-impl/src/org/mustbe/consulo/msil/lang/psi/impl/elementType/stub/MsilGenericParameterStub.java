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

package org.mustbe.consulo.msil.lang.psi.impl.elementType.stub;

import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.dotnet.psi.DotNetGenericParameter;
import org.mustbe.consulo.dotnet.psi.DotNetModifier;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.BitUtil;
import com.intellij.util.io.StringRef;

/**
 * @author VISTALL
 * @since 23.05.14
 */
public class MsilGenericParameterStub extends NamedStubBase<DotNetGenericParameter>
{
	private static final int COVARIANT = 1 << 0;
	private static final int CONTRAVARIANT = 1 << 1;

	private int myModifierMask;

	public MsilGenericParameterStub(StubElement parent, IStubElementType elementType, @Nullable StringRef name, int modifierMask)
	{
		super(parent, elementType, name);
		myModifierMask = modifierMask;
	}

	public MsilGenericParameterStub(StubElement parent, IStubElementType elementType, @Nullable String name, int modifierMask)
	{
		super(parent, elementType, name);
		myModifierMask = modifierMask;
	}

	public static int toModifiers(DotNetGenericParameter parameter)
	{
		int i = 0;
		i = BitUtil.set(i, COVARIANT, parameter.hasModifier(DotNetModifier.COVARIANT));
		i = BitUtil.set(i, CONTRAVARIANT, parameter.hasModifier(DotNetModifier.CONTRAVARIANT));
		return i;
	}

	public int getModifierMask()
	{
		return myModifierMask;
	}

	public boolean hasModifier(DotNetModifier modifier)
	{
		if(modifier == DotNetModifier.COVARIANT)
		{
			return BitUtil.isSet(myModifierMask, COVARIANT);
		}
		else if(modifier == DotNetModifier.CONTRAVARIANT)
		{
			return BitUtil.isSet(myModifierMask, CONTRAVARIANT);
		}
		return false;
	}
}

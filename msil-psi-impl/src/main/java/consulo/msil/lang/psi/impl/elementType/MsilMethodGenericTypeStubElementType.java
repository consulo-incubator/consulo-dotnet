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

package consulo.msil.lang.psi.impl.elementType;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import consulo.annotations.RequiredReadAction;
import consulo.msil.lang.psi.MsilMethodGenericType;
import consulo.msil.lang.psi.impl.MsilMethodGenericTypeImpl;
import consulo.msil.lang.psi.impl.elementType.stub.MsilMethodGenericTypeStub;

/**
 * @author VISTALL
 * @since 22.05.14
 */
public class MsilMethodGenericTypeStubElementType extends AbstractMsilStubElementType<MsilMethodGenericTypeStub, MsilMethodGenericType>
{
	public MsilMethodGenericTypeStubElementType()
	{
		super("MSIL_METHOD_GENERIC_TYPE");
	}

	@Nonnull
	@Override
	public MsilMethodGenericType createElement(@Nonnull ASTNode astNode)
	{
		return new MsilMethodGenericTypeImpl(astNode);
	}

	@Nonnull
	@Override
	public MsilMethodGenericType createPsi(@Nonnull MsilMethodGenericTypeStub msilMethodGenericTypeStub)
	{
		return new MsilMethodGenericTypeImpl(msilMethodGenericTypeStub, this);
	}

	@RequiredReadAction
	@Override
	public MsilMethodGenericTypeStub createStub(@Nonnull MsilMethodGenericType msilMethodGenericType, StubElement stubElement)
	{
		int index = msilMethodGenericType.getIndex();
		return new MsilMethodGenericTypeStub(stubElement, this, index);
	}

	@Override
	public void serialize(@Nonnull MsilMethodGenericTypeStub stub, @Nonnull StubOutputStream stubOutputStream) throws IOException
	{
		stubOutputStream.writeInt(stub.getIndex());
	}

	@Nonnull
	@Override
	public MsilMethodGenericTypeStub deserialize(@Nonnull StubInputStream inputStream, StubElement stubElement) throws IOException
	{
		int index = inputStream.readInt();
		return new MsilMethodGenericTypeStub(stubElement, this, index);
	}
}

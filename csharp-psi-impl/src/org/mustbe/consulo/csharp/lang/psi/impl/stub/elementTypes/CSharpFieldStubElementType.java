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

package org.mustbe.consulo.csharp.lang.psi.impl.stub.elementTypes;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.csharp.lang.psi.impl.source.CSharpFieldDeclarationImpl;
import org.mustbe.consulo.csharp.lang.psi.impl.stub.CSharpFieldStub;
import org.mustbe.consulo.dotnet.psi.DotNetFieldDeclaration;
import org.mustbe.consulo.dotnet.psi.stub.index.DotNetIndexKeys;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;

/**
 * @author VISTALL
 * @since 21.12.13.
 */
public class CSharpFieldStubElementType extends CSharpAbstractStubElementType<CSharpFieldStub, DotNetFieldDeclaration>
{
	public CSharpFieldStubElementType()
	{
		super("FIELD_DECLARATION");
	}

	@Override
	public DotNetFieldDeclaration createPsi(@NotNull ASTNode astNode)
	{
		return new CSharpFieldDeclarationImpl(astNode);
	}

	@Override
	public DotNetFieldDeclaration createPsi(@NotNull CSharpFieldStub fieldStub)
	{
		return new CSharpFieldDeclarationImpl(fieldStub);
	}

	@Override
	public CSharpFieldStub createStub(@NotNull DotNetFieldDeclaration fieldDeclaration, StubElement stubElement)
	{
		return new CSharpFieldStub(stubElement, StringRef.fromNullableString(fieldDeclaration.getName()),
				StringRef.fromNullableString(fieldDeclaration.getPresentableParentQName()));
	}

	@Override
	public void serialize(@NotNull CSharpFieldStub fieldStub, @NotNull StubOutputStream stubOutputStream) throws IOException
	{
		stubOutputStream.writeName(fieldStub.getName());
		stubOutputStream.writeName(fieldStub.getParentQName());
	}

	@NotNull
	@Override
	public CSharpFieldStub deserialize(@NotNull StubInputStream stubInputStream, StubElement stubElement) throws IOException
	{
		StringRef name = stubInputStream.readName();
		StringRef parentQName = stubInputStream.readName();
		return new CSharpFieldStub(stubElement, name, parentQName);
	}

	@Override
	public void indexStub(@NotNull CSharpFieldStub cSharpFieldStub, @NotNull IndexSink indexSink)
	{
		String name = cSharpFieldStub.getName();
		if(!StringUtil.isEmpty(name))
		{
			indexSink.occurrence(DotNetIndexKeys.FIELD_INDEX, name);
		}
	}
}

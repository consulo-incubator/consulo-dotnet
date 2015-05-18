package org.mustbe.consulo.msil.lang.psi.impl.elementType.stub;

import org.mustbe.consulo.msil.lang.psi.MsilCustomAttributeSignature;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;

/**
 * @author VISTALL
 * @since 18.05.2015
 */
public class MsilCustomAttributeSignatureStub extends StubBase<MsilCustomAttributeSignature>
{
	private byte[] myBytes;

	public MsilCustomAttributeSignatureStub(StubElement parent, IStubElementType elementType, byte[] bytes)
	{
		super(parent, elementType);
		myBytes = bytes;
	}

	public byte[] getBytes()
	{
		return myBytes;
	}
}

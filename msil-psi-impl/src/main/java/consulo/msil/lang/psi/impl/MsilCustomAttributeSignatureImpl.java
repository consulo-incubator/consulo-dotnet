package consulo.msil.lang.psi.impl;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import consulo.annotation.access.RequiredReadAction;
import consulo.msil.lang.psi.MsilCustomAttributeSignature;
import consulo.msil.lang.psi.MsilTokens;
import consulo.msil.lang.psi.impl.elementType.stub.MsilCustomAttributeSignatureStub;

/**
 * @author VISTALL
 * @since 18.05.2015
 */
public class MsilCustomAttributeSignatureImpl extends MsilStubElementImpl<MsilCustomAttributeSignatureStub> implements MsilCustomAttributeSignature
{
	public MsilCustomAttributeSignatureImpl(@Nonnull ASTNode node)
	{
		super(node);
	}

	public MsilCustomAttributeSignatureImpl(@Nonnull MsilCustomAttributeSignatureStub stub, @Nonnull IStubElementType nodeType)
	{
		super(stub, nodeType);
	}

	@Override
	public void accept(MsilVisitor visitor)
	{
		visitor.visitCustomAttributeSignature(this);
	}

	@Nonnull
	@Override
	@RequiredReadAction
	public byte[] getBytes()
	{
		MsilCustomAttributeSignatureStub stub = getGreenStub();
		if(stub != null)
		{
			return stub.getBytes();
		}

		PsiElement[] child = findChildrenByType(MsilTokens.HEX_NUMBER_LITERAL, PsiElement.class);
		byte[] bytes = new byte[child.length];
		for(int i = 0; i < child.length; i++)
		{
			PsiElement psiElement = child[i];
			byte b = 0;
			try
			{
				b = (byte) Integer.parseInt(psiElement.getText(), 16);
			}
			catch(NumberFormatException ignored)
			{
			}
			bytes[i] = b;
		}
		return bytes;
	}
}

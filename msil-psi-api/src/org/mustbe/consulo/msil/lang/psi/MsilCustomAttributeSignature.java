package org.mustbe.consulo.msil.lang.psi;

import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.RequiredReadAction;
import org.mustbe.consulo.dotnet.psi.DotNetElement;

/**
 * @author VISTALL
 * @since 18.05.2015
 */
public interface MsilCustomAttributeSignature extends DotNetElement
{
	@NotNull
	@RequiredReadAction
	byte[] getBytes();
}
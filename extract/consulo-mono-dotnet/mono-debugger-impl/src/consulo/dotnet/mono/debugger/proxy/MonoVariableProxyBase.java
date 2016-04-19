package consulo.dotnet.mono.debugger.proxy;

import org.consulo.util.pointers.Named;
import org.jetbrains.annotations.NotNull;
import consulo.dotnet.debugger.proxy.DotNetVariableProxy;
import mono.debugger.MirrorWithIdAndName;

/**
 * @author VISTALL
 * @since 19.04.2016
 */
public abstract class MonoVariableProxyBase<T extends MirrorWithIdAndName> implements Named, DotNetVariableProxy
{
	protected T myMirror;

	public MonoVariableProxyBase(@NotNull T mirror)
	{
		myMirror = mirror;
	}

	@NotNull
	public T getMirror()
	{
		return myMirror;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof MonoVariableProxyBase && myMirror.equals(((MonoVariableProxyBase) obj).myMirror);
	}

	@Override
	public int hashCode()
	{
		return myMirror.hashCode();
	}

	@NotNull
	@Override
	public String getName()
	{
		return myMirror.name();
	}
}
import org.mustbe.consulo.dotnet.dll.vfs.builder.util.XStubUtil;

/**
 * @author VISTALL
 * @since 19.04.14
 */
public class GenericTest
{
	public static void main(String[] args)
	{
		String qualifiedName = "test`1";

		int index = qualifiedName.indexOf(XStubUtil.GENERIC_MARKER_IN_NAME);

		int genericCount = 0;
		if(index != -1)
		{
			genericCount = Integer.parseInt(qualifiedName.substring(index + 1, qualifiedName.length()));
			qualifiedName = qualifiedName.substring(0, index);
		}
		System.out.println("b");
	}
}
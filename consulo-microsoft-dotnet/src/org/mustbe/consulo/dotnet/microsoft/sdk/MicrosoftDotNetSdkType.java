package org.mustbe.consulo.dotnet.microsoft.sdk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.dotnet.microsoft.MicrosoftDotNetIcons;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.projectRoots.AdditionalDataConfigurable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModel;
import com.intellij.openapi.projectRoots.SdkModificator;
import com.intellij.openapi.projectRoots.SdkTable;
import com.intellij.openapi.projectRoots.SdkType;
import com.intellij.openapi.projectRoots.impl.SdkConfigurationUtil;
import com.intellij.openapi.projectRoots.impl.SdkImpl;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.util.Consumer;
import lombok.val;

/**
 * @author VISTALL
 * @since 23.11.13.
 */
public class MicrosoftDotNetSdkType extends SdkType
{
	public MicrosoftDotNetSdkType()
	{
		super("MICROSOFT_DOTNET_SDK");
	}

	@Nullable
	@Override
	public String suggestHomePath()
	{
		return null;
	}

	@Override
	public boolean isValidSdkHome(String s)
	{
		return new File(s, "csc.exe").exists();
	}

	@Nullable
	@Override
	public String getVersionString(String s)
	{
		return new File(s).getName();
	}

	@Override
	public String suggestSdkName(String s, String s2)
	{
		File file = new File(s2);
		if(file.getParent().equalsIgnoreCase("Framework64"))
		{
			return getPresentableName() + " " + file.getName() + "(64)";
		}
		else
		{
			return getPresentableName() + " " + file.getName();
		}
	}

	@Nullable
	@Override
	public AdditionalDataConfigurable createAdditionalDataConfigurable(SdkModel sdkModel, SdkModificator sdkModificator)
	{
		return null;
	}

	@NotNull
	@Override
	public String getPresentableName()
	{
		return "Microsoft .NET";
	}

	@Override
	public void saveAdditionalData(SdkAdditionalData sdkAdditionalData, Element element)
	{

	}

	@Override
	public boolean isRootTypeApplicable(OrderRootType type)
	{
		return type == OrderRootType.CLASSES;
	}

	@Nullable
	@Override
	public Icon getIcon()
	{
		return MicrosoftDotNetIcons.DotNet;
	}

	@Override
	public boolean supportsCustomCreateUI()
	{
		return SystemInfo.isWindows;
	}

	@Override
	public void showCustomCreateUI(SdkModel sdkModel, JComponent parentComponent, final Consumer<Sdk> sdkCreatedCallback)
	{
		String windir = System.getenv("windir");


		File microNet = new File(windir, "Microsoft.NET");

		List<Pair<String, File>> list = new ArrayList<Pair<String, File>>();

		File framework = new File(microNet, "Framework");
		if(framework.exists())
		{
			File[] files = framework.listFiles();
			if(files != null)
			{
				for(File file : files)
				{
					if(isValidSdkHome(file.getAbsolutePath()))
					{
						list.add(new Pair<String, File>(file.getName(), file));
					}
				}
			}
		}

		framework = new File(microNet, "Framework64");
		if(framework.exists())
		{
			File[] files = framework.listFiles();
			if(files != null)
			{
				for(File file : files)
				{
					if(isValidSdkHome(file.getAbsolutePath()))
					{
						list.add(new Pair<String, File>(file.getName() + "(x64)", file));
					}
				}
			}
		}

		val thisSdks = SdkTable.getInstance().getSdksOfType(this);
		DefaultActionGroup actionGroup = new DefaultActionGroup();
		for(val pair : list)
		{
			actionGroup.add(new AnAction(pair.getFirst())
			{
				@Override
				public void actionPerformed(AnActionEvent anActionEvent)
				{
					val path = pair.getSecond();
					val absolutePath = path.getAbsolutePath();

					String uniqueSdkName = SdkConfigurationUtil.createUniqueSdkName(MicrosoftDotNetSdkType.this, absolutePath,
							thisSdks);
					SdkImpl sdk = new SdkImpl(uniqueSdkName, MicrosoftDotNetSdkType.this);
					sdk.setVersionString(getVersionString(absolutePath));
					sdk.setHomePath(absolutePath);

					sdkCreatedCallback.consume(sdk);
				}
			});
		}

		DataContext dataContext = DataManager.getInstance().getDataContext(parentComponent);

		ListPopup choose = JBPopupFactory.getInstance().createActionGroupPopup("Choose", actionGroup, dataContext,
				JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false);

		choose.showInCenterOf(parentComponent);
	}
}

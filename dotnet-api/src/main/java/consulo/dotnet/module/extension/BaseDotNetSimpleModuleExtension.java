/*
 * Copyright 2013-2015 must-be.org
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

package consulo.dotnet.module.extension;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.jdom.Document;
import org.jdom.Element;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.cl.PluginClassLoader;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Couple;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.util.ArrayUtil;
import com.intellij.util.Processor;
import com.intellij.util.SmartList;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.io.URLUtil;
import consulo.annotations.RequiredReadAction;
import consulo.dotnet.dll.DotNetModuleFileType;
import consulo.dotnet.externalAttributes.ExternalAttributesRootOrderType;
import consulo.dotnet.module.DotNetNamespaceGeneratePolicy;
import consulo.internal.dotnet.asm.mbel.ModuleParser;
import consulo.module.extension.ModuleInheritableNamedPointer;
import consulo.module.extension.impl.ModuleExtensionImpl;
import consulo.roots.ModuleRootLayer;
import consulo.roots.types.BinariesOrderRootType;
import consulo.roots.types.DocumentationOrderRootType;
import consulo.vfs.util.ArchiveVfsUtil;

/**
 * @author VISTALL
 * @since 22.02.2015
 */
public abstract class BaseDotNetSimpleModuleExtension<S extends BaseDotNetSimpleModuleExtension<S>> extends ModuleExtensionImpl<S> implements DotNetSimpleModuleExtension<S>
{
	public static final Logger LOGGER = Logger.getInstance(BaseDotNetSimpleModuleExtension.class);

	public static final File[] EMPTY_FILE_ARRAY = new File[0];

	protected Sdk myLastSdk;

	protected List<String> myVariables = new ArrayList<String>();
	protected Map<String, Map<OrderRootType, String[]>> myUrlsCache = new HashMap<String, Map<OrderRootType, String[]>>();
	protected DotNetModuleSdkPointer mySdkPointer;

	public BaseDotNetSimpleModuleExtension(@Nonnull String id, @Nonnull ModuleRootLayer moduleRootLayer)
	{
		super(id, moduleRootLayer);
		mySdkPointer = new DotNetModuleSdkPointer(moduleRootLayer.getProject(), id);
	}

	@Nonnull
	@Override
	public List<String> getVariables()
	{
		return myVariables;
	}

	@Override
	public boolean isSupportCompilation()
	{
		return false;
	}

	@Nonnull
	@Override
	public DotNetNamespaceGeneratePolicy getNamespaceGeneratePolicy()
	{
		return DotNetNamespaceGeneratePolicy.DEFAULT;
	}

	@Nonnull
	@Override
	public Map<String, String> getAvailableSystemLibraries()
	{
		Map<String, String> map = new TreeMap<String, String>();
		File[] directoriesForLibraries = getFilesForLibraries();
		for(File childFile : directoriesForLibraries)
		{
			if(!FileUtilRt.getExtension(childFile.getName()).equals("dll"))
			{
				continue;
			}
			Couple<String> info = parseLibrary(childFile);
			if(info == null)
			{
				continue;
			}
			map.put(info.getFirst(), info.getSecond());
		}
		return map;
	}

	@Nonnull
	@Override
	public ModuleInheritableNamedPointer<Sdk> getInheritableSdk()
	{
		return mySdkPointer;
	}

	@Nullable
	@Override
	public Sdk getSdk()
	{
		return getInheritableSdk().get();
	}

	@Nullable
	@Override
	public String getSdkName()
	{
		return getInheritableSdk().getName();
	}

	@Nonnull
	@Override
	public String[] getSystemLibraryUrls(@Nonnull String name, @Nonnull OrderRootType orderRootType)
	{
		Sdk sdk = getSdk();
		if(sdk == null)
		{
			return ArrayUtil.EMPTY_STRING_ARRAY;
		}

		if(!Comparing.equal(myLastSdk, sdk))
		{
			myLastSdk = sdk;
			myUrlsCache.clear();
		}

		Map<OrderRootType, String[]> orderRootTypeMap = myUrlsCache.get(name);
		if(orderRootTypeMap == null)
		{
			myUrlsCache.put(name, orderRootTypeMap = new HashMap<OrderRootType, String[]>());
		}

		String[] urls = orderRootTypeMap.get(orderRootType);
		if(urls == null)
		{
			orderRootTypeMap.put(orderRootType, urls = getSystemLibraryUrlsImpl(sdk, name, orderRootType));
		}

		return urls;
	}

	@RequiredReadAction
	@Override
	protected void loadStateImpl(@Nonnull Element element)
	{
		mySdkPointer.fromXml(element);

		for(Element defineElement : element.getChildren("define"))
		{
			myVariables.add(defineElement.getText());
		}
	}

	@Override
	protected void getStateImpl(@Nonnull Element element)
	{
		mySdkPointer.toXml(element);
		for(String variable : myVariables)
		{
			element.addContent(new Element("define").setText(variable));
		}
	}

	@Override
	public void commit(@Nonnull S mutableModuleExtension)
	{
		super.commit(mutableModuleExtension);

		mySdkPointer.set(mutableModuleExtension.getInheritableSdk());
		myVariables.clear();
		myVariables.addAll(mutableModuleExtension.myVariables);
	}

	public boolean isModifiedImpl(S ex)
	{
		return myIsEnabled != ex.isEnabled() ||
				!mySdkPointer.equals(ex.getInheritableSdk()) ||
				!myVariables.equals(ex.getVariables());
	}

	@Nullable
	private File getLibraryByAssemblyName(@Nonnull final String name, @Nullable Ref<Couple<String>> cache)
	{
		File[] filesForLibraries = getFilesForLibraries();
		String nameWithExtension = name + ".dll";
		File singleFile = ContainerUtil.find(filesForLibraries, new Condition<File>()
		{
			@Override
			public boolean value(File file)
			{
				return file.getName().equalsIgnoreCase(nameWithExtension);
			}
		});

		if(singleFile != null && isValidLibrary(singleFile, name, cache))
		{
			return singleFile;
		}

		for(File childFile : filesForLibraries)
		{
			if(isValidLibrary(childFile, name, cache))
			{
				return childFile;
			}
		}
		return null;
	}

	@Nonnull
	protected String[] getSystemLibraryUrlsImpl(@Nonnull Sdk sdk, String name, OrderRootType orderRootType)
	{
		if(orderRootType == BinariesOrderRootType.getInstance())
		{
			File libraryByAssemblyName = getLibraryByAssemblyName(name, null);
			if(libraryByAssemblyName == null)
			{
				return ArrayUtil.EMPTY_STRING_ARRAY;
			}

			return new String[]{
					VirtualFileManager.constructUrl(DotNetModuleFileType.PROTOCOL, libraryByAssemblyName.getPath()) + URLUtil.ARCHIVE_SEPARATOR
			};
		}
		else if(orderRootType == DocumentationOrderRootType.getInstance())
		{
			String[] systemLibraryUrls = getSystemLibraryUrls(name, BinariesOrderRootType.getInstance());
			if(systemLibraryUrls.length != 1)
			{
				return ArrayUtil.EMPTY_STRING_ARRAY;
			}
			VirtualFile libraryFile = VirtualFileManager.getInstance().findFileByUrl(systemLibraryUrls[0]);
			if(libraryFile == null)
			{
				return ArrayUtil.EMPTY_STRING_ARRAY;
			}
			VirtualFile localFile = ArchiveVfsUtil.getVirtualFileForArchive(libraryFile);
			if(localFile == null)
			{
				return ArrayUtil.EMPTY_STRING_ARRAY;
			}
			VirtualFile docFile = localFile.getParent().findChild(localFile.getNameWithoutExtension() + ".xml");
			if(docFile != null)
			{
				return new String[]{docFile.getUrl()};
			}
			return ArrayUtil.EMPTY_STRING_ARRAY;
		}
		else if(orderRootType == ExternalAttributesRootOrderType.getInstance())
		{
			try
			{
				final Ref<Couple<String>> ref = Ref.create();
				File libraryFile = getLibraryByAssemblyName(name, ref);
				if(libraryFile == null)
				{
					return ArrayUtil.EMPTY_STRING_ARRAY;
				}

				assert ref.get() != null;

				File dir = new File(PluginManager.getPluginPath(BaseDotNetSimpleModuleExtension.class), "externalAttributes");

				List<String> urls = new SmartList<String>();
				String requiredFileName = name + ".xml";
				FileUtil.visitFiles(dir, new Processor<File>()
				{
					@Override
					public boolean process(File file)
					{
						if(file.isDirectory())
						{
							return true;
						}
						if(Comparing.equal(requiredFileName, file.getName(), false) && isValidExternalFile(ref.get().getSecond(), file))
						{
							urls.add(VfsUtil.pathToUrl(file.getPath()));
						}
						return true;
					}
				});

				return ArrayUtil.toStringArray(urls);
			}
			catch(Exception e)
			{
				BaseDotNetSimpleModuleExtension.LOGGER.error(e);
			}
		}
		return ArrayUtil.EMPTY_STRING_ARRAY;
	}

	private boolean isValidLibrary(@Nonnull File file, @Nonnull final String name, @Nullable Ref<Couple<String>> cache)
	{
		Couple<String> info = parseLibrary(file);
		if(info == null)
		{
			return false;
		}
		if(Comparing.equal(info.getFirst(), name))
		{
			if(cache != null)
			{
				cache.set(info);
			}
			return true;
		}
		return false;
	}

	private static boolean isValidExternalFile(String version, File toCheck)
	{
		try
		{
			Document document = JDOMUtil.loadDocument(toCheck);
			String versionPattern = document.getRootElement().getChildText("version");
			return Pattern.compile(versionPattern).matcher(version).find();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	@Nonnull
	public File[] getFilesForLibraries()
	{
		Sdk sdk = getSdk();
		if(sdk == null)
		{
			return EMPTY_FILE_ARRAY;
		}

		String homePath = sdk.getHomePath();
		if(homePath == null)
		{
			return EMPTY_FILE_ARRAY;
		}
		File[] files = new File(homePath).listFiles();
		return files == null ? EMPTY_FILE_ARRAY : files;
	}

	@Nullable
	private static Couple<String> parseLibrary(File f)
	{
		DotNetLibraryOpenCache.Record record = null;
		try
		{
			record = DotNetLibraryOpenCache.acquire(f.getPath());
			ModuleParser moduleParser = record.get();
			return Couple.of(moduleParser.getAssemblyInfo().getName(), moduleParser.getAssemblyInfo().getMajorVersion() + "." + moduleParser.getAssemblyInfo().getMinorVersion() +
					"." + moduleParser.getAssemblyInfo().getBuildNumber() + "." + moduleParser.getAssemblyInfo().getRevisionNumber());
		}
		catch(Exception e)
		{
			return null;
		}
		finally
		{
			if(record != null)
			{
				record.finish();
			}
		}
	}
}

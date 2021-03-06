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

package consulo.dotnet.module.dependency;

import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.ui.configuration.classpath.ClasspathPanel;
import consulo.dotnet.module.extension.DotNetModuleExtensionWithLibraryProviding;
import consulo.dotnet.roots.orderEntry.DotNetLibraryOrderEntryImpl;
import consulo.module.extension.ModuleExtension;
import consulo.roots.ModifiableModuleRootLayer;
import consulo.roots.impl.ModuleRootLayerImpl;
import consulo.roots.ui.configuration.LibrariesConfigurator;
import consulo.roots.ui.configuration.ModulesConfigurator;
import consulo.roots.ui.configuration.classpath.AddModuleDependencyContext;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author VISTALL
 * @since 28.09.14
 */
public class DotNetLibraryModuleDependencyContext extends AddModuleDependencyContext<List<Map.Entry<String, String>>>
{
	public DotNetLibraryModuleDependencyContext(ClasspathPanel classpathPanel, ModulesConfigurator modulesConfigurator, LibrariesConfigurator librariesConfigurator)
	{
		super(classpathPanel, modulesConfigurator, librariesConfigurator);
	}

	@Nonnull
	@Override
	public List<OrderEntry> createOrderEntries(@Nonnull ModifiableModuleRootLayer layer, @Nonnull List<Map.Entry<String, String>> selectedValues)
	{
		List<OrderEntry> orderEntries = new ArrayList<>(selectedValues.size());
		for(Map.Entry<String, String> selectedValue : selectedValues)
		{
			DotNetLibraryOrderEntryImpl orderEntry = new DotNetLibraryOrderEntryImpl((ModuleRootLayerImpl) layer, selectedValue.getKey());

			layer.addOrderEntry(orderEntry);

			orderEntries.add(orderEntry);
		}
		return orderEntries;
	}

	@Nonnull
	public Map<String, String> getAvailableSystemLibraries()
	{
		Map<String, String> map = new HashMap<>();
		ModifiableRootModel rootModel = myClasspathPanel.getRootModel();
		for(ModuleExtension<?> moduleExtension : rootModel.getExtensions())
		{
			if(moduleExtension instanceof DotNetModuleExtensionWithLibraryProviding)
			{
				map.putAll(((DotNetModuleExtensionWithLibraryProviding<?>) moduleExtension).getAvailableSystemLibraries());
			}
		}
		return map;
	}

	public static OrderEntry findOrderEntry(String name, ModifiableModuleRootLayer layer)
	{
		OrderEntry[] orderEntries = layer.getOrderEntries();
		for(OrderEntry orderEntry : orderEntries)
		{
			if(orderEntry instanceof DotNetLibraryOrderEntryImpl && orderEntry.getPresentableName().equalsIgnoreCase(name))
			{
				return orderEntry;
			}
		}
		return null;
	}
}

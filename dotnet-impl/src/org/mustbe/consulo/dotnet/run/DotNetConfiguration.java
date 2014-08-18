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

package org.mustbe.consulo.dotnet.run;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.dotnet.compiler.DotNetMacroUtil;
import org.mustbe.consulo.dotnet.execution.DebugConnectionInfo;
import org.mustbe.consulo.dotnet.module.extension.DotNetModuleExtension;
import com.intellij.execution.CommonProgramRunConfigurationParameters;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ModuleBasedConfiguration;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.XmlSerializer;
import lombok.val;

/**
 * @author VISTALL
 * @since 26.11.13.
 */
public class DotNetConfiguration extends ModuleBasedConfiguration<RunConfigurationModule> implements CommonProgramRunConfigurationParameters
{
	private String myProgramParameters;
	private String myWorkingDir = "";
	private Map<String, String> myEnvsMap = Collections.emptyMap();
	private boolean myPassParentEnvs = true;

	public DotNetConfiguration(String name, RunConfigurationModule configurationModule, ConfigurationFactory factory)
	{
		super(name, configurationModule, factory);
	}

	public DotNetConfiguration(RunConfigurationModule configurationModule, ConfigurationFactory factory)
	{
		super(configurationModule, factory);
	}

	@Override
	public Collection<Module> getValidModules()
	{
		val list = new ArrayList<Module>();
		for(val module : ModuleManager.getInstance(getProject()).getModules())
		{
			if(ModuleUtilCore.getExtension(module, DotNetModuleExtension.class) != null)
			{
				list.add(module);
			}
		}
		return list;
	}

	@Override
	public void readExternal(Element element) throws InvalidDataException
	{
		super.readExternal(element);
		readModule(element);

		XmlSerializer.deserializeInto(this, element);
	}

	@Override
	public void writeExternal(Element element) throws WriteExternalException
	{
		super.writeExternal(element);
		writeModule(element);

		XmlSerializer.serializeInto(this, element);
	}

	@NotNull
	@Override
	public SettingsEditor<? extends RunConfiguration> getConfigurationEditor()
	{
		return new DotNetConfigurationEditor(getProject());
	}

	@Nullable
	@Override
	public RunProfileState getState(@NotNull Executor executor, @NotNull final ExecutionEnvironment executionEnvironment) throws ExecutionException
	{
		val module = getConfigurationModule().getModule();
		if(module == null)
		{
			throw new ExecutionException("Module is null");
		}

		DotNetModuleExtension<?> extension = ModuleUtilCore.getExtension(module, DotNetModuleExtension.class);

		if(extension == null)
		{
			throw new ExecutionException("Module don't have .NET extension");
		}

		val exeFile = DotNetMacroUtil.expandOutputFile(extension);

		DebugConnectionInfo debugConnectionInfo = null;
		if(executor instanceof DefaultDebugExecutor)
		{
			debugConnectionInfo = new DebugConnectionInfo("127.0.0.1", -1, true);
		}

		DotNetConfiguration runProfile = (DotNetConfiguration) executionEnvironment.getRunProfile();
		val runCommandLine = extension.createDefaultCommandLine(exeFile, debugConnectionInfo);
		String programParameters = runProfile.getProgramParameters();
		if(!StringUtil.isEmpty(programParameters))
		{
			runCommandLine.addParameters(StringUtil.split(programParameters, " "));
		}
		runCommandLine.setPassParentEnvironment(runProfile.isPassParentEnvs());
		runCommandLine.getEnvironment().putAll(runProfile.getEnvs());
		runCommandLine.setWorkDirectory(DotNetMacroUtil.expand(module, runProfile.getWorkingDirectory(), false));
		return new DotNetRunProfileState(exeFile, executionEnvironment, runCommandLine, debugConnectionInfo);
	}

	@Override
	public void setProgramParameters(@Nullable String s)
	{
		myProgramParameters = s;
	}

	@Nullable
	@Override
	public String getProgramParameters()
	{
		return myProgramParameters;
	}

	@Override
	public void setWorkingDirectory(@Nullable String s)
	{
		myWorkingDir = s;
	}

	@Nullable
	@Override
	public String getWorkingDirectory()
	{
		return myWorkingDir;
	}

	@Override
	public void setEnvs(@NotNull Map<String, String> map)
	{
		myEnvsMap = map;
	}

	@NotNull
	@Override
	public Map<String, String> getEnvs()
	{
		return myEnvsMap;
	}

	@Override
	public void setPassParentEnvs(boolean b)
	{
		myPassParentEnvs = b;
	}

	@Override
	public boolean isPassParentEnvs()
	{
		return myPassParentEnvs;
	}
}

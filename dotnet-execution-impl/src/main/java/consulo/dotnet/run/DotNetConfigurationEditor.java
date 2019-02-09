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

package consulo.dotnet.run;

import java.awt.event.ItemEvent;

import javax.annotation.Nonnull;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import com.intellij.application.options.ModuleListCellRenderer;
import com.intellij.execution.ui.CommonProgramParametersPanel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.FormBuilder;
import consulo.ui.RequiredUIAccess;
import consulo.dotnet.module.extension.DotNetRunModuleExtension;

/**
 * @author VISTALL
 * @since 26.11.13.
 */
public class DotNetConfigurationEditor extends SettingsEditor<DotNetConfiguration>
{
	private final Project myProject;

	private JComboBox myModuleComboBox;
	private CommonProgramParametersPanel myProgramParametersPanel;

	public DotNetConfigurationEditor(Project project)
	{
		myProject = project;
	}

	@Override
	protected void resetEditorFrom(DotNetConfiguration runConfiguration)
	{
		myProgramParametersPanel.reset(runConfiguration);
		myModuleComboBox.setSelectedItem(runConfiguration.getConfigurationModule().getModule());
		myProgramParametersPanel.setModuleContext(runConfiguration.getConfigurationModule().getModule());
	}

	@Override
	protected void applyEditorTo(DotNetConfiguration runConfiguration) throws ConfigurationException
	{
		myProgramParametersPanel.applyTo(runConfiguration);
		runConfiguration.getConfigurationModule().setModule((Module) myModuleComboBox.getSelectedItem());
	}

	@Nonnull
	@Override
	@RequiredUIAccess
	protected JComponent createEditor()
	{
		myProgramParametersPanel = new CommonProgramParametersPanel();

		myModuleComboBox = new JComboBox();
		myModuleComboBox.setRenderer(new ModuleListCellRenderer());
		for(Module module : ModuleManager.getInstance(myProject).getModules())
		{
			if(ModuleUtilCore.getExtension(module, DotNetRunModuleExtension.class) != null)
			{
				myModuleComboBox.addItem(module);
			}
		}
		myModuleComboBox.addItemListener(e ->
		{
			if(e.getStateChange() == ItemEvent.SELECTED)
			{
				myProgramParametersPanel.setModuleContext((Module) myModuleComboBox.getSelectedItem());
			}
		});

		FormBuilder formBuilder = FormBuilder.createFormBuilder();
		formBuilder.addLabeledComponent("Module", myModuleComboBox);

		myProgramParametersPanel.add(formBuilder.getPanel());
		return myProgramParametersPanel;
	}
}

package org.mustbe.consulo.csharp.module.extension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JList;
import javax.swing.JPanel;

import org.consulo.module.extension.MutableModuleInheritableNamedPointer;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.ColoredListCellRendererWrapper;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.JBCheckBox;
import lombok.val;

/**
 * @author VISTALL
 * @since 31.07.14
 */
public class CSharpConfigurationPanel extends JPanel
{
	public CSharpConfigurationPanel(final CSharpMutableModuleExtension<?> ext)
	{
		super(new VerticalFlowLayout());
		final ComboBox levelComboBox = new ComboBox();
		levelComboBox.setRenderer(new ColoredListCellRendererWrapper<Object>()
		{
			@Override
			protected void doCustomize(JList list, Object value, int index, boolean selected, boolean hasFocus)
			{
				if(value instanceof CSharpLanguageVersion)
				{
					final CSharpLanguageVersion languageLevel = (CSharpLanguageVersion) value;
					append(languageLevel.getPresentableName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
					append(" ");
					append(languageLevel.getDescription(), SimpleTextAttributes.GRAY_ATTRIBUTES);
				}
				else if(value instanceof Module)
				{
					setIcon(AllIcons.Nodes.Module);
					append(((Module) value).getName(), SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES);

					final CSharpModuleExtension extension = ModuleUtilCore.getExtension((Module) value, CSharpModuleExtension.class);
					if(extension != null)
					{
						final CSharpLanguageVersion languageLevel = extension.getLanguageVersion();
						append("(" + languageLevel.getPresentableName() + ")", SimpleTextAttributes.GRAY_ATTRIBUTES);
					}
				}
				else if(value instanceof String)
				{
					setIcon(AllIcons.Nodes.Module);
					append((String) value, SimpleTextAttributes.ERROR_BOLD_ATTRIBUTES);
				}
			}
		});

		for(CSharpLanguageVersion languageLevel : CSharpLanguageVersion.values())
		{
			levelComboBox.addItem(languageLevel);
		}

		for(Module module : ModuleManager.getInstance(ext.getProject()).getModules())
		{
			// dont add self e
			if(module == ext.getModule())
			{
				continue;
			}

			final CSharpModuleExtension extension = (CSharpModuleExtension) ModuleUtilCore.getExtension(module, ext.getId());
			if(extension != null)
			{
				levelComboBox.addItem(module);
			}
		}

		final MutableModuleInheritableNamedPointer<CSharpLanguageVersion> languageVersionPointer = ext.getLanguageVersionPointer();
		final String moduleName = languageVersionPointer.getModuleName();
		if(moduleName != null)
		{
			final Module module = languageVersionPointer.getModule();
			if(module != null)
			{
				levelComboBox.setSelectedItem(module);
			}
			else
			{
				levelComboBox.addItem(moduleName);
			}
		}
		else
		{
			levelComboBox.setSelectedItem(languageVersionPointer.get());
		}

		levelComboBox.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				final Object selectedItem = levelComboBox.getSelectedItem();
				if(selectedItem instanceof Module)
				{
					languageVersionPointer.set(((Module) selectedItem).getName(), null);
				}
				else if(selectedItem instanceof CSharpLanguageVersion)
				{
					languageVersionPointer.set(null, ((CSharpLanguageVersion) selectedItem).getName());
				}
				else
				{
					languageVersionPointer.set(selectedItem.toString(), null);
				}
			}
		});

		val comp = new JBCheckBox("Allow unsafe code?", ext.isAllowUnsafeCode());
		comp.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ext.setAllowUnsafeCode(comp.isSelected());
			}
		});

		add(LabeledComponent.left(levelComboBox, "Language Version: "));
		add(comp);
	}
}
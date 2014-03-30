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

package org.mustbe.consulo.nunit.run;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.dotnet.module.extension.DotNetModuleExtension;
import org.mustbe.consulo.dotnet.psi.DotNetMethodDeclaration;
import org.mustbe.consulo.dotnet.psi.DotNetNamedElement;
import org.mustbe.consulo.dotnet.psi.DotNetTypeDeclaration;
import org.mustbe.consulo.dotnet.resolve.DotNetPsiFacade;
import com.intellij.execution.Location;
import com.intellij.execution.PsiLocation;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.testframework.sm.runner.SMTestProxy;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.QualifiedName;
import com.intellij.util.ui.UIUtil;

/**
 * @author VISTALL
 * @since 28.03.14
 */
public class NUnitProcessAdapter extends ProcessAdapter
{
	private static final String TEST_STARTED = "***** ";
	private static final Pattern RESULT_PATTERN = Pattern.compile("(\\d+)\\) ([\\s\\w]+) : ([\\s\\w\\W]+)");

	private final Module myModule;
	private final SMTestProxy.SMRootTestProxy myRootTestProxy;
	private final NUnitTestsOutputConsoleView myTestsOutputConsoleView;

	private boolean myResults;

	public NUnitProcessAdapter(Module module, SMTestProxy.SMRootTestProxy rootTestProxy, NUnitTestsOutputConsoleView testsOutputConsoleView)
	{
		myModule = module;
		myRootTestProxy = rootTestProxy;
		myTestsOutputConsoleView = testsOutputConsoleView;
		rootTestProxy.setStarted();
	}

	@Override
	public void onTextAvailable(ProcessEvent event, Key outputType)
	{
		String text = event.getText();
		if(text.startsWith(TEST_STARTED))
		{
			String testName = text.substring(TEST_STARTED.length(), text.length()).trim();
			QualifiedName qualifiedName = QualifiedName.fromDottedString(testName);

			SMTestProxy proxy = myRootTestProxy;
			List<String> components = qualifiedName.getComponents();
			for(int i = 0; i < components.size(); i++)
			{
				String name = components.get(i);

				boolean isLastElement = components.size() - 1 == i;
				proxy = getOrCreateProxy(proxy, name, isLastElement, isLastElement ? findLocation(qualifiedName) : null);
			}

			myTestsOutputConsoleView.getResultsPanel().getTreeBuilder().queueUpdate();
		}
		else if(text.startsWith("Errors and Failures:"))
		{
			myResults = true;
		}
		else if(myResults)
		{
			Matcher matcher = RESULT_PATTERN.matcher(text);
			if(matcher.find())
			{
				String result = matcher.group(2).trim();
				String test = matcher.group(3).trim();

				if("Test Failure".equals(result))
				{
					QualifiedName qualifiedName = QualifiedName.fromDottedString(test);

					SMTestProxy temp = myRootTestProxy;
					for(String s : qualifiedName.getComponents())
					{
						temp = getOrCreateProxy(temp, s, false, null);
					}

					temp.setFinished();
					temp.setTestFailed("failed", null, true);
					UIUtil.invokeLaterIfNeeded(new Runnable()
					{
						@Override
						public void run()
						{
							myTestsOutputConsoleView.getResultsPanel().getTreeBuilder().queueUpdate();
						}
					});
				}
			}
		}
	}

	@Override
	public void processTerminated(ProcessEvent event)
	{
		super.processTerminated(event);

		setFinished(myRootTestProxy);

		UIUtil.invokeLaterIfNeeded(new Runnable()
		{
			@Override
			public void run()
			{
				myTestsOutputConsoleView.getResultsPanel().getTreeBuilder().queueUpdate();
			}
		});
	}

	public PsiLocation<?> findLocation(final QualifiedName qualifiedName)
	{
		return new ReadAction<PsiLocation<?>>()
		{
			@Override
			protected void run(Result<PsiLocation<?>> psiLocationResult) throws Throwable
			{
				QualifiedName parent = qualifiedName.getParent();
				String lastComponent = qualifiedName.getLastComponent();

				DotNetModuleExtension dotNetModuleExtension = ModuleUtilCore.getExtension(myModule, DotNetModuleExtension.class);

				assert dotNetModuleExtension != null;

				DotNetTypeDeclaration[] types = DotNetPsiFacade.getInstance(myModule.getProject()).findTypes(parent.toString(),
						dotNetModuleExtension.getScopeForResolving(true), 0);

				for(DotNetTypeDeclaration type : types)
				{
					for(DotNetNamedElement dotNetNamedElement : type.getMembers())
					{
						if(dotNetNamedElement instanceof DotNetMethodDeclaration && lastComponent.equals(dotNetNamedElement.getName()))
						{
							psiLocationResult.setResult(new PsiLocation<PsiElement>(myModule.getProject(), myModule, dotNetNamedElement));
						}
					}
				}
			}
		}.execute().getResultObject();
	}

	private static void setFinished(SMTestProxy proxy)
	{
		if(!proxy.hasErrors())
		{
			proxy.setFinished();
		}

		for(SMTestProxy smTestProxy : proxy.getChildren())
		{
			setFinished(smTestProxy);
		}
	}

	private static SMTestProxy getOrCreateProxy(SMTestProxy proxy, String name, boolean last, final Location location)
	{
		for(SMTestProxy smTestProxy : proxy.getChildren())
		{
			if(smTestProxy.getName().equals(name))
			{
				return smTestProxy;
			}
		}
		SMTestProxy temp = new SMTestProxy(name, !last, null)
		{
			@Nullable
			@Override
			public Location getLocation(Project project, GlobalSearchScope searchScope)
			{
				return location;
			}
		};
		temp.setStarted();
		proxy.addChild(temp);
		return temp;
	}
}
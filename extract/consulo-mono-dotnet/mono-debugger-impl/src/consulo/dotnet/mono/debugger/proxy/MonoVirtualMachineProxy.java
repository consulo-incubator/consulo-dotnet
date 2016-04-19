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

package consulo.dotnet.mono.debugger.proxy;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.dotnet.module.extension.DotNetModuleLangExtension;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.MultiMap;
import com.intellij.xdebugger.breakpoints.XBreakpoint;
import consulo.dotnet.debugger.proxy.DotNetThreadProxy;
import consulo.dotnet.debugger.proxy.DotNetVirtualMachineProxy;
import consulo.dotnet.debugger.proxy.value.DotNetBooleanValueProxy;
import consulo.dotnet.debugger.proxy.value.DotNetCharValueProxy;
import consulo.dotnet.debugger.proxy.value.DotNetNullValueProxy;
import consulo.dotnet.debugger.proxy.value.DotNetNumberValueProxy;
import consulo.dotnet.debugger.proxy.value.DotNetStringValueProxy;
import consulo.dotnet.mono.debugger.MonoDebugUtil;
import consulo.dotnet.mono.debugger.TypeMirrorUnloadedException;
import mono.debugger.AppDomainMirror;
import mono.debugger.AssemblyMirror;
import mono.debugger.BooleanValueMirror;
import mono.debugger.NoObjectValueMirror;
import mono.debugger.ThreadMirror;
import mono.debugger.TypeMirror;
import mono.debugger.VMDisconnectedException;
import mono.debugger.VirtualMachine;
import mono.debugger.request.EventRequest;
import mono.debugger.request.EventRequestManager;
import mono.debugger.request.StepRequest;

/**
 * @author VISTALL
 * @since 16.04.2015
 */
public class MonoVirtualMachineProxy implements DotNetVirtualMachineProxy
{
	private final Map<Integer, AppDomainMirror> myLoadedAppDomains = ContainerUtil.newConcurrentMap();
	private final Set<StepRequest> myStepRequests = ContainerUtil.newLinkedHashSet();
	private final MultiMap<XBreakpoint, EventRequest> myBreakpointEventRequests = MultiMap.create();

	private final VirtualMachine myVirtualMachine;

	private final boolean mySupportSearchTypesBySourcePaths;
	private final boolean mySupportSearchTypesByQualifiedName;
	private final boolean mySupportSystemThreadId;

	public MonoVirtualMachineProxy(@NotNull VirtualMachine virtualMachine)
	{
		myVirtualMachine = virtualMachine;
		mySupportSearchTypesByQualifiedName = myVirtualMachine.isAtLeastVersion(2, 9);
		mySupportSearchTypesBySourcePaths = myVirtualMachine.isAtLeastVersion(2, 7);
		mySupportSystemThreadId = myVirtualMachine.isAtLeastVersion(2, 2);
	}

	@NotNull
	@Override
	public List<DotNetThreadProxy> getThreads()
	{
		return ContainerUtil.map(myVirtualMachine.allThreads(), new Function<ThreadMirror, DotNetThreadProxy>()
		{
			@Override
			public DotNetThreadProxy fun(ThreadMirror threadMirror)
			{
				return new MonoThreadProxy(MonoVirtualMachineProxy.this, threadMirror);
			}
		});
	}

	@NotNull
	@Override
	public DotNetStringValueProxy createStringValue(@NotNull String value)
	{
		return MonoValueProxyUtil.wrap(myVirtualMachine.rootAppDomain().createString(value));
	}

	@NotNull
	@Override
	public DotNetCharValueProxy createCharValue(char value)
	{
		return null;
	}

	@NotNull
	@Override
	public DotNetBooleanValueProxy createBooleanValue(boolean value)
	{
		return MonoValueProxyUtil.wrap(new BooleanValueMirror(myVirtualMachine, value));
	}

	@NotNull
	@Override
	public DotNetNumberValueProxy createNumberValue(int tag, @NotNull Number value)
	{
		return null;
	}

	@NotNull
	@Override
	public DotNetNullValueProxy createNullValue()
	{
		return MonoValueProxyUtil.wrap(new NoObjectValueMirror(myVirtualMachine));
	}

	public boolean isSupportSystemThreadId()
	{
		return mySupportSystemThreadId;
	}

	public void dispose()
	{
		myStepRequests.clear();
		myVirtualMachine.dispose();
		myBreakpointEventRequests.clear();
	}

	public void addStepRequest(@NotNull StepRequest stepRequest)
	{
		myStepRequests.add(stepRequest);
	}

	public void stopStepRequest(@NotNull StepRequest stepRequest)
	{
		stepRequest.disable();
		myStepRequests.remove(stepRequest);
	}

	public void putRequest(@NotNull XBreakpoint<?> breakpoint, @NotNull EventRequest request)
	{
		myBreakpointEventRequests.putValue(breakpoint, request);
	}

	public void stopBreakpointRequests(XBreakpoint<?> breakpoint)
	{
		Collection<EventRequest> eventRequests = myBreakpointEventRequests.remove(breakpoint);
		if(eventRequests == null)
		{
			return;
		}
		for(EventRequest eventRequest : eventRequests)
		{
			eventRequest.disable();
		}
		myVirtualMachine.eventRequestManager().deleteEventRequests(eventRequests);
	}

	public void stopStepRequests()
	{
		for(StepRequest stepRequest : myStepRequests)
		{
			stepRequest.disable();
		}
		myStepRequests.clear();
	}

	public EventRequestManager eventRequestManager()
	{
		return myVirtualMachine.eventRequestManager();
	}

	@NotNull
	public VirtualMachine getDelegate()
	{
		return myVirtualMachine;
	}

	@Nullable
	public TypeMirror findTypeMirror(@NotNull Project project, @NotNull final VirtualFile virtualFile, @NotNull final String vmQualifiedName) throws TypeMirrorUnloadedException
	{
		try
		{
			if(mySupportSearchTypesByQualifiedName)
			{
				TypeMirror[] typesByQualifiedName = myVirtualMachine.findTypesByQualifiedName(vmQualifiedName, false);
				return typesByQualifiedName.length == 0 ? null : typesByQualifiedName[0];
			}
			else if(mySupportSearchTypesBySourcePaths)
			{
				TypeMirror[] typesBySourcePath = myVirtualMachine.findTypesBySourcePath(virtualFile.getPath(), SystemInfo.isFileSystemCaseSensitive);
				return ContainerUtil.find(typesBySourcePath, new Condition<TypeMirror>()
				{
					@Override
					public boolean value(TypeMirror typeMirror)
					{
						return Comparing.equal(MonoDebugUtil.getVmQName(typeMirror), vmQualifiedName);
					}
				});
			}
			else
			{
				Module moduleForFile = ModuleUtilCore.findModuleForFile(virtualFile, project);
				if(moduleForFile == null)
				{
					return null;
				}

				final DotNetModuleLangExtension<?> extension = ModuleUtilCore.getExtension(moduleForFile, DotNetModuleLangExtension.class);
				if(extension == null)
				{
					return null;
				}

				final String assemblyTitle = ApplicationManager.getApplication().runReadAction(new Computable<String>()
				{
					@Override
					public String compute()
					{
						return extension.getAssemblyTitle();
					}
				});

				for(AppDomainMirror appDomainMirror : myLoadedAppDomains.values())
				{
					AssemblyMirror[] assemblies = appDomainMirror.assemblies();
					for(AssemblyMirror assembly : assemblies)
					{
						String assemblyName = getAssemblyName(assembly.name());
						if(assemblyTitle.equals(assemblyName))
						{
							TypeMirror typeByQualifiedName = assembly.findTypeByQualifiedName(vmQualifiedName, false);
							if(typeByQualifiedName != null)
							{
								return typeByQualifiedName;
							}
						}
					}
				}
				return null;
			}
		}
		catch(VMDisconnectedException e)
		{
			return null;
		}
	}

	@NotNull
	private static String getAssemblyName(String name)
	{
		int i = name.indexOf(',');
		if(i == -1)
		{
			return name;
		}
		return name.substring(0, i);
	}

	public void resume()
	{
		myVirtualMachine.resume();
	}

	public void suspend()
	{
		myVirtualMachine.suspend();
	}

	@NotNull
	public List<ThreadMirror> allThreads()
	{
		return myVirtualMachine.allThreads();
	}

	public void loadAppDomain(AppDomainMirror appDomainMirror)
	{
		myLoadedAppDomains.put(appDomainMirror.id(), appDomainMirror);
	}

	public void unloadAppDomain(AppDomainMirror appDomainMirror)
	{
		myLoadedAppDomains.remove(appDomainMirror.id());
	}
}
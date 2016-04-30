/*
 * Copyright 2013-2016 must-be.org
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

package consulo.dotnet.debugger.proxy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import consulo.dotnet.debugger.proxy.value.DotNetValueProxy;

/**
 * @author VISTALL
 * @since 18.04.2016
 */
public interface DotNetStackFrameProxy
{
	int getIndex();

	@NotNull
	DotNetThreadProxy getThread();

	@NotNull
	Object getEqualityObject();

	@Nullable
	DotNetSourceLocation getSourceLocation();

	@NotNull
	DotNetValueProxy getThisObject() throws DotNetInvalidObjectException, DotNetInvalidStackFrameException, DotNetAbsentInformationException;

	@Nullable
	DotNetValueProxy getParameterValue(@NotNull DotNetMethodParameterProxy parameterProxy);

	void setParameterValue(@NotNull DotNetMethodParameterProxy parameterProxy, @NotNull DotNetValueProxy valueProxy);

	@Nullable
	DotNetValueProxy getLocalValue(@NotNull DotNetLocalVariableProxy localVariableProxy);

	void setLocalValue(@NotNull DotNetLocalVariableProxy localVariableProxy, @NotNull DotNetValueProxy valueProxy);
}

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

package org.mustbe.consulo.msil.lang.psi;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.RequiredReadAction;
import org.mustbe.consulo.dotnet.psi.DotNetMethodDeclaration;

/**
 * @author VISTALL
 * @since 21.05.14
 */
public interface MsilMethodEntry extends MsilEntry, DotNetMethodDeclaration
{
	@NotNull
	String getNameFromBytecode();

	@NotNull
	@RequiredReadAction
	MsilCustomAttribute[] getAttributes();

	/**
	 * @param index zero based index
	 */
	@NotNull
	@RequiredReadAction
	MsilCustomAttribute[] getParameterAttributes(int index);

	/**
	 * @param index zero based index
	 */
	@Nullable
	@RequiredReadAction
	MsilConstantValue getConstantValue(int index);
}

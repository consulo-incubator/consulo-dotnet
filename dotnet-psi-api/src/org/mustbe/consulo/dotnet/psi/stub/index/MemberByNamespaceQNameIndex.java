/*
 * Copyright 2013 must-be.org
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

package org.mustbe.consulo.dotnet.psi.stub.index;

import org.jetbrains.annotations.NotNull;
import org.mustbe.consulo.dotnet.psi.DotNetNamedElement;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;

/**
 * @author VISTALL
 * @since 18.12.13.
 */
public class MemberByNamespaceQNameIndex extends StringStubIndexExtension<DotNetNamedElement>
{
	public static MemberByNamespaceQNameIndex getInstance()
	{
		return StubIndexExtension.EP_NAME.findExtension(MemberByNamespaceQNameIndex.class);
	}

	@NotNull
	@Override
	public StubIndexKey<String, DotNetNamedElement> getKey()
	{
		return DotNetIndexKeys.MEMBER_BY_NAMESPACE_QNAME_INDEX;
	}
}

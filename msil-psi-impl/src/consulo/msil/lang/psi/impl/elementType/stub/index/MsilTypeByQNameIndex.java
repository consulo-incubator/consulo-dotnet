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

package consulo.msil.lang.psi.impl.elementType.stub.index;

import org.jetbrains.annotations.NotNull;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import consulo.msil.lang.psi.MsilClassEntry;

/**
 * @author VISTALL
 * @since 22.05.14
 */
public class MsilTypeByQNameIndex extends StringStubIndexExtension<MsilClassEntry>
{
	@NotNull
	public static MsilTypeByQNameIndex getInstance()
	{
		return EP_NAME.findExtension(MsilTypeByQNameIndex.class);
	}

	@NotNull
	@Override
	public StubIndexKey<String, MsilClassEntry> getKey()
	{
		return MsilIndexKeys.TYPE_BY_QNAME_INDEX;
	}
}

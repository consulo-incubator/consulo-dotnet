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

package consulo.dotnet.run;

import consulo.dotnet.execution.DebugConnectionInfo;
import consulo.util.dataholder.Key;

/**
 * @author VISTALL
 * @since 31.01.15
 */
public interface DotNetRunKeys
{
	Key<DebugConnectionInfo> DEBUG_CONNECTION_INFO_KEY = Key.create("DEBUG_CONNECTION_INFO_KEY");
}

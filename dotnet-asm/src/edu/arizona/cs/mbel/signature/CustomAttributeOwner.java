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

package edu.arizona.cs.mbel.signature;

import org.jetbrains.annotations.NotNull;
import edu.arizona.cs.mbel.mbel.CustomAttribute;

/**
 * @author VISTALL
 * @since 09.01.14
 */
public interface CustomAttributeOwner
{
	void addCustomAttribute(@NotNull CustomAttribute ca);

	CustomAttribute[] getCustomAttributes();

	void removeCustomAttribute(@NotNull CustomAttribute ca);
}
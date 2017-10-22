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

package consulo.dotnet.debugger.nodes;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.NullableFunction;
import com.intellij.xdebugger.frame.XCompositeNode;
import com.intellij.xdebugger.frame.XValueChildrenList;
import com.intellij.xdebugger.frame.XValueModifier;
import com.intellij.xdebugger.frame.XValueNode;
import com.intellij.xdebugger.frame.XValuePlace;
import consulo.dotnet.DotNetTypes;
import consulo.dotnet.debugger.DotNetDebugContext;
import consulo.dotnet.debugger.nodes.logicView.DotNetLogicValueView;
import consulo.dotnet.debugger.proxy.DotNetErrorValueProxyImpl;
import consulo.dotnet.debugger.proxy.DotNetStackFrameProxy;
import consulo.dotnet.debugger.proxy.DotNetThrowValueException;
import consulo.dotnet.debugger.proxy.DotNetTypeProxy;
import consulo.dotnet.debugger.proxy.DotNetVirtualMachineProxy;
import consulo.dotnet.debugger.proxy.value.DotNetErrorValueProxy;
import consulo.dotnet.debugger.proxy.value.DotNetNullValueProxy;
import consulo.dotnet.debugger.proxy.value.DotNetValueProxy;

/**
 * @author VISTALL
 * @since 11.04.14
 */
public abstract class DotNetAbstractVariableValueNode extends AbstractTypedValueNode
{
	private XValueModifier myValueModifier = new XValueModifier()
	{
		@Override
		public void setValue(@NotNull String expression, @NotNull XModificationCallback callback)
		{
			DotNetTypeProxy typeOfVariable = getTypeOfVariable();
			assert typeOfVariable != null;
			DotNetVirtualMachineProxy virtualMachine = myDebugContext.getVirtualMachine();

			TypeTag typeTag = typeTag(typeOfVariable);
			assert typeTag != null;

			DotNetValueProxy setValue = null;
			switch(typeTag)
			{
				case String:
					if(expression.equals("null"))
					{
						setValue = virtualMachine.createNullValue();
					}
					else
					{
						expression = StringUtil.unquoteString(expression);

						setValue = virtualMachine.createStringValue(expression);
					}
					break;
				case Char:
					String chars = StringUtil.unquoteString(expression);
					if(chars.length() == 1)
					{
						setValue = virtualMachine.createCharValue(chars.charAt(0));
					}
					break;
				case Boolean:
					setValue = virtualMachine.createBooleanValue(Boolean.valueOf(expression));
					break;
				default:
					setValue = virtualMachine.createNumberValue(typeTag.getTag(), Double.parseDouble(expression));
					break;
			}

			if(setValue != null)
			{
				setValueForVariable(setValue);
			}

			callback.valueModified();
		}

		@Override
		@Nullable
		public String getInitialValueEditorText()
		{
			DotNetValueProxy valueOfVariable = getValueOfVariable();
			if(valueOfVariable == null)
			{
				return null;
			}

			if(valueOfVariable instanceof DotNetNullValueProxy)
			{
				return "null";
			}

			String valueOfString = String.valueOf(valueOfVariable.getValue());
			TypeTag typeTag = typeTag(null);
			assert typeTag != null;

			if(typeTag == TypeTag.String)
			{
				return StringUtil.QUOTER.fun(valueOfString);
			}
			else if(typeTag == TypeTag.Char)
			{
				return StringUtil.SINGLE_QUOTER.fun(valueOfString);
			}
			return valueOfString;
		}
	};

	@NotNull
	protected final DotNetStackFrameProxy myFrameProxy;
	private final UserDataHolderBase myDataHolder = new UserDataHolderBase();

	public DotNetAbstractVariableValueNode(@NotNull DotNetDebugContext debuggerContext, @NotNull String name, @NotNull DotNetStackFrameProxy frameProxy)
	{
		super(debuggerContext, name);
		myFrameProxy = frameProxy;
	}

	@Nullable
	public TypeTag typeTag(@Nullable DotNetTypeProxy alreadyCalledType)
	{
		DotNetTypeProxy typeOfVariable = alreadyCalledType != null ? alreadyCalledType : getTypeOfVariable();
		if(typeOfVariable == null)
		{
			return null;
		}
		return TypeTag.byType(typeOfVariable.getFullName());
	}

	@NotNull
	public Icon getIconForVariable(@Nullable DotNetValueProxy alreadyCalledValue)
	{
		DotNetTypeProxy typeOfVariable = getTypeOfVariableValue(alreadyCalledValue);
		if(typeOfVariable == null)
		{
			return AllIcons.Debugger.Value;
		}

		if(typeOfVariable.isArray())
		{
			return AllIcons.Debugger.Db_array;
		}

		TypeTag typeTag = typeTag(typeOfVariable);
		if(typeTag != null && typeTag != TypeTag.String)
		{
			return AllIcons.Debugger.Db_primitive;
		}

		DotNetTypeProxy baseType = typeOfVariable.getBaseType();
		if(baseType != null && DotNetTypes.System.Enum.equals(baseType.getFullName()))
		{
			return AllIcons.Nodes.Enum;
		}

		return AllIcons.Debugger.Value;
	}

	@Nullable
	public abstract DotNetValueProxy getValueOfVariableImpl();

	public abstract void setValueForVariableImpl(@NotNull DotNetValueProxy value);

	@Nullable
	public DotNetValueProxy getValueOfVariable()
	{
		try
		{
			return getValueOfVariableImpl();
		}
		catch(Throwable e)
		{
			DotNetValueProxy proxy = null;
			if(e instanceof DotNetThrowValueException)
			{
				proxy = ((DotNetThrowValueException) e).getThrowExceptionValue();
			}
			return new DotNetErrorValueProxyImpl(e, proxy);
		}
	}

	@Nullable
	public DotNetTypeProxy getTypeOfVariableValue(@Nullable DotNetValueProxy alreadyCalledValue)
	{
		DotNetValueProxy valueOfVariable = alreadyCalledValue != null ? alreadyCalledValue : getValueOfVariable();
		if(valueOfVariable == null)
		{
			return getTypeOfVariable();
		}
		try
		{
			return valueOfVariable.getType();
		}
		catch(Exception e)
		{
			return getTypeOfVariable();
		}
	}

	public void setValueForVariable(@NotNull DotNetValueProxy value)
	{
		invoke(o ->
		{
			setValueForVariableImpl(o);
			return null;
		}, value);
	}

	@Nullable
	protected <P, R> R invoke(NullableFunction<P, R> func, P param)
	{
		try
		{
			return func.fun(param);
		}
		catch(Exception e)
		{
			// ignore all
		}
		return null;
	}

	@Nullable
	@Override
	public XValueModifier getModifier()
	{
		if(typeTag(null) != null)
		{
			return myValueModifier;
		}
		return null;
	}

	@Override
	public void computeChildren(@NotNull XCompositeNode node)
	{
		myDebugContext.invoke(() ->
		{
			DotNetValueProxy value = getValueOfVariable();
			if(value instanceof DotNetErrorValueProxy)
			{
				DotNetValueProxy throwObject = ((DotNetErrorValueProxy) value).getThrowObject();

				if(throwObject == null)
				{
					node.setErrorMessage("No children for error value");
					return;
				}
				else
				{
					XValueChildrenList list = new XValueChildrenList(1);
					list.add(new DotNetThrowValueNode(myDebugContext, myFrameProxy, throwObject));
					node.addChildren(list, true);
					return;
				}
			}

			DotNetTypeProxy typeOfVariable = getTypeOfVariableValue(null);

			if(typeOfVariable == null)
			{
				node.setErrorMessage("Variable type is not resolved");
				return;
			}

			DotNetLogicValueView valueView = null;
			for(DotNetLogicValueView temp : DotNetLogicValueView.IMPL)
			{
				if(temp.canHandle(myDebugContext, typeOfVariable))
				{
					valueView = temp;
					break;
				}
			}

			assert valueView != null : "Required default implementation";

			valueView.computeChildren(myDataHolder, myDebugContext, this, myFrameProxy, value, node);
		});
	}

	private boolean canHaveChildren()
	{
		TypeTag typeTag = typeTag(null);
		return typeTag == null || typeTag == TypeTag.String;
	}

	@Override
	public void computePresentation(@NotNull XValueNode xValueNode, @NotNull XValuePlace xValuePlace)
	{
		computePresentationImpl(xValueNode, xValuePlace);
	}

	protected void computePresentationImpl(@NotNull XValueNode xValueNode, @NotNull XValuePlace xValuePlace)
	{
		final DotNetValueProxy valueOfVariable = getValueOfVariable();

		xValueNode.setPresentation(getIconForVariable(null), new DotNetValuePresentation(myDebugContext, myFrameProxy, valueOfVariable), canHaveChildren());
	}
}

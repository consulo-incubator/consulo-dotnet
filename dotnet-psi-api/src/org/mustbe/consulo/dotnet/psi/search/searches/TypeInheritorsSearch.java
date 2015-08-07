/*
 * Copyright 2000-2009 JetBrains s.r.o.
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
package org.mustbe.consulo.dotnet.psi.search.searches;

import gnu.trove.THashSet;

import java.util.Set;

import org.consulo.lombok.annotations.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mustbe.consulo.RequiredReadAction;
import org.mustbe.consulo.dotnet.DotNetTypes;
import org.mustbe.consulo.dotnet.psi.DotNetModifier;
import org.mustbe.consulo.dotnet.psi.DotNetTypeDeclaration;
import org.mustbe.consulo.dotnet.resolve.DotNetPsiSearcher;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Conditions;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiBundle;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiSearchScopeUtil;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ExtensibleQueryFactory;
import com.intellij.util.EmptyQuery;
import com.intellij.util.Function;
import com.intellij.util.Processor;
import com.intellij.util.Query;
import com.intellij.util.QueryExecutor;
import com.intellij.util.containers.Stack;

/**
 * @author VISTALL
 *         <p/>
 *         Inspired by Jetbrains from java-impl (com.intellij.psi.search.searches.ClassInheritorsSearch) by max
 */
@Logger
public class TypeInheritorsSearch extends ExtensibleQueryFactory<DotNetTypeDeclaration, TypeInheritorsSearch.SearchParameters>
{
	public static final TypeInheritorsSearch INSTANCE = new TypeInheritorsSearch();

	static
	{
		INSTANCE.registerExecutor(new QueryExecutor<DotNetTypeDeclaration, SearchParameters>()
		{
			@Override
			public boolean execute(@NotNull final SearchParameters parameters, @NotNull final Processor<DotNetTypeDeclaration> consumer)
			{
				final String baseVmQName = parameters.getVmQName();
				final SearchScope searchScope = parameters.getScope();

				TypeInheritorsSearch.LOGGER.assertTrue(searchScope != null);

				ProgressIndicator progress = ProgressIndicatorProvider.getGlobalProgressIndicator();
				if(progress != null)
				{
					progress.pushState();
					progress.setText(PsiBundle.message("psi.search.inheritors.of.class.progress", baseVmQName));
				}

				boolean result = processInheritors(consumer, baseVmQName, searchScope, parameters);

				if(progress != null)
				{
					progress.popState();
				}

				return result;
			}
		});
	}

	public static class SearchParameters
	{
		private final Project myProject;
		private final String myVmQName;
		private final SearchScope myScope;
		private final boolean myCheckDeep;
		private final boolean myCheckInheritance;
		private final Function<DotNetTypeDeclaration, DotNetTypeDeclaration> myTransformer;
		private final Condition<String> myNameCondition;

		public SearchParameters(Project project,
				@NotNull final String aClassQName,
				@NotNull SearchScope scope,
				final boolean checkDeep,
				final boolean checkInheritance,
				Function<DotNetTypeDeclaration, DotNetTypeDeclaration> transformer)
		{
			this(project, aClassQName, scope, checkDeep, checkInheritance, Conditions.<String>alwaysTrue(), transformer);
		}

		public SearchParameters(@NotNull Project project,
				@NotNull final String aClassQName,
				@NotNull SearchScope scope,
				final boolean checkDeep,
				final boolean checkInheritance,
				@NotNull final Condition<String> nameCondition,
				Function<DotNetTypeDeclaration, DotNetTypeDeclaration> transformer)
		{
			myProject = project;
			myVmQName = aClassQName;
			myScope = scope;
			myCheckDeep = checkDeep;
			myCheckInheritance = checkInheritance;
			myNameCondition = nameCondition;
			myTransformer = transformer;
		}

		@NotNull
		public String getVmQName()
		{
			return myVmQName;
		}

		@NotNull
		public Condition<String> getNameCondition()
		{
			return myNameCondition;
		}

		public boolean isCheckDeep()
		{
			return myCheckDeep;
		}

		public SearchScope getScope()
		{
			return myScope;
		}

		public boolean isCheckInheritance()
		{
			return myCheckInheritance;
		}

		public Project getProject()
		{
			return myProject;
		}
	}

	private TypeInheritorsSearch()
	{
		super("org.mustbe.consulo.dotnet.core");
	}

	@NotNull
	public static Query<DotNetTypeDeclaration> search(@NotNull final DotNetTypeDeclaration typeDeclaration,
			@NotNull SearchScope scope,
			final boolean checkDeep,
			final boolean checkInheritance,
			Function<DotNetTypeDeclaration, DotNetTypeDeclaration> transformer)
	{
		String vmQName = ApplicationManager.getApplication().runReadAction(new Computable<String>()
		{
			@Override
			public String compute()
			{
				if(typeDeclaration.hasModifier(DotNetModifier.SEALED))
				{
					return null;
				}
				return typeDeclaration.getVmQName();
			}
		});
		if(vmQName == null)
		{
			return EmptyQuery.getEmptyQuery();
		}
		return search(new SearchParameters(typeDeclaration.getProject(), vmQName, scope, checkDeep, checkInheritance, transformer));
	}

	public static Query<DotNetTypeDeclaration> search(@NotNull SearchParameters parameters)
	{
		return INSTANCE.createQuery(parameters);
	}

	@NotNull
	public static Query<DotNetTypeDeclaration> search(@NotNull final DotNetTypeDeclaration typeDeclaration,
			@NotNull SearchScope scope,
			final boolean checkDeep,
			Function<DotNetTypeDeclaration, DotNetTypeDeclaration> transformer)
	{
		return search(typeDeclaration, scope, checkDeep, true, transformer);
	}

	@NotNull
	public static Query<DotNetTypeDeclaration> search(@NotNull final DotNetTypeDeclaration typeDeclaration, final boolean checkDeep)
	{
		return search(typeDeclaration, typeDeclaration.getUseScope(), checkDeep, DotNetPsiSearcher.DEFAULT_TRANSFORMER);
	}

	@NotNull
	public static Query<DotNetTypeDeclaration> search(@NotNull final DotNetTypeDeclaration typeDeclaration,
			final boolean checkDeep,
			@NotNull Function<DotNetTypeDeclaration, DotNetTypeDeclaration> transformer)
	{
		return search(typeDeclaration, typeDeclaration.getUseScope(), checkDeep, transformer);
	}

	@NotNull
	public static Query<DotNetTypeDeclaration> search(@NotNull DotNetTypeDeclaration typeDeclaration,
			@NotNull Function<DotNetTypeDeclaration, DotNetTypeDeclaration> transformer)
	{
		return search(typeDeclaration, true, transformer);
	}

	@RequiredReadAction
	private static boolean processInheritors(@NotNull final Processor<DotNetTypeDeclaration> consumer,
			@NotNull final String baseVmQName,
			@NotNull final SearchScope searchScope,
			@NotNull final SearchParameters parameters)
	{

		if(DotNetTypes.System.Object.equals(baseVmQName))
		{
			return AllTypesSearch.search(searchScope, parameters.getProject(), parameters.getNameCondition()).forEach(new
																																Processor<DotNetTypeDeclaration>()
			{
				@Override
				public boolean process(final DotNetTypeDeclaration aClass)
				{
					ProgressIndicatorProvider.checkCanceled();
					final String qname1 = ApplicationManager.getApplication().runReadAction(new Computable<String>()
					{
						@Override
						@Nullable
						public String compute()
						{
							return aClass.getVmQName();
						}
					});
					return DotNetTypes.System.Object.equals(qname1) || consumer.process(parameters.myTransformer.fun(aClass));
				}
			});
		}

		final Ref<String> currentBase = Ref.create(null);
		final Stack<String> stack = new Stack<String>();
		// there are two sets for memory optimization: it's cheaper to hold FQN than PsiClass
		final Set<String> processedFqns = new THashSet<String>(); // FQN of processed classes if the class has one

		final Processor<DotNetTypeDeclaration> processor = new Processor<DotNetTypeDeclaration>()
		{
			@Override
			public boolean process(final DotNetTypeDeclaration candidate)
			{
				ProgressIndicatorProvider.checkCanceled();

				final Ref<Boolean> result = new Ref<Boolean>();
				final Ref<String> vmQNameRef = new Ref<String>();
				ApplicationManager.getApplication().runReadAction(new Runnable()
				{
					@Override
					public void run()
					{
						vmQNameRef.set(candidate.getVmQName());
						if(parameters.isCheckInheritance() || parameters.isCheckDeep())
						{
							if(!candidate.isInheritor(currentBase.get(), false))
							{
								result.set(true);
								return;
							}
						}

						if(PsiSearchScopeUtil.isInScope(searchScope, candidate))
						{
							final String name = candidate.getName();
							if(name != null && parameters.getNameCondition().value(name) && !consumer.process(parameters.myTransformer.fun
									(candidate)))
							{
								result.set(false);
							}
						}
					}
				});
				if(!result.isNull())
				{
					return result.get();
				}

				if(parameters.isCheckDeep() && !isSealed(candidate))
				{
					stack.push(vmQNameRef.get());
				}

				return true;
			}
		};
		stack.push(baseVmQName);

		final GlobalSearchScope projectScope = GlobalSearchScope.allScope(parameters.getProject());
		while(!stack.isEmpty())
		{
			ProgressIndicatorProvider.checkCanceled();

			String vmQName = stack.pop();

			if(!processedFqns.add(vmQName))
			{
				continue;
			}

			currentBase.set(vmQName);
			if(!DirectTypeInheritorsSearch.search(parameters.getProject(), vmQName, projectScope, false).forEach(processor))
			{
				return false;
			}
		}
		return true;
	}

	private static boolean isSealed(@NotNull final DotNetTypeDeclaration baseClass)
	{
		return ApplicationManager.getApplication().runReadAction(new Computable<Boolean>()
		{
			@Override
			public Boolean compute()
			{
				return baseClass.hasModifier(DotNetModifier.SEALED);
			}
		});
	}
}
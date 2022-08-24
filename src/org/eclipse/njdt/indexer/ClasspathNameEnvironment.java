/*******************************************************************************
 * Copyright (c) 2022 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.njdt.indexer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jdt.internal.compiler.batch.FileSystem;
import org.eclipse.jdt.internal.compiler.env.IModule;
import org.eclipse.jdt.internal.compiler.env.IModuleAwareNameEnvironment;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;

/**
 * An abstract implementation of a parser {@link IModuleAwareNameEnvironment} based on a kind of 
 * "compile claspspath". Somewhat like {@link FileSystem}, but makes no assumptions about the 
 * {@link ClasspathEntry} instances it uses.
 * This class is not complete: in particular, modules are not being index at the moment.
 * @author Thomas Mäder
 *
 */
public class ClasspathNameEnvironment implements IModuleAwareNameEnvironment {
	
	private List<ClasspathEntry> entries;

	public ClasspathNameEnvironment(List<ClasspathEntry> entries) {
		this.entries= entries;
	}
	
	
	@Override
	public void cleanup() {
	}

	@Override
	public NameEnvironmentAnswer findType(char[][] compoundName, char[] moduleName) {
		return doFindType(moduleName, concatWith(compoundName, "/").toString());
	}

	private String concatWith(char[][] compoundName, String separator) {
		StringBuilder buf= new StringBuilder();
		for (int i = 0; i < compoundName.length; i++) {
			if (i > 0) {
				buf.append(separator);
			}
			buf.append(compoundName[i]);
		}
		return buf.toString();
	}

	private NameEnvironmentAnswer doFindType(char[] moduleName, String string) {
		return findInClassPathEntries(entry ->entry.findType(moduleName, string));
	}

	@Override
	public NameEnvironmentAnswer findType(char[] typeName, char[][] packageName, char[] moduleName) {
		StringBuilder buf= new StringBuilder();
		for (int i = 0; i < packageName.length; i++) {
			buf.append(packageName[i]);
			buf.append('/');
		}
		return doFindType(moduleName, buf.toString());
	}

	@Override
	public char[][] getModulesDeclaringPackage(char[][] packageName, char[] moduleName) {
		return toCharArray(findInClassPathEntries(entry ->entry.getModulesDeclaringPackage(concatWith(packageName, "/"), ModuleMatchingStrategy.fromModuleName(moduleName))));
	}
	
	private <T> T findInClassPathEntries(Function<ClasspathEntry, T> what) {
		for (ClasspathEntry entry : entries) {
			T answer= what.apply(entry);
			if (answer != null) {
				return answer;
			}
		}
		return null;
	}

	@Override
	public boolean hasCompilationUnit(char[][] qualifiedPackageName, char[] moduleName, boolean checkCUs) {
		Boolean hasCompilationUnit=  findInClassPathEntries(entry -> {
			if (entry.hasCompilationUnit(concatWith(qualifiedPackageName, "/"), moduleName, checkCUs)) {
				return Boolean.TRUE;
			}
			return (Boolean)null;
		});
		return hasCompilationUnit != null;
	}

	@Override
	public IModule getModule(char[] moduleName) {
		return findInClassPathEntries(entry -> entry.getModule(moduleName));
	}

	@Override
	public char[][] getAllAutomaticModules() {
		List<String> automaticModules= new ArrayList<>();
		for (ClasspathEntry entry : entries) {
			automaticModules.addAll(entry.getAutomaticModules());
		}
		return toCharArray(automaticModules.toArray(new String[automaticModules.size()]));
	}

	@Override
	public char[][] listPackages(char[] moduleName) {
		return toCharArray(findInClassPathEntries(entry -> entry.listPackages(moduleName)));
	}
	
	char[][] toCharArray(String[] source) {
		char[][] target = new char[source.length][];
		for (int i = 0; i < source.length; i++) {
			target[i]= source[i].toCharArray();
		}
		
		return target;
	}
}

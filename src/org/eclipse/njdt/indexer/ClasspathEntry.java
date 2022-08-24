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

import java.util.List;

import org.eclipse.jdt.internal.compiler.env.IModule;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;

/**
 * An entry on the classpath, typically something like a source folder or a jar, but may also be 
 * implemented differently, for example via a precomputed index.
 * @author Thomas Mäder
 *
 */
public interface ClasspathEntry {
	NameEnvironmentAnswer findType(char[] moduleName, String string);
	String[] getModulesDeclaringPackage(String binaryPackageName, ModuleMatchingStrategy fromModuleName);
	boolean hasCompilationUnit(String concatWith, char[] moduleName, boolean checkCUs);
	IModule getModule(char[] moduleName);
	String[] listPackages(char[] moduleName);
	List<String> getAutomaticModules();

}

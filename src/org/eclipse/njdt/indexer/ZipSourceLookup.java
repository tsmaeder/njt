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

import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;


/**
 * Source lookup based on a zip file. The structure of a zip entry is assumed to be 
 * <module_name>/path/to/package/MainTypeName.java".
 * This lookup strategy is probably not correct for inner types of non-public types. Feel free to fix.
 * 
 * @author Thomas Mäder
 *
 */
public class ZipSourceLookup implements SourceLookup {
	
	private Path path;
	private ZipFile openZipFile;

	ZipSourceLookup(Path path) {
		this.path= path;
	}

	@Override
	public void open() {
		try {
			this.openZipFile= new ZipFile(path.toFile());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() {
		if (this.openZipFile != null) {
			try {
				var file= this.openZipFile;
				this.openZipFile= null;
				file.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public ICompilationUnit lookup(String module, String binaryTypeName) {
		
		int innnertypeSeparator = binaryTypeName.indexOf('$');
		if (innnertypeSeparator >= 0) {
			binaryTypeName= binaryTypeName.substring(0, innnertypeSeparator);
		}
		String path= module + '/'+ binaryTypeName+".java";
		ZipEntry entry = openZipFile.getEntry(path);
		if (entry == null) {
			return null;
		}
		try {
			return new InputStreamCompilationUnit(binaryTypeName+".java", openZipFile.getInputStream(entry), (int)entry.getSize());
		} catch (IOException e) {
			throw new RuntimeException(e);	
		}
	}
	
}

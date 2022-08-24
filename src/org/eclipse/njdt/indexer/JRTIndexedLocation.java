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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.njdt.indexer.writer.DocumentAddress;
import org.eclipse.njdt.indexer.writer.IndexWriter;

/**
 * An indexed location for building the index over a JDK location 
 * @author Thomas Mäder
 *
 */
public class JRTIndexedLocation implements IndexedLocation {
	public static final String SCHEME = "jrt+file";
	private File javaHome;
	private FileSystem fs;
	private String indexUri;
	Map<CharSequence, Boolean> typeCache= new HashMap<>();
	private ClassFileSourceParser sourceParser;
	private ZipSourceLookup sourceLookup;
	
	private CompilationUnitDeclaration lastAst;
	private ICompilationUnit lastCompilationUnit;

	public JRTIndexedLocation(String javaHome, ClassFileSourceParser parser)
			throws IOException {
		this.javaHome = new File(javaHome);
		this.fs = FileSystems.newFileSystem(URI.create("jrt:/"), Collections.singletonMap("java.home", javaHome));
		String uri = this.javaHome.toURI().toString();
		this.indexUri= uri.replaceFirst("file", SCHEME);
		this.sourceParser= parser;
		this.sourceLookup= new ZipSourceLookup(this.javaHome.toPath().resolve("lib/src.zip"));
	}

	@Override
	public CharSequence lookupTypeId(CharSequence typeName) {
		Boolean exists = typeCache.get(typeName); 
		if (exists != null) {
			return Boolean.TRUE.equals(exists) ? typeName: null;
		}
		
		Pair<String, String> parsedName = parseSourceName(typeName.toString());	
		
		try (DirectoryStream<Path> modules = Files.newDirectoryStream(fs.getPath("modules"))) {
			for (Path module : modules) {
				Path path = module.resolve(parsedName.left()).resolve(parsedName.right().replace('.', '$') + ".class");
				if (Files.exists(path)) {
					typeCache.put(typeName, Boolean.TRUE);
					return typeName;
				}	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		typeCache.put(typeName, Boolean.FALSE);
		return null;
	}

	@Override
	public String getIndexUri() {
		return indexUri;
	}

	private Pair<String, String> parseSourceName(String fqn) {
		int lastSlashIndex = fqn.lastIndexOf('/');
		String pkg = fqn.substring(0, lastSlashIndex);
		String clazz = fqn.substring(lastSlashIndex+1);

		return new Pair<>(pkg, clazz);
	}

	@Override
	public void index(IndexWriter indexWriter) {
		ClassFileIndexer classFileIndexer = new ClassFileIndexer(indexWriter);
		sourceLookup.open();
		try {
			Files.walkFileTree(fs.getPath("modules"), new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (attrs.isRegularFile() && file.getFileName().toString().endsWith(".class")) {
						String t= file.subpath(2, file.getNameCount()).toString();
						String typeName= t.substring(0, t.length()-6);
						String module= file.subpath(1, 2).toString();

						try (InputStream stream = Files.newInputStream(file, StandardOpenOption.READ)) {
							DocumentAddress documentAddress = new DocumentAddress(getIndexUri(), file.toString());

							
							Supplier<CompilationUnitDeclaration> astSource= () -> {
								ICompilationUnit source = sourceLookup.lookup(module, typeName);
								
								if (source != null) {
									if (!source.equals(lastCompilationUnit)) {
										lastCompilationUnit= source;
										lastAst= (CompilationUnitDeclaration) sourceParser.parse(source);
									} 
									return lastAst;
								}
								return null;
							};
							classFileIndexer.indexClassFile(documentAddress,
									stream, module, astSource);
						}
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			throw new RuntimeException("Unexpected Error indexing file", e);
		} finally {
			sourceLookup.close();
		}
	}
}

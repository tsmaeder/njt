package org.eclipse.njdt.indexer;

import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;

public interface SourceLookup {
	void open();
	void close();
	ICompilationUnit lookup(String module, String binaryTypeName);
}

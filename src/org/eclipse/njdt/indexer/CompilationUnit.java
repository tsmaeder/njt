package org.eclipse.njdt.indexer;

import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;

public abstract class CompilationUnit implements ICompilationUnit {
	private char[][] packageName;
	private String fileName;
	public CompilationUnit(char[][] packageName, String fileName) {
		this.packageName= packageName;
		this.fileName = fileName;
	}
	
	@Override
	public char[] getFileName() {
		return fileName.toCharArray();
	}

	@Override
	public abstract char[] getContents();

	@Override
	public char[] getMainTypeName() {
		return fileName.substring(fileName.length()-5).toCharArray();
	}

	@Override
	public char[][] getPackageName() {
		return packageName;
	}

}

package org.eclipse.njdt.indexer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;

public class InputStreamCompilationUnit implements ICompilationUnit {
	private char[] contents;
	private String fileName;
	private char[][] packageName;

	public InputStreamCompilationUnit(String path, InputStream inputStream, int size) {
		this.contents = readFully(inputStream, size);
		int lastSlash = path.lastIndexOf('/');
		String[] packageParts = lastSlash >= 0 ? path.substring(0, lastSlash).split("/") : new String[0];
		this.packageName= new char[packageParts.length][];
		for (int i = 0; i < packageParts.length; i++) {
			this.packageName[i]= packageParts[i].toCharArray();
		}
		this.fileName= lastSlash >= 0 ? path.substring(lastSlash+1): path;
	}

	private char[] readFully(InputStream inputStream, int size) {
		byte[] buf = new byte[8192];
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(size);
		try {
			int bytesRead;
			bytesRead = inputStream.read(buf);
			while (bytesRead > 0) {
				byteArrayOutputStream.write(buf, 0, bytesRead);
				bytesRead = inputStream.read(buf);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return byteArrayOutputStream.toString().toCharArray();
	}

	@Override
	public char[] getFileName() {
		return fileName.toCharArray();
	}

	@Override
	public char[] getContents() {
		return contents;
	}

	@Override
	public char[] getMainTypeName() {
		return fileName.substring(fileName.length()-5).toCharArray();
	}

	@Override
	public char[][] getPackageName() {
		return packageName;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || o.getClass() != getClass()) {
			return false;
		}
		
		InputStreamCompilationUnit that= (InputStreamCompilationUnit) o;
		return fileName.equals(that.fileName) && Arrays.deepEquals(packageName, that.packageName);
	}
	
	@Override
	public int hashCode() {
		return fileName.hashCode();
	}
}

package org.eclipse.njdt.indexer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;

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

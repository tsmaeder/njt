package org.eclipse.njdt.indexer;

import java.lang.reflect.Proxy;
import java.util.Collections;

import org.eclipse.njdt.indexer.writer.IndexWriter;

public class IndexJrt {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("usage: IndexJrt <java.home>");
			System.exit(-1);
		}
		
		long t0= System.currentTimeMillis();
		
		JRTIndexedLocation jrtIndexedLocation = new JRTIndexedLocation(args[0]);
		IndexWriter indexWriter = (IndexWriter) Proxy.newProxyInstance(IndexJrt.class.getClassLoader(), new Class[] { IndexWriter.class }, new LoggingProxy(new NullIndexWriter()));
		jrtIndexedLocation.index(indexWriter, new DefaultMonikerFactory(Collections.singletonList(jrtIndexedLocation)));
	
		long t1= System.currentTimeMillis();
		System.out.println("indexing took "+(t1-t0)+"ms");
	}

}

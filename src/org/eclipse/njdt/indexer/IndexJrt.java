package org.eclipse.njdt.indexer;

import java.util.Collections;

import org.eclipse.njdt.indexer.db.DBIndexWriter;
import org.eclipse.njdt.indexer.db.IndexDb;

public class IndexJrt {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.println("usage: IndexJrt <java.home> <db password>");
			System.exit(-1);
		}

		long t0 = System.currentTimeMillis();

		JRTIndexedLocation jrtIndexedLocation = new JRTIndexedLocation(args[0]);
		IndexDb indexDb = new IndexDb("localhost/njtindex", "njt", args[1]);
		try {
			DBIndexWriter indexWriter = new DBIndexWriter(Collections.singletonList(jrtIndexedLocation), indexDb);

			indexWriter.connect();
			try {
				jrtIndexedLocation.index(indexWriter);
			} finally {
				indexWriter.close();
			}
		} finally {
			indexDb.close();
		}

		long t1 = System.currentTimeMillis();
		System.out.println("indexing took " + (t1 - t0) + "ms");
	}

}

package org.eclipse.njdt.indexer;

public interface IndexEntries {

}


class Location {
	String locationUri;
	int start;
	int length;
}

class TypeReference {
	String name;
	Location location;
	
	String locationURI; // locationUri= (local | remote:IndexId)path/to/document
	boolean implicitReference;
	String referenceKind; // declaration, cast, extends, implements
}

class MethodReference {
	String name;
	String typeName;
	String locationUri;
	boolean implicit;
	String referenceKind;
}

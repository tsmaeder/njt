package org.eclipse.njdt.indexer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.njdt.indexer.writer.DocumentAddress;

public class DefaultMonikerFactory implements MonikerFactory {
	private List<IndexedLocation> indexedLocations;

	public DefaultMonikerFactory(List<IndexedLocation> locations) {
		this.indexedLocations= new ArrayList<IndexedLocation>(locations);
	}
	
	@Override
	public CharSequence createTypeMoniker(DocumentAddress referenceFrom, CharSequence typeName) {
		if (typeName.toString().indexOf('/') == -1) {
			// a simple name, hence a primitive type
			return typeName;
		}
		for (IndexedLocation indexedLocation : indexedLocations) {
			CharSequence localId=  indexedLocation.lookupTypeId(typeName);
			if (localId != null) {
				String indexId = indexedLocation.getIndexUri();
				if (indexId.equals(referenceFrom.indexId())) {
					return "local/"+localId;
				}
				return indexId  + "/" + localId;
			}
		}
		return "unresolved:/"+typeName;
	}

}

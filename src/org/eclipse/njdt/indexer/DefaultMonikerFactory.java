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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.njdt.indexer.writer.DocumentAddress;

/**
 * Simple factory for monikers. creates either a "local:/<type id>" or  a 
 * "<index id>:/<type id>" moniker
 * @author Thomas Mäder
 *
 */
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
					return "local:/"+localId;
				}
				return indexId  + "/" + localId;
			}
		}
		return "unresolved:/"+typeName;
	}

}

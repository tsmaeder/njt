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
package org.eclipse.njdt.indexer.query;

import java.util.List;

import org.eclipse.njdt.indexer.query.expression.Expression;

public interface IndexReader {
	<T extends Reference<?>> List<T> queryDeclarations(Class<T> what, Expression<Boolean> where);
	<T extends Reference<?>> List<T> queryReferences(Class<T> what, Expression<Boolean> where);
}

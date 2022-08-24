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
/**
 * This package defines a simple expression language for expressing 
 * conditions on various entities in the index. A concrete index implementation
 * must transform these expressions to be used locally, for example by building
 * a sql statement from the expressions
 */
package org.eclipse.njdt.indexer.query.expression;
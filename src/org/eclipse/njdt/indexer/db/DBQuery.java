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
package org.eclipse.njdt.indexer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.TypeReference;
import org.eclipse.njdt.indexer.FieldReferenceKind;
import org.eclipse.njdt.indexer.MethodReferenceKind;
import org.eclipse.njdt.indexer.TypeReferenceKind;
import org.eclipse.njdt.indexer.query.Declaration;
import org.eclipse.njdt.indexer.query.MethodReference;
import org.eclipse.njdt.indexer.query.Reference;
import org.eclipse.njdt.indexer.query.expression.ArithmeticOperation;
import org.eclipse.njdt.indexer.query.expression.BinaryOperation;
import org.eclipse.njdt.indexer.query.expression.BooleanOperation;
import org.eclipse.njdt.indexer.query.expression.ComparableOperation;
import org.eclipse.njdt.indexer.query.expression.Expression;
import org.eclipse.njdt.indexer.query.expression.ExpressionVisitor;
import org.eclipse.njdt.indexer.query.expression.Literal;
import org.eclipse.njdt.indexer.query.expression.Parameter;
import org.eclipse.njdt.indexer.query.expression.Property;
import org.eclipse.njdt.indexer.query.expression.StringPredicate;

class ExpressionRenderer {
	private static final String[] SQL_ARITHEMETIC_OPS= new String[] {
			"+",
			"-",
			"*",
			"/"	
	};
	
	private static final String[] SQL_BOOLEAN_OPS= new String[] {
			"AND",
			"OR"
	};
	
	private static final String[] SQL_STRING_OPS= new String[] {
			"LIKE"
	};

	private static final String[] SQL_COMPARISON_OPS= new String[] {
			"<",
			"=",
			">"
	};

	
	private Expression<Boolean> where;

	private Map<String, Integer> parameters;
	
	public ExpressionRenderer(Expression<Boolean> where) {
		this.where= where;
		this.parameters= new HashMap<>();
	}
	
	public int getParameterIndex(String name) {
		return 0;
	}

	public String render() {
		StringBuilder builder= new StringBuilder();
		
		where.accept(new ExpressionVisitor<Void>() {
			@Override
			public Void visit(ArithmeticOperation e) {
				renderBinaryOp(builder, e, SQL_ARITHEMETIC_OPS[e.getOperator().ordinal()]);
				return null;
			}

			private void renderBinaryOp(StringBuilder builder, BinaryOperation<?, ?, ?, ?> e, String operator) {
				builder.append("(");
				e.getLeft().accept(this);
				builder.append(" ").append(operator).append("");
				e.getRight().accept(this);
				builder.append(")");
			}
			
			@Override
			public Void visit(BooleanOperation e) {
				renderBinaryOp(builder, e, SQL_BOOLEAN_OPS[e.getOperator().ordinal()]);
				return null;
			}
			
			@Override
			public Void visit(ComparableOperation<?> e) {
				renderBinaryOp(builder, e, SQL_COMPARISON_OPS[e.getOperator().ordinal()]);
				return null;
			}
			
			@Override
			public Void visit(Literal<?> e) {
				if (CharSequence.class.isAssignableFrom(e.getClass())) {
					builder.append("'").append(String.valueOf(e.getValue())).append("'");
				} else {
					builder.append(String.valueOf(e.getValue()));
				}
				return null;
			}
			
			@Override
			public Void visit(Parameter<?> e) {
				parameters.put(e.getName(), parameters.size()+1);
				builder.append("?");
				return null;
			}
			
			@Override
			public Void visit(Property<?> e) {
				builder.append(e.getName());
				return null;
			}
			
			public Void visit(StringPredicate e) {
				renderBinaryOp(builder, e, SQL_STRING_OPS[e.getOperator().ordinal()]);
				return null;
			};
			
		});
		return builder.toString();
	}
}

public class DBQuery {
	private Class<?> what;
	private ExpressionRenderer renderer;

	public DBQuery(Class<?> what, Expression<Boolean> where) {
		this.what= what;
		this.renderer= new ExpressionRenderer(where);
	}
	
	PreparedStatement createStatement(Connection c) throws SQLException {
		StringBuilder sql= new StringBuilder("SELECT index_id, document_id, modifiers, type_name, name, signature, source_start, source_length FROM ");
		if (Reference.class.isAssignableFrom(what)) {
			sql.append("refs WHERE ");
			if (what == FieldReference.class) {
				sql.append(FieldReferenceKind.MIN).append(" <= ").append(" reference_kind and  reference_kind <= ").append(FieldReferenceKind.MAX);
			} else if (what == MethodReference.class) {
				sql.append(MethodReferenceKind.MIN).append(" <= ").append(" reference_kind and  reference_kind <= ").append(MethodReferenceKind.MAX);
			} else if (what == TypeReference.class) {
				sql.append(TypeReferenceKind.MIN).append(" <= ").append(" reference_kind and  reference_kind <= ").append(TypeReferenceKind.MAX);
			};
		} else if (Declaration.class.isAssignableFrom(what)) {
			sql.append("declarations WHERE");
		}
		
		sql.append("(").append(renderer.render()).append(")");
		return c.prepareStatement(sql.toString());
	}
	
	void bindParameters(PreparedStatement statement, Map<String, Object> values) {
		values.forEach((String name, Object value) -> {
			try {
				statement.setObject(renderer.getParameterIndex(name), value);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		});
	}
}

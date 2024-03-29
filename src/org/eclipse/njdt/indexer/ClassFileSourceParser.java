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

import org.eclipse.jdt.internal.compiler.ASTVisitor;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.IModuleAwareNameEnvironment;
import org.eclipse.jdt.internal.compiler.env.ISourceType;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.impl.ITypeRequestor;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.CompilationUnitScope;
import org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment;
import org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;

public class ClassFileSourceParser {
	private IModuleAwareNameEnvironment nameEnvironment;

	ClassFileSourceParser(IModuleAwareNameEnvironment nameEnvironment) {
		this.nameEnvironment= nameEnvironment;
	}
	
	ASTNode parse(ICompilationUnit sourceUnit) {
		CompilationResult unitResult =
				new CompilationResult(sourceUnit, 1, 1, 20);
		
		ProblemReporter problemReporter = new ProblemReporter(DefaultErrorHandlingPolicies.proceedWithAllProblems(), new CompilerOptions(), new DefaultProblemFactory());
		
		Parser parser = new Parser(problemReporter, false);
		CompilationUnitDeclaration ast = parser.parse(sourceUnit, unitResult);
		
		LookupEnvironment lookupEnvironment = new LookupEnvironment(new ITypeRequestor() {
			
			@Override
			public void accept(ISourceType[] sourceType, PackageBinding packageBinding, AccessRestriction accessRestriction) {
				
			}
			
			@Override
			public void accept(ICompilationUnit newUnit, AccessRestriction accessRestriction) {	
			}
			
			@Override
			public void accept(IBinaryType binaryType, PackageBinding packageBinding, AccessRestriction accessRestriction) {
				LookupEnvironment env = packageBinding.environment;
				env.createBinaryTypeFrom(binaryType, packageBinding, accessRestriction);
			}
		}, new CompilerOptions(), problemReporter, nameEnvironment);
		
		lookupEnvironment.buildTypeBindings(ast, null);
		lookupEnvironment.completeTypeBindings();
		return ast;
	}
	
	public static Range lookupClassDeclaration(CompilationUnitDeclaration cuNode, String binaryName) {
		ASTNode node= lookupClassDeclarationNode(cuNode, binaryName);
		return node == null ? null : new Range(node.sourceStart, node.sourceEnd-node.sourceStart);
	}
	
	private static ASTNode lookupClassDeclarationNode(CompilationUnitDeclaration root, String binaryName) {
		if (root == null) {
			return null;
		}
		class Visitor extends ASTVisitor {
			ASTNode foundNode= null;;
			
			@Override
			public boolean visit(TypeDeclaration localTypeDeclaration, BlockScope scope) {
				return matchClassName(localTypeDeclaration);
			}

			private boolean matchClassName(TypeDeclaration localTypeDeclaration) {
				if (localTypeDeclaration.binding != null && new String(localTypeDeclaration.binding.constantPoolName()).equals(binaryName)) {
					foundNode= localTypeDeclaration;
				}
				return foundNode == null;
			}
			
			@Override
			public boolean visit(TypeDeclaration memberTypeDeclaration, ClassScope scope) {
				return matchClassName(memberTypeDeclaration);
			}
			@Override
			public boolean visit(TypeDeclaration typeDeclaration, CompilationUnitScope scope) {
				return matchClassName(typeDeclaration);
			}
		}
		
		Visitor v= new Visitor();
		root.traverse(v, null, false);
		return v.foundNode;
	}
}

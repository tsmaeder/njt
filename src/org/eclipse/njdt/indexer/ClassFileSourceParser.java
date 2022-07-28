package org.eclipse.njdt.indexer;

import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.IModuleAwareNameEnvironment;
import org.eclipse.jdt.internal.compiler.env.ISourceType;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.impl.ITypeRequestor;
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
}

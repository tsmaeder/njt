package org.eclipse.njdt.indexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.env.ISourceType;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.impl.ITypeRequestor;
import org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment;
import org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;

public class Indexer {
	public static void main(String[] args) {
		Path sourceRoot = Path.of("C:\\Users\\thomas\\workspaces\\jdt\\ECompiler\\compiler");
		Path file= Path.of("org\\eclipse\\jdt\\internal\\compiler\\ast\\Assignment.java");
		new Indexer().index(sourceRoot, file);
	}

	private void index(Path sourceRoot, Path relativePath) {
		
		char[][] packageName= new char[relativePath.getNameCount()-1][];
		for (int i = 0; i < relativePath.getNameCount()-1; i++) {
			packageName[i]= relativePath.getName(i).toString().toCharArray();
		}
		
		String fileName= relativePath.getName(relativePath.getNameCount()-1).toString();
		ICompilationUnit sourceUnit = new ICompilationUnit() {
			
			@Override
			public char[] getFileName() {
				return packageName[packageName.length-1];
			}
			
			@Override
			public char[][] getPackageName() {
				return packageName;
			}
			
			@Override
			public char[] getMainTypeName() {
				return fileName.substring(fileName.length()-5).toCharArray();
			}
			
			@Override
			public char[] getContents() {
				try {
					return Files.readString(sourceRoot.resolve(relativePath)).toCharArray();
				} catch (IOException e) {
					return new char[0];
				}
			}
		};
		CompilationResult unitResult =
				new CompilationResult(sourceUnit, 1, 1, 20);
		
		ProblemReporter problemReporter = new ProblemReporter(DefaultErrorHandlingPolicies.proceedWithAllProblems(), new CompilerOptions(), new DefaultProblemFactory());
		
		CompilationUnitDeclaration ast = new Parser(problemReporter, false).parse(sourceUnit, unitResult);
		
		LookupEnvironment lookupEnvironment = new LookupEnvironment(new ITypeRequestor() {
			
			@Override
			public void accept(ISourceType[] sourceType, PackageBinding packageBinding, AccessRestriction accessRestriction) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void accept(ICompilationUnit unit, AccessRestriction accessRestriction) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void accept(IBinaryType binaryType, PackageBinding packageBinding, AccessRestriction accessRestriction) {
				// TODO Auto-generated method stub
				
			}
		}, new CompilerOptions(), problemReporter, new INameEnvironment() {
			
			@Override
			public boolean isPackage(char[][] parentPackageName, char[] packageName) {
				return 
			}
			
			@Override
			public NameEnvironmentAnswer findType(char[] typeName, char[][] packageName) {
				return null;
			}
			
			@Override
			public NameEnvironmentAnswer findType(char[][] compoundTypeName) {
				return null;
			}
			
			@Override
			public void cleanup() {
				
			}
		});
		
		lookupEnvironment.buildTypeBindings(ast, null);
		lookupEnvironment.completeTypeBindings();
		ast.resolve();
		
		System.out.println("parsed "+new String(ast.getFileName()));
	}
}

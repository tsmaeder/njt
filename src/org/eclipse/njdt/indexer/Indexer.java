package org.eclipse.njdt.indexer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.batch.ClasspathDirectory;
import org.eclipse.jdt.internal.compiler.batch.ClasspathJrt;
import org.eclipse.jdt.internal.compiler.batch.FileSystem;
import org.eclipse.jdt.internal.compiler.batch.FileSystem.Classpath;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import org.eclipse.jdt.internal.compiler.env.AccessRule;
import org.eclipse.jdt.internal.compiler.env.AccessRuleSet;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.env.ISourceType;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.impl.ITypeRequestor;
import org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.problem.AbortCompilationUnit;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.util.Messages;

public class Indexer {
	public static final String JAVA_HOME= "C:\\Users\\thomas\\Software\\jdk-18.0.1.1";
	
	public static void main(String[] args) {
		Path sourceRoot = Path.of("C:\\Users\\thomas\\workspaces\\jdt\\njt\\compiler");
		Path file= Path.of("org\\eclipse\\jdt\\internal\\compiler\\ast\\Assignment.java");
		new Indexer().index(sourceRoot, file);
	}

	private int totalUnits;
	private LookupEnvironment lookupEnvironment;
	
	private void index(Path sourceRoot, Path relativePath) {
		AccessRuleSet accessRuleSet = new AccessRuleSet(new AccessRule[0], AccessRestriction.COMMAND_LINE, "jdk");
		ClasspathJrt jdk = new ClasspathJrt(new File(JAVA_HOME), false, accessRuleSet, null);
		Classpath dir= FileSystem.getClasspath(sourceRoot.toString(), System.getProperty("file.encoding"), accessRuleSet);
		
		FileSystem fileSystem = new FileSystem(new FileSystem.Classpath[] {jdk, dir }, new String[] { sourceRoot.resolve(relativePath).toString() }, true);
		
		
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
		
		Parser parser = new Parser(problemReporter, false);
		CompilationUnitDeclaration ast = parser.parse(sourceUnit, unitResult);
		
		this.lookupEnvironment = new LookupEnvironment(new ITypeRequestor() {
			
			@Override
			public void accept(ISourceType[] sourceType, PackageBinding packageBinding, AccessRestriction accessRestriction) {
				
			}
			
			@Override
			public void accept(ICompilationUnit newUnit, AccessRestriction accessRestriction) {
				// Switch the current policy and compilation result for this unit to the requested one.
				CompilationResult unitResult =
					new CompilationResult(newUnit, Indexer.this.totalUnits++, Indexer.this.totalUnits, 10);
				unitResult.checkSecondaryTypes = true;
				try {
					CompilationUnitDeclaration parsedUnit;
					parsedUnit = parser.parse(newUnit, unitResult);
					// initial type binding creation
					lookupEnvironment.buildTypeBindings(parsedUnit, accessRestriction);

					// binding resolution
					lookupEnvironment.completeTypeBindings(parsedUnit);
				} catch (AbortCompilationUnit e) {
					System.out.println("whoops");
				}			
			}
			
			@Override
			public void accept(IBinaryType binaryType, PackageBinding packageBinding, AccessRestriction accessRestriction) {
				LookupEnvironment env = packageBinding.environment;
				env.createBinaryTypeFrom(binaryType, packageBinding, accessRestriction);
			}
		}, new CompilerOptions(), problemReporter, fileSystem);
		
		lookupEnvironment.buildTypeBindings(ast, null);
		lookupEnvironment.completeTypeBindings();

		System.out.println("parsed "+new String(ast.getFileName()));
	}
}

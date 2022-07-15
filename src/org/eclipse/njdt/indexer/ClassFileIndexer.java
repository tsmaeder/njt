package org.eclipse.njdt.indexer;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.njdt.indexer.writer.DocumentAddress;
import org.eclipse.njdt.indexer.writer.IndexWriter;
import org.eclipse.njdt.indexer.writer.MethodReferenceKind;
import org.eclipse.njdt.indexer.writer.TypeReferenceKind;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassFileIndexer {
	private IndexWriter indexWriter;
	private MonikerFactory monikerFactory;
	private char[] buffer=  new char[1 << 16];

	public ClassFileIndexer(IndexWriter indexWriter, MonikerFactory monikerFactory) {
		this.indexWriter = indexWriter;
		this.monikerFactory = monikerFactory;
	}

	boolean indexClassFile(DocumentAddress address, InputStream bytes) {
		Object session = indexWriter.beginIndexing(address);
		try {

			ClassReader parser = new ClassReader(bytes);
			ClassNode clazz = new ClassNode();
			parser.accept(clazz, ClassReader.SKIP_CODE);
			CharSequence sourceName = sourceName(clazz.name);
			indexWriter.addTypeDeclaration(session, clazz.access, sourceName, null);
			for (MethodNode method : clazz.methods) {
				indexWriter.addMethodDeclaration(session, sourceName, method.access, method.name, method.signature,
						null);
			}

			for (FieldNode field : clazz.fields) {
				indexWriter.addFieldDeclaration(session, sourceName, field.access, field.name,
						monikerFactory.createTypeMoniker(address, sourceName));
			}
			indexConstantPoolReferences(session, address, parser);

			return true;
		} catch (IOException e) {
			return false;
		} finally {
			indexWriter.doneIndexing(session);
		}
	}

	private void indexConstantPoolReferences(Object session, DocumentAddress address, ClassReader reader) {
		for (int i = 1; i < reader.getItemCount(); i++) {
			int offset = reader.getItem(i);
			if (offset > 0) {
				int tag = reader.readByte(reader.getItem(i) - 1);

				switch (tag) {
				case ClassFileConstants.FieldRefTag: {
					// add reference to the class/interface and field name and type
					FieldRef ref = readFieldReferenceAt(reader, i);
					indexWriter.addFieldReference(session, false, false,
							monikerFactory.createTypeMoniker(address, sourceName(ref.containingType())),
							ref.nameAndType().name(), null);
					indexWriter.addTypeReference(session, TypeReferenceKind.FieldType,
							sourceName(ref.nameAndType().type()), null);
					break;
				}
				case ClassFileConstants.MethodRefTag:
					// add reference to the class and method name and type
				case ClassFileConstants.InterfaceMethodRefTag: {
					// add reference to the interface and method name and type
					MethodRef ref = readMethodReferenceAt(reader, i);
					if (ref.containingType().charAt(0) != '[') {
						CharSequence name = ref.nameAndType().name();
						if ("<init>".equals(name)) {
							name = typeName(sourceName(ref.containingType()));
						}
						// add a method reference
						indexWriter.addMethodReference(session, MethodReferenceKind.QualifiedReference,
								monikerFactory.createTypeMoniker(address, sourceName(ref.containingType())), name,
								ref.nameAndType().type(), null);
					}
					break;
				}
				case ClassFileConstants.ClassTag: {
					// add a type reference
					CharSequence name = sourceName(readClassNameAt(reader, i));
					if (name.length() > 0 && name.charAt(0) == '[')
						break; // skip over array references
					indexWriter.addTypeReference(session, TypeReferenceKind.Unknown, name, null);

					break;
				}
				}
			}
		}
	}

	private CharSequence typeName(CharSequence sourceName) {
		int dotIndex = sourceName.toString().indexOf('.');
		return sourceName.subSequence(dotIndex + 1, sourceName.length());
	}

	private FieldRef readFieldReferenceAt(ClassReader reader, int cpIndex) {
		int cpOffset = reader.getItem(cpIndex);
		CharSequence clazz = readClassNameAt(reader, reader.readUnsignedShort(cpOffset));
		NameAndType nameAndType = readNameAndTypeAt(reader, reader.readUnsignedShort(cpOffset + 2));
		return new FieldRef(clazz, nameAndType);
	}

	private CharSequence readClassNameAt(ClassReader reader, int cpIndex) {
		int cpOffset = reader.getItem(cpIndex);
		return readUtf8At(reader, cpOffset);
	}

	private CharSequence readUtf8At(ClassReader reader, int cpOffset) {
		return reader.readUTF8(cpOffset, buffer);
	}

	private NameAndType readNameAndTypeAt(ClassReader reader, int cpIndex) {
		int cpOffset = reader.getItem(cpIndex);
		CharSequence name = readUtf8At(reader, cpOffset);
		CharSequence descriptor = readUtf8At(reader, cpOffset + 2);
		return new NameAndType(name, descriptor);
	}

	private MethodRef readMethodReferenceAt(ClassReader reader, int cpIndex) {
		int cpOffset = reader.getItem(cpIndex);
		CharSequence clazz = readClassNameAt(reader, reader.readUnsignedShort(cpOffset));
		NameAndType nameAndType = readNameAndTypeAt(reader, reader.readUnsignedShort(cpOffset + 2));
		return new MethodRef(clazz, nameAndType);
	}

	private CharSequence sourceName(CharSequence clazz) {
		return clazz.toString().replace('$', '.');
	}
}

record NameAndType(CharSequence name, CharSequence type) {
};

record FieldRef(CharSequence containingType, NameAndType nameAndType) {
}

record MethodRef(CharSequence containingType, NameAndType nameAndType) {
}

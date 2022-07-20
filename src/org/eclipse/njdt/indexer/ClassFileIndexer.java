package org.eclipse.njdt.indexer;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiFunction;

import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.eclipse.njdt.indexer.writer.DocumentAddress;
import org.eclipse.njdt.indexer.writer.FieldReferenceKind;
import org.eclipse.njdt.indexer.writer.IndexWriter;
import org.eclipse.njdt.indexer.writer.IndexWriterDocumentSession;
import org.eclipse.njdt.indexer.writer.MethodReferenceKind;
import org.eclipse.njdt.indexer.writer.TypeReferenceKind;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassFileIndexer {
	private IndexWriter indexWriter;
	private char[] buffer=  new char[1 << 16];

	public ClassFileIndexer(IndexWriter indexWriter) {
		this.indexWriter = indexWriter;
	}

	boolean indexClassFile(DocumentAddress address, InputStream bytes) {
		IndexWriterDocumentSession session = indexWriter.beginIndexing(address);
		try {

			ClassReader parser = new ClassReader(bytes);
			ClassNode clazz = new ClassNode();
			parser.accept(clazz, ClassReader.SKIP_CODE);
			CharSequence sourceName = sourceName(clazz.name);
			session.addTypeDeclaration(clazz.access, sourceName, null);
			for (MethodNode method : clazz.methods) {
				session.addMethodDeclaration(method.access, sourceName, method.name, method.desc,
						null);
			}

			for (FieldNode field : clazz.fields) {
				session.addFieldDeclaration(field.access, sourceName, field.name, sourceName, null);
			}
			indexConstantPoolReferences(session, address, parser);

			return true;
		} catch (IOException e) {
			return false;
		} finally {
			session.done();
		}
	}

	private void indexConstantPoolReferences(IndexWriterDocumentSession session, DocumentAddress address, ClassReader reader) {
		for (int i = 1; i < reader.getItemCount(); i++) {
			int offset = reader.getItem(i);
			if (offset > 0) {
				int tag = reader.readByte(reader.getItem(i) - 1);

				switch (tag) {
				case ClassFileConstants.FieldRefTag: {
					// add reference to the class/interface and field name and type
					FieldRef ref = readFieldReferenceAt(reader, i);
					session.addFieldReference(FieldReferenceKind.NONE,
							sourceName(ref.containingType()),
							false, ref.nameAndType().name(), null);
					session.addTypeReference(TypeReferenceKind.FieldType,
							sourceName(decodeBaseDescriptor(ref.nameAndType().type())), false, null);
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
						session.addMethodReference(MethodReferenceKind.QualifiedReference,
								sourceName(ref.containingType()), false, name,
								ref.nameAndType().type(), null);
					}
					break;
				}
				case ClassFileConstants.ClassTag: {
					// add a type reference
					CharSequence name = sourceName(readClassNameAt(reader, i));
					if (name.length() > 0 && name.charAt(0) == '[')
						break; // skip over array references
					session.addTypeReference(TypeReferenceKind.Unknown, name, false, null);

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

	private String readUtf8At(ClassReader reader, int cpOffset) {
		return reader.readUTF8(cpOffset, buffer);
	}

	private NameAndType readNameAndTypeAt(ClassReader reader, int cpIndex) {
		int cpOffset = reader.getItem(cpIndex);
		CharSequence name = readUtf8At(reader, cpOffset);
		CharSequence descriptor = readUtf8At(reader, cpOffset + 2);
		return new NameAndType(name, descriptor);
	}

	private CharSequence decodeBaseDescriptor(CharSequence descriptor) {
		return decodeTypeDescriptor(descriptor, (CharSequence signature, Integer arrayDimensions) -> {
			return signature;
		});
	}

//	private CharSequence decodeTypeDescriptor(CharSequence descriptor) {
//		return decodeTypeDescriptor(descriptor, (CharSequence signature, Integer arrayDimensions) -> {
//			StringBuilder buf = new StringBuilder(signature);
//			for (int i = 0; i < arrayDimensions; i++) {
//				buf.append("[]");
//			}
//			return buf;
//		});
//	}

	private CharSequence decodeTypeDescriptor(CharSequence signature,
			BiFunction<CharSequence, Integer, CharSequence> arrayTypeOf) {
		if (signature == null)
			return null;
		int arrayDim = 0;
		int i = 0;
		while (i < signature.length() && signature.charAt(i) == '[') {
			arrayDim++;
			i++;
		}
		switch (signature.charAt(i)) {
		case 'B':
			return arrayTypeOf.apply("byte", arrayDim);
		case 'C':
			return arrayTypeOf.apply("char", arrayDim);

		case 'D':
			return arrayTypeOf.apply("double", arrayDim);

		case 'F':
			return arrayTypeOf.apply("float", arrayDim);
		case 'I':
			return arrayTypeOf.apply("integer", arrayDim);
		case 'J':
			return arrayTypeOf.apply("long", arrayDim);
		case 'L':
			return arrayTypeOf.apply(signature.subSequence(i + 1, signature.length() - 1), arrayDim);
		case 'S':
			return arrayTypeOf.apply("short", arrayDim);
		case 'Z':
			return arrayTypeOf.apply("boolean", arrayDim);
		case 'V':
			return arrayTypeOf.apply("void", arrayDim);
		default:
			throw new RuntimeException("Unexpected type tag: "+signature.charAt(i));
		}
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

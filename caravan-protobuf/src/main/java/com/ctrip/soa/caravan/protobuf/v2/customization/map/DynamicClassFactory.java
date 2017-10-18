package com.ctrip.soa.caravan.protobuf.v2.customization.map;

import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_7;

import com.ctrip.soa.caravan.common.collect.KeyValuePair;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.type.MapType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

/**
 * Created by marsqing on 24/04/2017.
 */
public class DynamicClassFactory {

  private final static String OBJECT_TYPE = "Ljava/lang/Object;";

  public static DynamicClassFactory INSTANCE = new DynamicClassFactory();

  private Map<ClassPair, Class<?>> classMap = new HashMap<>();
  private ClassLoader classLoader;
  private Method defineClassMethod;
  private AtomicLong index = new AtomicLong(0);
  private String interfaceName;

  private DynamicClassFactory() {
    interfaceName = KVPair.class.getName().replaceAll("\\.", "/");
    classLoader = this.getClass().getClassLoader();
    Class<?> curClass = this.getClass().getClassLoader().getClass();
    while (curClass != Object.class) {
      try {
        defineClassMethod = curClass.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
        defineClassMethod.setAccessible(true);
        break;
      } catch (NoSuchMethodException e) {
        curClass = curClass.getSuperclass();
      }
    }

    if (defineClassMethod == null) {
      throw new RuntimeException("No defineClass method found on classloader");
    }
  }

  public synchronized Class<?> fetchOrCreatePairClass(ClassPair classPair) {
    if (!classMap.containsKey(classPair)) {
      classMap.put(classPair, createClass(classPair));
    }
    return classMap.get(classPair);
  }

  public synchronized Class<?> fetchOrCreatePairClass(MapType mapType) {
    ClassPair classPair = new ClassPair(mapType.getKeyType().getRawClass(), mapType.getContentType().getRawClass());
    return fetchOrCreatePairClass(classPair);
  }

  private Class<?> createClass(ClassPair classPair) {
    String className = generateClassName(classPair);
    String classStr = className.replaceAll("\\.", "/");

    String keyClassType = Type.getDescriptor(classPair.keyClass);
    String valueClassType = Type.getDescriptor(classPair.valueClass);

    ClassWriter cw = new ClassWriter(0);
    cw.visit(V1_7, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object", new String[]{interfaceName});

    AnnotationVisitor anno = cw.visitAnnotation(Type.getDescriptor(JsonPropertyOrder.class), true);
    AnnotationVisitor aa = anno.visitArray("value");
    aa.visit("", "key");
    aa.visit("", "value");
    aa.visitEnd();
    anno.visitEnd();

    FieldVisitor keyField = cw.visitField(ACC_PRIVATE, "key", keyClassType, null, null);
    keyField.visitEnd();

    FieldVisitor valueField = cw.visitField(ACC_PRIVATE, "value", valueClassType, null, null);
    valueField.visitEnd();

    MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
    mv.visitMaxs(2, 1);
    mv.visitVarInsn(ALOAD, 0);
    mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(Object.class), "<init>", "()V", false); // call the constructor of super class
    mv.visitInsn(RETURN);
    mv.visitEnd();

    addGetterSetter(classPair, className, classStr, keyClassType, valueClassType, cw);
    addKVPairMethods(classPair, className, classStr, keyClassType, valueClassType, cw);

    cw.visitEnd();

    return defineClass(className, cw.toByteArray());
  }

  private Class<?> defineClass(String className, byte[] bytes) {
    try {
      return (Class<?>) defineClassMethod.invoke(classLoader, className, bytes, 0, bytes.length);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Can not define class " + className, e);
    }
  }

  private void addGetterSetter(ClassPair classPair, String className, String classStr, String keyClassType, String valueClassType, ClassWriter cw) {
    MethodVisitor getKey = cw.visitMethod(ACC_PUBLIC, "getKey", "()" + keyClassType, null, null);
    getKey.visitCode();
    getKey.visitVarInsn(ALOAD, 0);
    getKey.visitFieldInsn(GETFIELD, className, "key", keyClassType);
    getKey.visitInsn(Type.getType(classPair.keyClass).getOpcode(IRETURN));
    getKey.visitMaxs(1, 1);
    getKey.visitEnd();

    MethodVisitor getValue = cw.visitMethod(ACC_PUBLIC, "getValue", "()" + valueClassType, null, null);
    getValue.visitCode();
    getValue.visitVarInsn(ALOAD, 0);
    getValue.visitFieldInsn(GETFIELD, className, "value", valueClassType);
    getValue.visitInsn(Type.getType(classPair.valueClass).getOpcode(IRETURN));
    getValue.visitMaxs(1, 1);
    getValue.visitEnd();

    MethodVisitor setKey = cw.visitMethod(ACC_PUBLIC, "setKey", "(" + keyClassType + ")V", null, null);
    setKey.visitCode();
    setKey.visitVarInsn(ALOAD, 0);
    setKey.visitVarInsn(ALOAD, 1);
    setKey.visitTypeInsn(CHECKCAST, Type.getInternalName(classPair.keyClass));
    setKey.visitFieldInsn(PUTFIELD, classStr, "key", keyClassType);
    setKey.visitInsn(RETURN);
    setKey.visitMaxs(2, 2);
    setKey.visitEnd();

    MethodVisitor setValue = cw.visitMethod(ACC_PUBLIC, "setValue", "(" + valueClassType + ")V", null, null);
    setValue.visitCode();
    setValue.visitVarInsn(ALOAD, 0);
    setValue.visitVarInsn(ALOAD, 1);
    setValue.visitTypeInsn(CHECKCAST, Type.getInternalName(classPair.valueClass));
    setValue.visitFieldInsn(PUTFIELD, classStr, "value", valueClassType);
    setValue.visitInsn(RETURN);
    setValue.visitMaxs(2, 2);
    setValue.visitEnd();
  }

  private void addKVPairMethods(ClassPair classPair, String className, String classStr, String keyClassType, String valueClassType, ClassWriter cw) {
    MethodVisitor getKey = cw.visitMethod(ACC_PUBLIC, "key", "()" + OBJECT_TYPE, null, null);
    getKey.visitCode();
    getKey.visitVarInsn(ALOAD, 0);
    getKey.visitFieldInsn(GETFIELD, className, "key", keyClassType);
    getKey.visitInsn(Type.getType(classPair.keyClass).getOpcode(IRETURN));
    getKey.visitMaxs(1, 1);
    getKey.visitEnd();

    MethodVisitor getValue = cw.visitMethod(ACC_PUBLIC, "value", "()" + OBJECT_TYPE, null, null);
    getValue.visitCode();
    getValue.visitVarInsn(ALOAD, 0);
    getValue.visitFieldInsn(GETFIELD, className, "value", valueClassType);
    getValue.visitInsn(Type.getType(classPair.valueClass).getOpcode(IRETURN));
    getValue.visitMaxs(1, 1);
    getValue.visitEnd();

    MethodVisitor setKey = cw.visitMethod(ACC_PUBLIC, "key", "(" + OBJECT_TYPE + ")V", null, null);
    setKey.visitCode();
    setKey.visitVarInsn(ALOAD, 0);
    setKey.visitVarInsn(ALOAD, 1);
    setKey.visitTypeInsn(CHECKCAST, Type.getInternalName(classPair.keyClass));
    setKey.visitFieldInsn(PUTFIELD, classStr, "key", keyClassType);
    setKey.visitInsn(RETURN);
    setKey.visitMaxs(2, 2);
    setKey.visitEnd();

    MethodVisitor setValue = cw.visitMethod(ACC_PUBLIC, "value", "(" + OBJECT_TYPE + ")V", null, null);
    setValue.visitCode();
    setValue.visitVarInsn(ALOAD, 0);
    setValue.visitVarInsn(ALOAD, 1);
    setValue.visitTypeInsn(CHECKCAST, Type.getInternalName(classPair.valueClass));
    setValue.visitFieldInsn(PUTFIELD, classStr, "value", valueClassType);
    setValue.visitInsn(RETURN);
    setValue.visitMaxs(2, 2);
    setValue.visitEnd();
  }

  private String generateClassName(ClassPair classPair) {
    return "CaravanGeneratedPrivateClassForMap" + classPair.keyClass.getSimpleName() + classPair.valueClass.getSimpleName() + index.getAndIncrement();
  }

  public void setKeyValue(Object obj, Object key, Object value) throws Exception {
    ensureKVPair(obj);

    KVPair pair = (KVPair) obj;

    pair.key(key);
    pair.value(value);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public KeyValuePair getKeyValue(Object obj) throws Exception {
    ensureKVPair(obj);

    KVPair pair = (KVPair) obj;

    return new KeyValuePair(pair.key(), pair.value());
  }

  private void ensureKVPair(Object obj) {
    if (!(obj instanceof KVPair)) {
      throw new RuntimeException(String.format("%s(%s) is not KVPair", obj, obj == null ? "null" : obj.getClass()));
    }
  }

}

package com.ctrip.soa.caravan.protobuf.v2;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.V1_6;

import java.lang.reflect.Field;
import org.junit.Test;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;

/**
 * Created by marsqing on 21/04/2017.
 */
public class AsmTest {

  public class DynamicClassLoader extends ClassLoader {

    public Class<?> defineClass(String name, byte[] b) {
      return defineClass(name, b, 0, b.length);
    }
  }

  @Test
  public void test() {
    ClassWriter cw = new ClassWriter(0);
    cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "HelloGen", null, "java/lang/Object", null);

    FieldVisitor fv = cw.visitField(1, "_key", Type.getDescriptor(String.class), null, null);
    fv.visitEnd();

    fv = cw.visitField(1, "_value", Type.getDescriptor(InnerClass.class), null, null);
    fv.visitEnd();

    Class<?> clazz = new DynamicClassLoader().defineClass("HelloGen", cw.toByteArray());
    for(Field f : clazz.getDeclaredFields()) {
      System.out.println(f.getName());
    }
  }

  public static class InnerClass {

  }

}

package com.ctrip.soa.caravan.util.serializer.xml;

import com.ctrip.soa.caravan.common.serializer.SerializationException;
import com.ctrip.soa.caravan.common.serializer.StreamSerializer;
import com.ctrip.soa.caravan.common.serializer.StringSerializer;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.reflect.Reflection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.stream.StreamSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class JAXBXmlSerializer implements StreamSerializer, StringSerializer {

    public static final JAXBXmlSerializer INSTANCE = new JAXBXmlSerializer();

    private LoadingCache<Class<?>, JAXBContext> _jctxtLoader;
    private LoadingCache<Class<?>, Object> _objFactoryLoader;
    private LoadingCache<Class<?>, Method> _modelCreatorLoader;

    private JAXBXmlSerializer() {
        _jctxtLoader = CacheBuilder.newBuilder().build(new CacheLoader<Class<?>, JAXBContext>() {
            @Override
            public JAXBContext load(Class<?> key) throws Exception {
                return JAXBContext.newInstance(key);
            }
        });
        _objFactoryLoader = CacheBuilder.newBuilder().build(new CacheLoader<Class<?>, Object>() {
            @Override
            public Object load(Class<?> clazz) {
                String objFactoryFullName = Reflection.getPackageName(clazz) + ".ObjectFactory";
                try {
                    return clazz.getClassLoader().loadClass(objFactoryFullName).newInstance();
                } catch (Error | RuntimeException ex) {
                    throw ex;
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        _modelCreatorLoader = CacheBuilder.newBuilder().build(new CacheLoader<Class<?>, Method>() {
            @Override
            public Method load(Class<?> clazz) throws ExecutionException {
                Class<?> objectFactory = _objFactoryLoader.get(clazz).getClass();
                for (Method m : objectFactory.getMethods()) {
                    if (JAXBElement.class.equals(m.getReturnType()) && m.getParameterTypes().length == 1 && m.getParameterTypes()[0].isAssignableFrom(clazz)) {
                        return m;
                    }
                }

                String errorMessage = "Cannot find method with return type: " + JAXBElement.class + " in class: " + clazz;
                throw new RuntimeException(errorMessage);
            }
        });
    }

    @Override
    public String contentType() {
        return "application/xml";
    }

    @Override
    public void serialize(OutputStream os, Object obj) {
        if (obj == null)
            return;

        Class<?> objType = obj.getClass();

        try {
            JAXBContext jctxt = _jctxtLoader.get(objType);
            Marshaller marshaller = jctxt.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);

            Object content;
            if (objType.getAnnotation(XmlRootElement.class) != null) {
                content = obj;
            } else {
                Method m = _modelCreatorLoader.get(objType);
                content = m.invoke(_objFactoryLoader.get(objType), obj);
            }

            marshaller.marshal(content, os);
        } catch (Error | RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            String errorMessage = "The object " + objType.getName() + " cannot be serialized/deserialized";
            throw new SerializationException(errorMessage, ex);
        }
    }

    @Override
    public <T> T deserialize(InputStream stream, Class<T> outputType) {
        try {
            JAXBContext jctxt = _jctxtLoader.get(outputType);
            Unmarshaller unmarshaller = jctxt.createUnmarshaller();
            JAXBElement<T> entityElement = unmarshaller.unmarshal(new StreamSource(new XmlFilterInputStream(stream)), outputType);
            return entityElement.getValue();
        } catch (Error | RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SerializationException(ex);
        }
    }

    @Override
    public String serialize(Object obj) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();) {
            serialize(os, obj);
            return new String(os.toByteArray(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }

    @Override
    public <T> T deserialize(String s, Class<T> clazz) {
        try (ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));) {
            return deserialize(is, clazz);
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }

}

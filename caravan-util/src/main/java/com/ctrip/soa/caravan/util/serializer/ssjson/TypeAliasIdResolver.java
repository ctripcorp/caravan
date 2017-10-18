package com.ctrip.soa.caravan.util.serializer.ssjson;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

/**
 * Created by Jinhua Liang on 10/05/2016.
 */
public class TypeAliasIdResolver extends TypeIdResolverBase {

	// many id to one class
	private static Map<String, Class<?>> idToClassMapping = new ConcurrentHashMap<>();
	// one class to one id
	private static Map<Class<?>, String> classToIdMapping = new ConcurrentHashMap<>();

	@Override
	public String idFromValue(Object value) {
		return idFromValueAndType(value, value.getClass());
	}

	@Override
	public String idFromValueAndType(Object value, Class<?> suggestedType) {
		String id = classToIdMapping.get(suggestedType);
		if (id != null) {
			return id;
		} else {
			throw new IllegalArgumentException(String.format("Unknown id for type %s", suggestedType));
		}
	}

	@Override
	public JavaType typeFromId(DatabindContext context, String id) throws IOException {
		Class<?> type = idToClassMapping.get(id);

		if (type == null) {
			synchronized (TypeAliasIdResolver.class) {
				type = idToClassMapping.get(id);
				if (type == null) {
					int commaPos = id.indexOf(",");
					if (commaPos > 0) {
						String exactId = id.substring(0, commaPos).trim();
						type = idToClassMapping.get(exactId);
						if (type != null) {
							idToClassMapping.put(id, type);
						}
					}
				}
			}
		}

		if (type != null) {
			return context.constructType(type);
		} else {
			throw new IllegalArgumentException(String.format("Unknown type for id %s", id));
		}
	}

	public static void registerType(Class<?> type) {
		if (!idToClassMapping.containsValue(type)) {
			synchronized (TypeAliasIdResolver.class) {
				if (!idToClassMapping.containsValue(type)) {
					TypeAlias typeAliasAnnotation = type.getAnnotation(TypeAlias.class);
					idToClassMapping.put(typeAliasAnnotation.value(), type);
					classToIdMapping.put(type, typeAliasAnnotation.value());
				}
			}
		}
	}

	@Override
	public Id getMechanism() {
		return Id.CUSTOM;
	}

}

package com.ctrip.soa.caravan.util.serializer.ssjson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by w.jian on 2017/4/28.
 */
@JsonAutoDetect(getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE)
public class TestEntity2 {
    
    @JsonProperty("ATestField")
    protected int aTestField;
    
    public int getATestField() {
        return aTestField;
    }
    
    public void setATestField(int aField) {
        this.aTestField = aField;
    }
    
    @Test
    public void serializeTest() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SSJsonSerializer.DEFAULT.serialize(outputStream, new TestEntity2());
        System.out.println(outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        SSJsonSerializer.DEFAULT.deserialize(inputStream, TestEntity2.class);
    }
}

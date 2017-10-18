package com.ctrip.soa.caravan.util.serializer.ssjson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * Created by w.jian on 2017/4/28.
 */
@JsonAutoDetect(getterVisibility = NONE, setterVisibility = NONE, isGetterVisibility = NONE)
public class TestEntity1 {
    
    @JsonProperty("ATestField")
    protected int aTestIntField;
    
    public int getATestIntField() {
        return aTestIntField;
    }
    
    public void setATestIntField(int aField) {
        this.aTestIntField = aField;
    }
    
    @Test
    public void serializeTest() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SSJsonSerializer.DEFAULT.serialize(outputStream, new TestEntity1());
        System.out.println(outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        SSJsonSerializer.DEFAULT.deserialize(inputStream, TestEntity1.class);
    }
}

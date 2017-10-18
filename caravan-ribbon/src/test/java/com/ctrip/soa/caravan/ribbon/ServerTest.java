package com.ctrip.soa.caravan.ribbon;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

/**
 * Created by w.jian on 2016/11/8.
 */
public class ServerTest {

    @Test
    public void serverEquals_ServerId_CaseInsensitive_Test() {
        Server server1 = Server.newBuilder().setServerId("Abc").build();
        Server server2 = Server.newBuilder().setServerId("abc").build();
        Assert.assertEquals(server1, server2);
        Assert.assertEquals(server2, server1);
    }

    @Test
    public void serverEquals_ServerId_Equals_Test() {
        Server server1 = Server.newBuilder().setServerId("abc").build();
        Server server2 = Server.newBuilder().setServerId("abc").build();
        Assert.assertEquals(server1, server2);
        Assert.assertEquals(server2, server1);
    }

    @Test
    public void serverEquals_IsAlive_Equals_Test() {
        Server server1 = Server.newBuilder().setServerId("abc").setIsAlive(true).build();
        Server server2 = Server.newBuilder().setServerId("abc").setIsAlive(false).build();
        Assert.assertEquals(server1, server2);
        Assert.assertEquals(server2, server1);
    }

    @Test
    public void serverEquals_Metadata_Equals_Test() {
        HashMap<String, String> server1Metadata = new HashMap<>();
        server1Metadata.put("Hello", "World");
        server1Metadata.put("City", "Shanghai");
        Server server1 = Server.newBuilder().setServerId("abc").setMetadata(server1Metadata).build();

        HashMap<String, String> server2Metadata = new HashMap<>();
        server2Metadata.put("Hello", "World");
        server2Metadata.put("City", "Shanghai");
        Server server2 = Server.newBuilder().setServerId("abc").setMetadata(server2Metadata).build();

        Assert.assertEquals(server1, server2);
        Assert.assertEquals(server2, server1);
    }

    @Test
    public void ServerEquals_Metadata_NullEqualEmpty_Test() {
        Server server1 = Server.newBuilder().setServerId("abc").setMetadata(null).build();
        Server server2 = Server.newBuilder().setServerId("abc").setMetadata(new HashMap<String, String>()).build();
        Assert.assertEquals(server1, server2);
        Assert.assertEquals(server2, server1);
    }

    @Test
    public void ServerEquals_Metadata_CountNotEquals_Test() {
        HashMap<String, String> server1Metadata = new HashMap<>();
        server1Metadata.put("City", "Shanghai");
        Server server1 = Server.newBuilder().setServerId("abc").setMetadata(server1Metadata).build();

        HashMap<String, String> server2Metadata = new HashMap<>();
        server2Metadata.put("Hello", "World");
        server2Metadata.put("City", "Shanghai");
        Server server2 = Server.newBuilder().setServerId("abc").setMetadata(server2Metadata).build();

        Assert.assertNotEquals(server1, server2);
        Assert.assertNotEquals(server2, server1);
    }

    @Test
    public void ServerEquals_Metadata_KeyNotEquals_Test() {
        HashMap<String, String> server1Metadata = new HashMap<>();
        server1Metadata.put("Hello ", "World");
        server1Metadata.put("City", "Shanghai");
        Server server1 = Server.newBuilder().setServerId("abc").setMetadata(server1Metadata).build();

        HashMap<String, String> server2Metadata = new HashMap<>();
        server2Metadata.put("Hello", "World");
        server2Metadata.put("City", "Shanghai");
        Server server2 = Server.newBuilder().setServerId("abc").setMetadata(server2Metadata).build();

        Assert.assertNotEquals(server1, server2);
        Assert.assertNotEquals(server2, server1);
    }

    @Test
    public void ServerEquals_Metadata_ValueNotEquals_Test() {
        HashMap<String, String> server1Metadata = new HashMap<>();
        server1Metadata.put("Hello", "World ");
        server1Metadata.put("City", "Shanghai");
        Server server1 = Server.newBuilder().setServerId("abc").setMetadata(server1Metadata).build();

        HashMap<String, String> server2Metadata = new HashMap<>();
        server2Metadata.put("Hello", "World");
        server2Metadata.put("City", "Shanghai");
        Server server2 = Server.newBuilder().setServerId("abc").setMetadata(server2Metadata).build();

        Assert.assertNotEquals(server1, server2);
        Assert.assertNotEquals(server2, server1);
    }
}

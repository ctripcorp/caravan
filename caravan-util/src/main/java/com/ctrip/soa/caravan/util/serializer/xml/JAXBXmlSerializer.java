package com.ctrip.soa.caravan.util.serializer.xml;

import com.ctrip.soa.caravan.common.serializer.StreamSerializer;
import com.ctrip.soa.caravan.common.serializer.StringSerializer;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class JAXBXmlSerializer extends AbstractJAXBXmlSerializer implements StreamSerializer, StringSerializer {

    public static final JAXBXmlSerializer INSTANCE = new JAXBXmlSerializer();

    private JAXBXmlSerializer() {
    }

    @Override
    public String contentType() {
        return "application/xml";
    }


    @Override
    protected void doMarshal(Marshaller marshaller, Object content, OutputStream os) throws JAXBException {
        marshaller.marshal(content, os);
    }

    @Override
    protected <T> JAXBElement<T> doUnmarshal(Unmarshaller unmarshaller, Class<T> outputType, InputStream stream) throws JAXBException {
        return unmarshaller.unmarshal(new StreamSource(new XmlFilterInputStream(stream)), outputType);
    }

}

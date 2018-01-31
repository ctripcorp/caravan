package com.ctrip.soa.caravan.util.serializer.xml;

import com.sun.xml.fastinfoset.stax.StAXDocumentParser;
import com.sun.xml.fastinfoset.stax.StAXDocumentSerializer;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

public class FastInfosetSerializer extends AbstractJAXBXmlSerializer {

  public static final FastInfosetSerializer INSTANCE = new FastInfosetSerializer();

  @Override
  public String contentType() {
    return "application/fastinfoset";
  }

  @Override
  protected void doMarshal(Marshaller marshaller, Object content, OutputStream os) throws Exception {
    GZIPOutputStream gzipOut = new GZIPOutputStream(new NoCloseOutputStream(os));
    XMLStreamWriter writer = new StAXDocumentSerializer(gzipOut);
    try {
      marshaller.marshal(content, writer);
    } finally {
      writer.close();
      gzipOut.close();
    }
  }

  @Override
  protected <T> JAXBElement<T> doUnmarshal(Unmarshaller unmarshaller, Class<T> outputType, InputStream is) throws Exception {
    GZIPInputStream gzipIn = new GZIPInputStream(new NoCloseInputStream(is));
    XMLStreamReader reader = new StAXDocumentParser(gzipIn);
    try {
      return unmarshaller.unmarshal(reader, outputType);
    } finally {
      reader.close();
      gzipIn.close();
    }
  }


  private static class NoCloseOutputStream extends FilterOutputStream {

    public NoCloseOutputStream(OutputStream out) {
      super(out);
    }

    @Override
    public void close() throws IOException {
    }
  }

  private static class NoCloseInputStream extends FilterInputStream {

    public NoCloseInputStream(InputStream is) {
      super(is);
    }

    @Override
    public void close() throws IOException {
    }

  }
}

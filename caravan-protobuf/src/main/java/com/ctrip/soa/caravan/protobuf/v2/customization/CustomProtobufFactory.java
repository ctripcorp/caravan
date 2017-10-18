package com.ctrip.soa.caravan.protobuf.v2.customization;

import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufFactory;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufParser;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by marsqing on 22/03/2017.
 */
public class CustomProtobufFactory extends ProtobufFactory {

  private static final long serialVersionUID = 1L;

  @Override
  protected ProtobufParser _createParser(InputStream in, IOContext ctxt) throws IOException {
    byte[] buf = ctxt.allocReadIOBuffer();
    return new CustomProtobufParser(ctxt, _parserFeatures,
        _objectCodec, in, buf, 0, 0, true);
  }

  @Override
  protected ProtobufParser _createParser(byte[] data, int offset, int len, IOContext ctxt) throws IOException {
    return new CustomProtobufParser(ctxt, _parserFeatures,
        _objectCodec, null, data, offset, len, false);
  }
}

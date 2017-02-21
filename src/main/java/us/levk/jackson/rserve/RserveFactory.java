/*
 * The MIT License (MIT)
 * Copyright (c) 2017 lev.v.kuznetsov@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package us.levk.jackson.rserve;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import org.rosuda.REngine.REXPMismatchException;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.io.IOContext;

/**
 * Rserve protocol factory
 * 
 * @author levk
 */
public class RserveFactory extends JsonFactory {

  /**
   * Serialization
   */
  private static final long serialVersionUID = 1L;

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonFactory#_createParser(byte[], int, int,
   * com.fasterxml.jackson.core.io.IOContext)
   */
  @Override
  protected JsonParser _createParser (byte[] b, int o, int s, IOContext t) throws IOException {
    try {
      return new RserveParser (t, _parserFeatures, _objectCodec, b, o);
    } catch (REXPMismatchException e) {
      throw new IOException (e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonFactory#_createParser(char[], int, int,
   * com.fasterxml.jackson.core.io.IOContext, boolean)
   */
  @Override
  protected JsonParser _createParser (char[] data, int offset, int len, IOContext ctxt, boolean recyclable)
      throws IOException {
    throw new UnsupportedOperationException ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.core.JsonFactory#_createParser(java.io.DataInput,
   * com.fasterxml.jackson.core.io.IOContext)
   */
  @Override
  protected JsonParser _createParser (DataInput input, IOContext ctxt) throws IOException {
    throw new UnsupportedOperationException ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.core.JsonFactory#_createParser(java.io.InputStream,
   * com.fasterxml.jackson.core.io.IOContext)
   */
  @Override
  protected JsonParser _createParser (InputStream in, IOContext ctxt) throws IOException {
    throw new UnsupportedOperationException ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonFactory#_createParser(java.io.Reader,
   * com.fasterxml.jackson.core.io.IOContext)
   */
  @Override
  protected JsonParser _createParser (Reader r, IOContext ctxt) throws IOException {
    throw new UnsupportedOperationException ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.core.JsonFactory#_createGenerator(java.io.Writer,
   * com.fasterxml.jackson.core.io.IOContext)
   */
  @Override
  protected JsonGenerator _createGenerator (Writer o, IOContext ctxt) throws IOException {
    throw new UnsupportedOperationException ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.core.JsonFactory#_createWriter(java.io.OutputStream,
   * com.fasterxml.jackson.core.JsonEncoding,
   * com.fasterxml.jackson.core.io.IOContext)
   */
  @Override
  protected Writer _createWriter (OutputStream o, JsonEncoding e, IOContext ctxt) throws IOException {
    throw new UnsupportedOperationException ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonFactory#_createUTF8Generator(java.io.
   * OutputStream, com.fasterxml.jackson.core.io.IOContext)
   */
  @Override
  protected JsonGenerator _createUTF8Generator (OutputStream o, IOContext ctxt) throws IOException {
    return new RserveGenerator (_generatorFeatures, _objectCodec, o);
  }
}

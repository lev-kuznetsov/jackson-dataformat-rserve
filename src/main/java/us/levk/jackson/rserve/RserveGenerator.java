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

import static java.lang.System.arraycopy;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPGenericVector;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPNull;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.protocol.REXPFactory;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.GeneratorBase;
import com.fasterxml.jackson.core.json.JsonWriteContext;

/**
 * Rserve protocol generator
 * 
 * @author levk
 */
public class RserveGenerator extends GeneratorBase {

  /**
   * Unnamed list type frame
   * 
   * @author levk
   */
  private static class Frame {
    List <REXP> values = new ArrayList <> ();
  }

  /**
   * Named list type frame
   * 
   * @author levk
   */
  private static class NamedFrame extends Frame {
    List <String> names = new ArrayList <> ();
  }

  /**
   * Output stream
   */
  private final OutputStream out;
  /**
   * Context stack
   */
  private final Stack <Frame> stack = new Stack <> ();

  /**
   * @param features
   * @param codec
   */
  public RserveGenerator (int features, ObjectCodec codec, OutputStream out) {
    super (features, codec);
    this.out = out;
  }

  /**
   * @param features
   * @param codec
   * @param ctxt
   */
  protected RserveGenerator (int features, ObjectCodec codec, JsonWriteContext ctxt, OutputStream out) {
    super (features, codec, ctxt);
    this.out = out;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.base.GeneratorBase#_releaseBuffers()
   */
  @Override
  protected void _releaseBuffers () {}

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.core.base.GeneratorBase#_verifyValueWrite(java.lang.
   * String)
   */
  @Override
  protected void _verifyValueWrite (String arg0) throws IOException {}

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.base.GeneratorBase#flush()
   */
  @Override
  public void flush () throws IOException {
    out.flush ();
  }

  /**
   * @param e
   *          expression to write
   * @throws IOException
   */
  private void writeRexp (REXP e) throws IOException {
    if (!stack.isEmpty ()) stack.peek ().values.add (e);
    else try {
      REXPFactory f = new REXPFactory (e);
      byte[] b = new byte[f.getBinaryLength ()];
      f.getBinaryRepresentation (b, 0);
      out.write (b);
    } catch (REXPMismatchException x) {
      throw new IOException ("Unable to write " + e.toDebugString (), x);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.core.JsonGenerator#writeBinary(com.fasterxml.jackson.
   * core.Base64Variant, byte[], int, int)
   */
  @Override
  public void writeBinary (Base64Variant arg0, byte[] arg1, int arg2, int arg3) throws IOException {
    throw new UnsupportedOperationException ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeBoolean(boolean)
   */
  @Override
  public void writeBoolean (boolean v) throws IOException {
    writeRexp (new REXPLogical (v));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeEndArray()
   */
  @Override
  public void writeEndArray () throws IOException {
    writeRexp (new REXPGenericVector (new RList (stack.pop ().values)));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeEndObject()
   */
  @Override
  public void writeEndObject () throws IOException {
    NamedFrame f = (NamedFrame) stack.pop ();
    writeRexp (new REXPGenericVector (new RList (f.values, f.names)));
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.core.JsonGenerator#writeFieldName(java.lang.String)
   */
  @Override
  public void writeFieldName (String n) throws IOException {
    ((NamedFrame) stack.peek ()).names.add (n);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeNull()
   */
  @Override
  public void writeNull () throws IOException {
    writeRexp (new REXPNull ());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeNumber(int)
   */
  @Override
  public void writeNumber (int v) throws IOException {
    writeRexp (new REXPInteger (v));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeNumber(long)
   */
  @Override
  public void writeNumber (long arg0) throws IOException {
    throw new UnsupportedOperationException ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.core.JsonGenerator#writeNumber(java.math.BigInteger)
   */
  @Override
  public void writeNumber (BigInteger arg0) throws IOException {
    throw new UnsupportedOperationException ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeNumber(double)
   */
  @Override
  public void writeNumber (double v) throws IOException {
    writeRexp (new REXPDouble (v));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeNumber(float)
   */
  @Override
  public void writeNumber (float v) throws IOException {
    writeNumber ((double) v);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.core.JsonGenerator#writeNumber(java.math.BigDecimal)
   */
  @Override
  public void writeNumber (BigDecimal arg0) throws IOException {
    throw new UnsupportedOperationException ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeNumber(java.lang.String)
   */
  @Override
  public void writeNumber (String arg0) throws IOException {
    throw new UnsupportedOperationException ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeRaw(java.lang.String)
   */
  @Override
  public void writeRaw (String arg0) throws IOException {
    throw new UnsupportedOperationException ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeRaw(char)
   */
  @Override
  public void writeRaw (char arg0) throws IOException {
    throw new UnsupportedOperationException ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeRaw(java.lang.String,
   * int, int)
   */
  @Override
  public void writeRaw (String arg0, int arg1, int arg2) throws IOException {
    throw new UnsupportedOperationException ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeRaw(char[], int, int)
   */
  @Override
  public void writeRaw (char[] arg0, int arg1, int arg2) throws IOException {
    throw new UnsupportedOperationException ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeRawUTF8String(byte[],
   * int, int)
   */
  @Override
  public void writeRawUTF8String (byte[] arg0, int arg1, int arg2) throws IOException {
    throw new UnsupportedOperationException ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeStartArray()
   */
  @Override
  public void writeStartArray () throws IOException {
    stack.push (new Frame ());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeStartObject()
   */
  @Override
  public void writeStartObject () throws IOException {
    stack.push (new NamedFrame ());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeString(java.lang.String)
   */
  @Override
  public void writeString (String v) throws IOException {
    writeRexp (new REXPString (v));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeString(char[], int, int)
   */
  @Override
  public void writeString (char[] v, int o, int s) throws IOException {
    writeString (new String (v, o, s));
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonGenerator#writeUTF8String(byte[], int,
   * int)
   */
  @Override
  public void writeUTF8String (byte[] v, int o, int s) throws IOException {
    byte[] t = new byte[s];
    arraycopy (v, o, t, 0, s);
    writeString (new String (t, "UTF-8"));
  }
}

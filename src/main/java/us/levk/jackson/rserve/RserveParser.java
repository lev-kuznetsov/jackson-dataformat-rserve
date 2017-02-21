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

import static com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
import static us.levk.jackson.rserve.ParserState.parse;

import java.io.IOException;
import java.util.Iterator;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.protocol.REXPFactory;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.base.ParserBase;
import com.fasterxml.jackson.core.io.IOContext;

/**
 * Rserve protocol parser
 * 
 * @author levk
 */
public class RserveParser extends ParserBase {

  /**
   * Codec
   */
  private ObjectCodec _codec;
  /**
   * Parser states
   */
  private final Iterator <ParserState <?>> states;
  /**
   * Current token
   */
  private ParserState <?> current;

  /**
   * @param t
   * @param f
   * @param c
   * @param b
   * @param o
   * @throws REXPMismatchException
   */
  public RserveParser (IOContext t, int f, ObjectCodec c, byte[] b, int o) throws REXPMismatchException {
    super (t, f);
    setCodec (c);
    REXPFactory v = new REXPFactory ();
    v.parseREXP (b, o);
    states = parse (v.getREXP ()).iterator ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.base.ParserBase#_closeInput()
   */
  @Override
  protected void _closeInput () throws IOException {}

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.base.ParserMinimalBase#nextToken()
   */
  @Override
  public JsonToken nextToken () throws IOException {
    JsonToken r = _currToken = (current = states.next ()).type;
    if (r == FIELD_NAME) _parsingContext.setCurrentName ((String) current.body);
    return r;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.base.ParserMinimalBase#getText()
   */
  @Override
  public String getText () throws IOException {
    return (String) current.body;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.base.ParserMinimalBase#getTextCharacters()
   */
  @Override
  public char[] getTextCharacters () throws IOException {
    return getText ().toCharArray ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.base.ParserMinimalBase#getTextLength()
   */
  @Override
  public int getTextLength () throws IOException {
    return getText ().length ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.base.ParserMinimalBase#getTextOffset()
   */
  @Override
  public int getTextOffset () throws IOException {
    return 0;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonParser#getCodec()
   */
  @Override
  public ObjectCodec getCodec () {
    return _codec;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.core.JsonParser#setCodec(com.fasterxml.jackson.core.
   * ObjectCodec)
   */
  @Override
  public void setCodec (ObjectCodec c) {
    _codec = c;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.base.ParserBase#getIntValue()
   */
  @Override
  public int getIntValue () throws IOException {
    return ((Number) current.body).intValue ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.base.ParserBase#getDoubleValue()
   */
  @Override
  public double getDoubleValue () throws IOException {
    return ((Number) current.body).doubleValue ();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.core.JsonParser#getBooleanValue()
   */
  @Override
  public boolean getBooleanValue () throws IOException {
    return (Boolean) current.body;
  }
}

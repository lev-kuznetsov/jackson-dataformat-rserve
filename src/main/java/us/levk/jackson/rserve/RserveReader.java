/*
 * The MIT License (MIT)
 * Copyright (c) 2017 lev.v.kunetsov@gmail.com
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

import java.io.IOException;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.protocol.REXPFactory;

import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

/**
 * Rserve mapping reader
 * 
 * @author levk
 */
public class RserveReader extends ObjectReader {

  /**
   * Serialization
   */
  private static final long serialVersionUID = 1L;

  /**
   * @param m
   *          mapper
   * @param c
   *          config
   * @param t
   *          valueType
   * @param u
   *          valueToUpdate
   * @param s
   *          schema
   * @param i
   *          injectableValues
   */
  public RserveReader (ObjectMapper m, DeserializationConfig c, JavaType t, Object u, FormatSchema s,
                       InjectableValues i) {
    super (m, c, t, u, s, i);
  }

  /**
   * @param e
   *          expression
   * @param <T>
   *          target type
   * @return value
   * @throws REXPMismatchException
   *           on malformed expression
   * @throws IOException
   *           if unable to map
   */
  public <T> T mapExpression (REXP e) throws REXPMismatchException, IOException {
    REXPFactory f = new REXPFactory (e);
    byte[] b = new byte[f.getBinaryLength ()];
    f.getBinaryRepresentation (b, 0);
    return readValue (b);
  }
}

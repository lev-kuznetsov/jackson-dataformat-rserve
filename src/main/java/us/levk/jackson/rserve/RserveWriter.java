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

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.protocol.REXPFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationConfig;

/**
 * Rserve protocol writer
 * 
 * @author levk
 */
public class RserveWriter extends ObjectWriter {

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
   *          rootType
   * @param p
   *          pp
   */
  protected RserveWriter (ObjectMapper m, SerializationConfig c, JavaType t, PrettyPrinter p) {
    super (m, c, t, p);
  }

  /**
   * @param v
   *          value
   * @return r expression
   * @throws JsonProcessingException
   *           on databind failure
   * @throws REXPMismatchException
   *           on serialization vailure
   */
  public REXP mapValue (Object v) throws JsonProcessingException, REXPMismatchException {
    REXPFactory q = new REXPFactory ();
    q.parseREXP (writeValueAsBytes (v), 0);
    return q.getREXP ();
  }
}

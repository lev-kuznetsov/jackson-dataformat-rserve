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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;

/**
 * Rserve protocol mapper
 * 
 * @author levk
 */
public class RserveMapper extends ObjectMapper {

  /**
   * Serialization
   */
  private static final long serialVersionUID = 1L;

  /**
   * Default constructor
   */
  public RserveMapper () {
    this (new RserveFactory ());
  }

  /**
   * @param f
   *          factory
   */
  public RserveMapper (RserveFactory f) {
    super (f);
  }

  /**
   * @param f
   *          factory
   * @param p
   *          provider
   * @param c
   *          context
   */
  public RserveMapper (RserveFactory f, DefaultSerializerProvider p, DefaultDeserializationContext c) {
    super (f, p, c);
  }

  /**
   * Copy constructor
   * 
   * @param s
   *          source mapper
   */
  public RserveMapper (RserveMapper s) {
    super (s);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.databind.ObjectMapper#copy()
   */
  @Override
  public ObjectMapper copy () {
    return new RserveMapper (this);
  }
}

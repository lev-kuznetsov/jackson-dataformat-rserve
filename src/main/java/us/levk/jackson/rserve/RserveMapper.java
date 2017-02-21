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

import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
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

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.databind.ObjectMapper#_newWriter(com.fasterxml.
   * jackson.databind.SerializationConfig,
   * com.fasterxml.jackson.databind.JavaType,
   * com.fasterxml.jackson.core.PrettyPrinter)
   */
  @Override
  protected RserveWriter _newWriter (SerializationConfig config, JavaType rootType, PrettyPrinter pp) {
    return new RserveWriter (this, config, rootType, pp);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.databind.ObjectMapper#writerFor(java.lang.Class)
   */
  @Override
  public RserveWriter writerFor (Class <?> rootType) {
    return _newWriter (getSerializationConfig (), ((rootType == null) ? null : _typeFactory.constructType (rootType)),
                       /* PrettyPrinter */null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.databind.ObjectMapper#writerFor(com.fasterxml.jackson
   * .databind.JavaType)
   */
  @Override
  public RserveWriter writerFor (JavaType rootType) {
    return _newWriter (getSerializationConfig (), rootType, /* PrettyPrinter */null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.databind.ObjectMapper#writerFor(com.fasterxml.jackson
   * .core.type.TypeReference)
   */
  @Override
  public RserveWriter writerFor (TypeReference <?> rootType) {
    return _newWriter (getSerializationConfig (), ((rootType == null) ? null : _typeFactory.constructType (rootType)),
                       /* PrettyPrinter */null);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.databind.ObjectMapper#_newReader(com.fasterxml.
   * jackson.databind.DeserializationConfig,
   * com.fasterxml.jackson.databind.JavaType, java.lang.Object,
   * com.fasterxml.jackson.core.FormatSchema,
   * com.fasterxml.jackson.databind.InjectableValues)
   */
  @Override
  protected RserveReader _newReader (DeserializationConfig config, JavaType valueType, Object valueToUpdate,
                                     FormatSchema schema, InjectableValues injectableValues) {
    return new RserveReader (this, config, valueType, valueToUpdate, schema, injectableValues);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.fasterxml.jackson.databind.ObjectMapper#readerFor(java.lang.Class)
   */
  @Override
  public RserveReader readerFor (Class <?> type) {
    return _newReader (getDeserializationConfig (), _typeFactory.constructType (type), null, null, _injectableValues);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.databind.ObjectMapper#readerFor(com.fasterxml.jackson
   * .databind.JavaType)
   */
  @Override
  public RserveReader readerFor (JavaType type) {
    return _newReader (getDeserializationConfig (), type, null, null, _injectableValues);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.fasterxml.jackson.databind.ObjectMapper#readerFor(com.fasterxml.jackson
   * .core.type.TypeReference)
   */
  @Override
  public RserveReader readerFor (TypeReference <?> type) {
    return _newReader (getDeserializationConfig (), _typeFactory.constructType (type), null, null, _injectableValues);
  }
}

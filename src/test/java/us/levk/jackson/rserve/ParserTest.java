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

import static com.fasterxml.jackson.core.JsonToken.END_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.END_OBJECT;
import static com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
import static com.fasterxml.jackson.core.JsonToken.START_ARRAY;
import static com.fasterxml.jackson.core.JsonToken.START_OBJECT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_FLOAT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_INT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
import static com.fasterxml.jackson.core.JsonToken.VALUE_TRUE;
import static java.util.Base64.getDecoder;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.InputStream;

import org.junit.Test;

import com.fasterxml.jackson.core.io.IOContext;

public class ParserTest {

  @Test
  public void stringFoo () throws Exception {
    try (InputStream i = getClass ().getResourceAsStream ("/stringFoo.b64");
         RserveParser r =
             new RserveParser (new IOContext (null, null, false), 0, null, getDecoder ().decode (toByteArray (i)), 0)) {
      assertThat (r.nextToken (), is (VALUE_STRING));
      assertThat (r.getText (), is ("foo"));
    }
  }

  @Test
  public void int15 () throws Exception {
    try (InputStream i = getClass ().getResourceAsStream ("/int15.b64");
         RserveParser r =
             new RserveParser (new IOContext (null, null, false), 0, null, getDecoder ().decode (toByteArray (i)), 0)) {
      assertThat (r.nextToken (), is (VALUE_NUMBER_INT));
      assertThat (r.getIntValue (), is (15));
    }
  }

  @Test
  public void double15 () throws Exception {
    try (InputStream i = getClass ().getResourceAsStream ("/double15.b64");
         RserveParser r =
             new RserveParser (new IOContext (null, null, false), 0, null, getDecoder ().decode (toByteArray (i)), 0)) {
      assertThat (r.nextToken (), is (VALUE_NUMBER_FLOAT));
      assertThat (r.getDoubleValue (), is (15.0));
    }
  }

  @Test
  public void booleanTrue () throws Exception {
    try (InputStream i = getClass ().getResourceAsStream ("/booleanTrue.b64");
         RserveParser r =
             new RserveParser (new IOContext (null, null, false), 0, null, getDecoder ().decode (toByteArray (i)), 0)) {
      assertThat (r.nextToken (), is (VALUE_TRUE));
    }
  }

  @Test
  public void array5FooTrue () throws Exception {
    try (InputStream i = getClass ().getResourceAsStream ("/array5FooTrue.b64");
         RserveParser r =
             new RserveParser (new IOContext (null, null, false), 0, null, getDecoder ().decode (toByteArray (i)), 0)) {
      assertThat (r.nextToken (), is (START_ARRAY));
      assertThat (r.nextToken (), is (VALUE_NUMBER_INT));
      assertThat (r.getIntValue (), is (5));
      assertThat (r.nextToken (), is (VALUE_STRING));
      assertThat (r.getText (), is ("foo"));
      assertThat (r.nextToken (), is (VALUE_TRUE));
      assertThat (r.nextToken (), is (END_ARRAY));
    }
  }

  @Test
  public void hashHelloWorldFoo5 () throws Exception {
    try (InputStream i = getClass ().getResourceAsStream ("/hashHelloWorldFoo5.b64");
         RserveParser r =
             new RserveParser (new IOContext (null, null, false), 0, null, getDecoder ().decode (toByteArray (i)), 0)) {
      assertThat (r.nextToken (), is (START_OBJECT));
      assertThat (r.nextToken (), is (FIELD_NAME));
      assertThat (r.getText (), is ("hello"));
      assertThat (r.nextToken (), is (VALUE_STRING));
      assertThat (r.getText (), is ("world"));
      assertThat (r.nextToken (), is (FIELD_NAME));
      assertThat (r.getText (), is ("foo"));
      assertThat (r.nextToken (), is (VALUE_NUMBER_INT));
      assertThat (r.getIntValue (), is (5));
      assertThat (r.nextToken (), is (END_OBJECT));
    }
  }
}

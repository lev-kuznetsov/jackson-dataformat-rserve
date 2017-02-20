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

import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
import static java.util.Base64.getDecoder;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

public class FactoryTest {

  RserveFactory f;

  @Before
  public void setup () {
    f = new RserveFactory ();
  }

  @Test
  public void createParser () throws Exception {
    JsonParser p =
        f.createParser (getDecoder ().decode (toByteArray (getClass ().getResourceAsStream ("/stringFoo.b64"))));
    assertThat (p.nextToken (), is (VALUE_STRING));
    assertThat (p.getText (), is ("foo"));
  }

  @Test
  public void createGenerator () throws Exception {
    ByteArrayOutputStream o = new ByteArrayOutputStream ();
    JsonGenerator g = f.createGenerator (o);
    g.writeString ("foo");
    assertThat (o.toByteArray (),
                is (getDecoder ().decode (toByteArray (getClass ().getResourceAsStream ("/stringFoo.b64")))));
  }
}

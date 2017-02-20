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

import static java.util.Base64.getDecoder;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.rosuda.REngine.REXPMismatchException;

public class GeneratorTest {

  RserveGenerator g;
  ByteArrayOutputStream o;

  @Before
  public void setup () {
    o = new ByteArrayOutputStream ();
    g = new RserveGenerator (0, null, o);
  }

  byte[] load (String t) throws IOException {
    try (InputStream i = getClass ().getResourceAsStream ("/" + t + ".b64")) {
      return getDecoder ().decode (toByteArray (i));
    }
  }

  @Test
  public void stringFoo () throws REXPMismatchException, IOException {
    String s = "foo";
    g.writeString (s);
    assertThat (o.toByteArray (), is (load ("stringFoo")));
  }

  @Test
  public void int15 () throws REXPMismatchException, IOException {
    int i = 15;
    g.writeNumber (i);
    assertThat (o.toByteArray (), is (load ("int15")));
  }

  @Test
  public void double15 () throws REXPMismatchException, IOException {
    double i = 15;
    g.writeNumber (i);
    assertThat (o.toByteArray (), is (load ("double15")));
  }

  @Test
  public void float15 () throws REXPMismatchException, IOException {
    float i = 15;
    g.writeNumber (i);
    assertThat (o.toByteArray (), is (load ("double15")));
  }

  @Test
  public void booleanTrue () throws REXPMismatchException, IOException {
    boolean i = true;
    g.writeBoolean (i);
    assertThat (o.toByteArray (), is (load ("booleanTrue")));
  }

  @Test
  public void list () throws IOException, REXPMismatchException {
    g.writeStartArray ();
    g.writeNumber (5);
    g.writeString ("foo");
    g.writeBoolean (true);
    g.writeEndArray ();
    assertThat (o.toByteArray (), is (load ("array5FooTrue")));
  }

  @Test
  public void named () throws IOException, REXPMismatchException {
    g.writeStartObject ();
    g.writeFieldName ("hello");
    g.writeString ("world");
    g.writeFieldName ("foo");
    g.writeNumber (5);
    g.writeEndObject ();
    assertThat (o.toByteArray (), is (load ("hashHelloWorldFoo5")));
  }
}

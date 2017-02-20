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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.Rserve.protocol.REXPFactory;

public class DatabindTest {

  RserveMapper m;

  @Before
  public void setup () {
    m = new RserveMapper ();
  }

  @Test
  public void readStringFoo () throws Exception {
    REXPFactory f = new REXPFactory (new REXPString ("foo"));
    byte[] b = new byte[f.getBinaryLength ()];
    f.getBinaryRepresentation (b, 0);
    assertThat (m.readValue (b, String.class), is ("foo"));
  }

  @Test
  public void writeStringFoo () throws Exception {
    REXPFactory f = new REXPFactory ();
    f.parseREXP (m.writeValueAsBytes ("foo"), 0);
    assertThat (((REXPString) f.getREXP ()).asString (), is ("foo"));
  }

  @Test
  public void mapExpressionStringFoo () throws Exception {
    assertThat (m.readerFor (String.class).mapExpression (new REXPString ("foo")), is ("foo"));
  }

  @Test
  public void mapValueStringFoo () throws Exception {
    assertThat (m.writerFor (String.class).mapValue ("foo").asString (), is ("foo"));
  }
}

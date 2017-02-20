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
import static com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_FLOAT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_INT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
import static com.fasterxml.jackson.core.JsonToken.VALUE_TRUE;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static us.levk.jackson.rserve.ParserState.ARRAY_END;
import static us.levk.jackson.rserve.ParserState.ARRAY_START;
import static us.levk.jackson.rserve.ParserState.OBJECT_END;
import static us.levk.jackson.rserve.ParserState.OBJECT_START;
import static us.levk.jackson.rserve.ParserState.decimal;
import static us.levk.jackson.rserve.ParserState.field;
import static us.levk.jackson.rserve.ParserState.integer;
import static us.levk.jackson.rserve.ParserState.list;
import static us.levk.jackson.rserve.ParserState.logical;
import static us.levk.jackson.rserve.ParserState.parse;
import static us.levk.jackson.rserve.ParserState.string;

import org.junit.Test;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPGenericVector;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.RList;

public class ParserStateTest {

  @Test
  public void integer5 () {
    ParserState <?> s = ParserState.integer (5);
    assertThat (s.type, is (VALUE_NUMBER_INT));
    assertThat (s.body, is (5));
  }

  @Test
  public void decimal2point5 () {
    ParserState <?> s = ParserState.decimal (2.5);
    assertThat (s.type, is (VALUE_NUMBER_FLOAT));
    assertThat (s.body, is (2.5));
  }

  @Test
  public void logicalTrue () {
    ParserState <?> s = ParserState.logical (true);
    assertThat (s.type, is (VALUE_TRUE));
  }

  @Test
  public void stringFoo () {
    ParserState <?> s = ParserState.string ("foo");
    assertThat (s.type, is (VALUE_STRING));
    assertThat (s.body, is ("foo"));
  }

  @Test
  public void fieldFoo () {
    ParserState <?> s = ParserState.field ("foo");
    assertThat (s.type, is (FIELD_NAME));
    assertThat (s.body, is ("foo"));
  }

  @Test
  public void intExpr () {
    assertThat (integer (new REXPInteger (5)).collect (toList ()), is (asList (integer (5))));
  }

  @Test
  public void intArrayExpr () {
    assertThat (integer (new REXPInteger (new int[] { 1, 5 })).collect (toList ()),
                is (asList (ARRAY_START, integer (1), integer (5), ARRAY_END)));
  }

  @Test
  public void doubleExpr () {
    assertThat (ParserState.decimal (new REXPDouble (2.5)).collect (toList ()), is (asList (decimal (2.5))));
  }

  @Test
  public void doubleArrayExpr () {
    assertThat (decimal (new REXPDouble (new double[] { 1.5, 5.5 })).collect (toList ()),
                is (asList (ARRAY_START, decimal (1.5), decimal (5.5), ARRAY_END)));
  }

  @Test
  public void stringExpr () {
    assertThat (string (new REXPString ("foo")).collect (toList ()), is (asList (string ("foo"))));
  }

  @Test
  public void stringArrayExpr () {
    assertThat (string (new REXPString (new String[] { "foo", "bar" })).collect (toList ()),
                is (asList (ARRAY_START, string ("foo"), string ("bar"), ARRAY_END)));
  }

  @Test
  public void boolExpr () {
    assertThat (logical (new REXPLogical (true)).collect (toList ()), is (asList (logical (true))));
  }

  @Test
  public void boolArrayExpr () {
    assertThat (logical (new REXPLogical (new boolean[] { true, false })).collect (toList ()),
                is (asList (ARRAY_START, logical (true), logical (false), ARRAY_END)));
  }

  @Test
  public void listFooBar () {
    assertThat (list (new REXPGenericVector (new RList (asList (new REXPString ("foo"),
                                                                new REXPString ("bar"))))).collect (toList ()),
                is (asList (ARRAY_START, string ("foo"), string ("bar"), ARRAY_END)));
  }

  @Test
  public void hashHelloWorldFoo5 () {
    assertThat (list (new REXPGenericVector (new RList (asList (new REXPString ("world"), new REXPInteger (5)),
                                                        asList ("hello", "foo")))).collect (toList ()),
                is (asList (OBJECT_START, field ("hello"), string ("world"), field ("foo"), integer (5), OBJECT_END)));
  }

  @Test
  public void parseInt () {
    assertThat (parse (new REXPInteger (5)).findFirst ().get (), is (integer (5)));
  }

  @Test
  public void parseDouble () {
    assertThat (parse (new REXPDouble (2.5)).findFirst ().get (), is (decimal (2.5)));
  }

  @Test
  public void parseString () {
    assertThat (parse (new REXPString ("foo")).findFirst ().get (), is (string ("foo")));
  }

  @Test
  public void parseList () {
    assertThat (parse (new REXPGenericVector (new RList (asList (new REXPInteger (5))))).collect (toList ()),
                is (asList (ARRAY_START, integer (5), ARRAY_END)));
  }
}

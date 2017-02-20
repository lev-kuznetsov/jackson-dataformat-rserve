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
import static com.fasterxml.jackson.core.JsonToken.VALUE_FALSE;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NULL;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_FLOAT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_INT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
import static com.fasterxml.jackson.core.JsonToken.VALUE_TRUE;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static java.util.stream.Stream.builder;
import static java.util.stream.Stream.of;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPGenericVector;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.RList;

import com.fasterxml.jackson.core.JsonToken;

/**
 * Parser state
 * 
 * @author levk
 */
class ParserState <T> {

  /**
   * Token type
   */
  final JsonToken type;
  /**
   * Tokjen body
   */
  final T body;

  /**
   * @param t
   *          token type
   * @param b
   *          body
   */
  private ParserState (JsonToken t, T b) {
    type = t;
    body = b;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals (Object o) {
    return o instanceof ParserState && ((ParserState <?>) o).type == type
           && (((ParserState <?>) o).body == null ? (body == null) : ((ParserState <?>) o).body.equals (body));
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode () {
    return type.hashCode () ^ (body == null ? 0 : body.hashCode ());
  }

  /**
   * Null
   */
  static final ParserState <Void> NIL = new ParserState <Void> (VALUE_NULL, null);
  /**
   * Array start
   */
  static final ParserState <Void> ARRAY_START = new ParserState <Void> (START_ARRAY, null);
  /**
   * Object start
   */
  static final ParserState <Void> OBJECT_START = new ParserState <Void> (START_OBJECT, null);
  /**
   * Array end
   */
  static final ParserState <Void> ARRAY_END = new ParserState <Void> (END_ARRAY, null);
  /**
   * Object end
   */
  static final ParserState <Void> OBJECT_END = new ParserState <Void> (END_OBJECT, null);
  /**
   * True
   */
  static final ParserState <Boolean> TRUE = new ParserState <Boolean> (VALUE_TRUE, true);
  /**
   * False
   */
  static final ParserState <Boolean> FALSE = new ParserState <Boolean> (VALUE_FALSE, false);

  /**
   * @param v
   *          integer
   * @return parser state
   */
  static ParserState <Integer> integer (int v) {
    return new ParserState <Integer> (VALUE_NUMBER_INT, v);
  }

  /**
   * @param v
   *          decimal
   * @return parser state
   */
  static ParserState <Double> decimal (double v) {
    return new ParserState <Double> (VALUE_NUMBER_FLOAT, v);
  }

  /**
   * @param v
   *          string
   * @return parser state
   */
  static ParserState <String> string (String v) {
    return new ParserState <String> (VALUE_STRING, v);
  }

  /**
   * @param v
   *          logical
   * @return parser state
   */
  static ParserState <Boolean> logical (boolean v) {
    return v ? TRUE : FALSE;
  }

  /**
   * @param s
   *          stream
   * @return array boxed stream if there is more than one element
   */
  private static Stream <ParserState <?>> box (Stream <ParserState <?>> s) {
    List <ParserState <?>> l = s.collect (toList ());
    if (l.size () > 1) {
      l.add (0, ARRAY_START);
      l.add (ARRAY_END);
    }
    return l.stream ();
  }

  /**
   * @param v
   *          integer expression
   * @return state stream
   */
  static Stream <ParserState <?>> integer (REXPInteger v) {
    int[] i = v.asIntegers ();
    boolean[] n = v.isNA ();
    return box (range (0, i.length).mapToObj (c -> n[c] ? NIL : integer (i[c])));
  }

  /**
   * @param v
   *          decimal expression
   * @return state stream
   */
  static Stream <ParserState <?>> decimal (REXPDouble v) {
    double[] d = v.asDoubles ();
    boolean[] n = v.isNA ();
    return box (range (0, d.length).mapToObj (c -> n[c] ? NIL : decimal (d[c])));
  }

  /**
   * @param v
   *          string expression
   * @return state stream
   */
  static Stream <ParserState <?>> string (REXPString v) {
    String[] s = v.asStrings ();
    boolean[] n = v.isNA ();
    return box (range (0, s.length).mapToObj (c -> n[c] ? NIL : string (s[c])));
  }

  /**
   * @param v
   *          logical expression
   * @return state stream
   */
  static Stream <ParserState <?>> logical (REXPLogical v) {
    boolean[] b = v.isTRUE ();
    boolean[] n = v.isNA ();
    return box (range (0, b.length).mapToObj (c -> n[c] ? NIL : logical (b[c])));
  }

  /**
   * @param n
   *          name
   * @return field name
   */
  static ParserState <?> field (String n) {
    return new ParserState <String> (FIELD_NAME, n);
  }

  /**
   * @param v
   *          list expression
   * @return state stream
   */
  static Stream <ParserState <?>> list (REXPGenericVector v) {
    RList l = v.asList ();
    if (l.isNamed ()) {
      Builder <ParserState <?>> s = builder ();
      return range (0, l.size ()).boxed ().reduce (s.add (OBJECT_START), (b, c) -> {
        return parse (l.at (c)).reduce (b.add (field (l.keyAt (c))), (p, i) -> {
          return p.add (i);
        }, (x, y) -> {
          throw new UnsupportedOperationException ();
        });
      }, (x, y) -> {
        throw new UnsupportedOperationException ();
      }).add (OBJECT_END).build ();
    } else return of (of (ARRAY_START), range (0, l.size ()).mapToObj (c -> parse (l.at (c))).flatMap (x -> x),
                      of (ARRAY_END)).flatMap (x -> x);
  }

  /**
   * @param v
   *          expression
   * @return state stream
   */
  static Stream <ParserState <?>> parse (REXP v) {
    if (v instanceof REXPInteger) return integer ((REXPInteger) v);
    else if (v instanceof REXPDouble) return decimal ((REXPDouble) v);
    else if (v instanceof REXPString) return string ((REXPString) v);
    else if (v instanceof REXPLogical) return logical ((REXPLogical) v);
    else if (v instanceof REXPGenericVector) return list ((REXPGenericVector) v);
    else throw new IllegalArgumentException ("Cannot process REXP of type " + v.getClass ().getSimpleName () + ": "
                                             + v.toDebugString ());
  }
}

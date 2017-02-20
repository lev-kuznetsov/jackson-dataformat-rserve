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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.protocol.REXPFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

public class E2e {

  public static class Book {
    @JsonProperty String author;
    @JsonProperty String title;
    @JsonProperty int year;
    @JsonProperty double price;
  }

  public static class Store {
    @JsonProperty Book[] books;
  }

  RConnection r;

  @Before
  public void connect () throws Exception {
    r = new RConnection ();
  }

  @After
  public void close () throws Exception {
    r.close ();
  }

  @Test
  public void readValue () throws Exception {
    REXP e = r.eval ("list(books=list(list(author='Stephen King', title='End of Watch', year=2016, price=10.0),"
                     + "list(author='John F. Kennedy', title='Profiles in Courage', year=1955, price=12.5)))");
    REXPFactory f = new REXPFactory (e);
    byte[] b = new byte[f.getBinaryLength ()];
    f.getBinaryRepresentation (b, 0);

    RserveMapper m = new RserveMapper ();
    Store s = m.readValue (b, Store.class);
    assertThat (s.books.length, is (2));
    assertThat (s.books[0].author, is ("Stephen King"));
    assertThat (s.books[0].title, is ("End of Watch"));
    assertThat (s.books[0].year, is (2016));
    assertThat (s.books[0].price, is (10.0));
    assertThat (s.books[1].author, is ("John F. Kennedy"));
    assertThat (s.books[1].title, is ("Profiles in Courage"));
    assertThat (s.books[1].year, is (1955));
    assertThat (s.books[1].price, is (12.5));
  }
}

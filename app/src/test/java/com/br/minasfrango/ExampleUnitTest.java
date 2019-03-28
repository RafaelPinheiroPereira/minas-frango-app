package com.br.minasfrango;

import com.br.minasfrango.util.FormatacaoMoeda;
import java.text.ParseException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
		@Test
		public void addition_isCorrect() {
			try {
				FormatacaoMoeda.converteStringDoubleValorMoeda("12.346,34");
				assertEquals(0,0);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
}
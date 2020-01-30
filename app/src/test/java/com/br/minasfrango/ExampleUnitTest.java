package com.br.minasfrango;

import static org.junit.Assert.*;

import com.br.minasfrango.util.ArquivoUtils;
import com.br.minasfrango.util.ConstantsUtil;
import org.junit.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {


		@Test
        public void lerFotos() {

            ArquivoUtils arquivoUtils = new ArquivoUtils();
           assertEquals(3, arquivoUtils.lerFotosDoDiretorio(ConstantsUtil.CAMINHO_IMAGEM_VENDAS).length);


        }


}
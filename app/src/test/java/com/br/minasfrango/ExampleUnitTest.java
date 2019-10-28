package com.br.minasfrango;

import static org.junit.Assert.*;

import com.br.minasfrango.data.dao.PrecoDAO;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import org.junit.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {

    PrecoDAO mPrecoDAO;

    private Realm realm;

    //Aprender a usar realm no test e talvez acabar com a super classe

		@Test
        public void loadAllPrices() {

            assertTrue(mPrecoDAO.getAll().size() > 0);


        }

    @Before
    public void setUp() throws Exception {
        RealmConfiguration config =
                new RealmConfiguration.Builder().inMemory().name("test-realm").build();
        realm = Realm.getInstance(config);


    }
}
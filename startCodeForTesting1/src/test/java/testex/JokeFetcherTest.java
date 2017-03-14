/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testex;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.BDDMockito.given;
import org.mockito.Mock;
import testex.jokefetching.ChuckNorris;
import testex.jokefetching.EduJoke;
import testex.jokefetching.FetcherFactory;
import testex.jokefetching.IFetcherFactory;
import testex.jokefetching.IJokeFetcher;
import testex.jokefetching.Moma;
import testex.jokefetching.Tambal;

/**
 *
 * @author Kasper
 */
@RunWith(MockitoJUnitRunner.class)
public class JokeFetcherTest {

    private JokeFetcher jokeFetcher;
    @Mock IDateFormatter ifMock;
    @Mock IFetcherFactory factory;
    @Mock Moma moma;
    @Mock ChuckNorris chuck;
    @Mock EduJoke edu;
    @Mock Tambal tambal;

    public JokeFetcherTest() {

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        List<IJokeFetcher> fetchers = Arrays.asList(edu, chuck,moma,tambal);
        when(factory.getJokeFetchers("EduJoke,ChuckNorris,Moma,Tambal")).thenReturn(fetchers);
        List<String> types = Arrays.asList("EduJoke","ChuckNorris","Moma","Tambal");
        when(factory.getAvailableTypes()).thenReturn(types);
        jokeFetcher = new JokeFetcher (ifMock, factory);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getAvailableTypes method, of class JokeFetcher. //
     */
    @Test
    public void testGetAvailableTypes() {
        System.out.println("getAvailableTypes");
        List<String> result = jokeFetcher.getAvailableTypes();
        assertThat(result, hasItems("eduprog","chucknorris","moma","tambal"));
    }
//    
    @Test
    public void testCheckIfValidString(){
        String token = "moma";
//        String token = "false";

        boolean result = jokeFetcher.isStringValid(token);
        assertEquals(true, result);
    }
    
    @Test
    public void testGetJokesDateFormatterCalls() throws Exception{
        
        given(ifMock.getFormattedDate(eq("Europe/Copenhagen"), anyObject())).willReturn("17 feb. 2017 10:56 AM");
        Date time = new Date();
        jokeFetcher.getJokes("eduprog,chucknorris,moma,tambal", "Europe/Copenhagen");
        verify(ifMock, times(1)).getFormattedDate("Europe/Copenhagen", time);
    }
    
    @Test
    public void testGetJoke() throws Exception{
        given(edu.getJoke()).willReturn(new Joke("Test joke", "bah"));
        given(chuck.getJoke()).willReturn(new Joke("Test joke", "bah"));
        given(moma.getJoke()).willReturn(new Joke("Test joke", "bah"));
        given(tambal.getJoke()).willReturn(new Joke("Test joke", "bah"));
        
        Jokes jokes = jokeFetcher.getJokes("eduprog,chucknorris,moma,tambal", "Europe/Copenhagen");
        jokes.getJokes().forEach((joke) -> {
            assertThat(joke.getJoke(), is("Test joke"));
            assertThat(joke.getReference(), is("bah"));
        });
    }
    
    @Test
    public void testGetJokesFetcherFactoryCalls() throws Exception{
        jokeFetcher.getJokes("eduprog,chucknorris,moma,tambal", "Europe/Copenhagen");
        verify(factory, times(1)).getJokeFetchers("eduprog,chucknorris,moma,tambal");
    }
    @Test
    public void testFactoryGetAvailableTypes() {
        FetcherFactory ff = new FetcherFactory();
        List<String> result = ff.getAvailableTypes();
        assertThat(result, hasItems("eduprog", "chucknorris", "moma", "tambal"));
    }

    @Test
    public void testFactoryObjectsSize() {
        FetcherFactory ff = new FetcherFactory();
        List<IJokeFetcher> result = ff.getJokeFetchers("eduprog,chucknorris,moma,tambal");
        assertThat(result.size(), is(4));
    }

    @Test
    public void testFactoryObjectsType() {
        FetcherFactory ff = new FetcherFactory();
        List<IJokeFetcher> result = ff.getJokeFetchers("eduprog,chucknorris,moma,tambal");
        assertThat(result.get(0), instanceOf(EduJoke.class));
        assertThat(result.get(1), instanceOf(ChuckNorris.class));
        assertThat(result.get(2), instanceOf(Moma.class));
        assertThat(result.get(3), instanceOf(Tambal.class));
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testex.jokefetching;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Kasper
 */
public class FetcherFactory implements IFetcherFactory{

    private final List<String> availableTypes = 
        Arrays.asList("eduprog", "chucknorris", "moma", "tambal");

  @Override
  public List<String> getAvailableTypes(){ return availableTypes;}
  
  @Override
  public List<IJokeFetcher> getJokeFetchers(String jokesToFetch) {
    //This is for you to do, but wait
    String[] jokes = jokesToFetch.split(",");
    List<IJokeFetcher> result = new ArrayList();
      for (String joke : jokes) {
          switch(joke){
              case "eduprog" : result.add(new EduJoke()); break;
              case "chucknorris" : result.add(new ChuckNorris()); break;
              case "moma" : result.add(new Moma()); break;
              case "tambal" : result.add(new Tambal()); break;
          }
      }
    return result;   
  }
    
}

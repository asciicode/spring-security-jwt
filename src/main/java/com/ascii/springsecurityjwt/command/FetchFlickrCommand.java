package com.ascii.springsecurityjwt.command;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/*
 * 
 * This class makes an async call to flickr
 * 
 */
public class FetchFlickrCommand
    implements
    Callable<ResponseEntity<String>>
{
  private RestTemplate restTemplate;

  private String searchTags;

  public FetchFlickrCommand(RestTemplate restTemplate, String searchTags)
  {
    this.restTemplate = restTemplate;
    this.searchTags = searchTags;
  }
  @Override
  public ResponseEntity<String> call()
    throws Exception
  {
    String urlTemplate = UriComponentsBuilder.fromHttpUrl("https://www.flickr.com/services/feeds/photos_public.gne")
        .queryParam("tags", "{tags}")
        .queryParam("format", "{format}")
        .queryParam("nojsoncallback", "{nojsoncallback}")
        .encode()
        .toUriString();

    Map<String, String> params = new HashMap<>();
    params.put("tags", searchTags);
    params.put("format", "json");
    params.put("nojsoncallback", "1");

    ResponseEntity<String> response = restTemplate.getForEntity(urlTemplate, String.class, params);
    return new ResponseEntity<String>(response.getBody(), HttpStatus.OK);
  }

}

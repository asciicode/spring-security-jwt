package com.ascii.springsecurityjwt.controllers;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ascii.springsecurityjwt.MyUserDetailsService;
import com.ascii.springsecurityjwt.command.FetchFlickrCommand;
import com.ascii.springsecurityjwt.models.AuthenticationRequest;
import com.ascii.springsecurityjwt.models.AuthenticationResponse;
import com.ascii.springsecurityjwt.services.FirestoreService;
import com.ascii.springsecurityjwt.util.JwtUtil;

/*
 * 
 * Main controller
 * 
 */
@RestController
@CrossOrigin(origins = { "${settings.cors_origin}" })
public class MonashController
{

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtUtil jwtTokenUtil;

  @Autowired
  private MyUserDetailsService userDetailsService;

  @Autowired
  private FirestoreService firestoreService;

  @Bean
  public RestTemplate restTemplate()
  {
    return new RestTemplate();
  }

  @GetMapping(value = "/flickr", produces = "application/json")
  public Callable<ResponseEntity<String>> flickr(@RequestParam String tags)
    throws IOException
  {
    return new FetchFlickrCommand(restTemplate(), tags);
  }

  @RequestMapping(value = "/authenticate/login", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
    throws Exception
  {

    try
    {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
              authenticationRequest.getPassword()));
    }
    catch (BadCredentialsException e)
    {
      throw new Exception("Incorrect username or password", e);
    }

    final UserDetails userDetails = userDetailsService
        .loadUserByUsername(authenticationRequest.getUsername());

    final String jwt = jwtTokenUtil.generateToken(userDetails);
    // username save to firebase
    firestoreService.saveUser(authenticationRequest.getUsername());
    return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }

}

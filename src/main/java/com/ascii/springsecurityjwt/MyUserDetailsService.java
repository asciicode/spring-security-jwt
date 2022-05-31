package com.ascii.springsecurityjwt;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService
    implements
    UserDetailsService
{

  @Override
  public UserDetails loadUserByUsername(String s)
    throws UsernameNotFoundException
  {
    // TODO can be config in property file
    return new User("monash", "welcomeallen",
        new ArrayList<>());
  }
}

package com.ascii.springsecurityjwt.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascii.springsecurityjwt.entity.User;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;

/*
 * 
 * This service class check and save to firestore
 * 
 */
@Service
public class FirestoreService
{
  @Autowired
  Firestore firestore;

  private static final String USER = "User";

  public void saveUser(String username)
  {
    try
    {
      ApiFuture<QuerySnapshot> documentSnapshotApiFuture = this.firestore.collection(USER)
          .whereEqualTo("username", username).get();
      if (documentSnapshotApiFuture.get().getDocuments().isEmpty())
      {
        User user = new User();
        user.setUsername(username);
        this.firestore.collection(USER)
            .document(UUID.randomUUID().toString()).set(user).get();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}

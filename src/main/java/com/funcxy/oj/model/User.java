package com.funcxy.oj.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by aak12 on 2017/2/28.
 */
@Document(collection = "users")
public class User {
    @Id
    private ObjectId id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
//    public void setId(int id){this.id=id;}
    public void setUsername(String username){this.username = new String(username);}
//    public void setPassword(String password){this.password = new String(password);}
    public String getUsername(){return this.username;}
    public void setPassword(String password){this.password = new String(password);}
    public String getPassword(){return this.password;}
    public void setEmail(String email){this.email = email;}
    public String getEmail(){return this.email;}
}

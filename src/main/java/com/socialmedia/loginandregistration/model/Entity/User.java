package com.socialmedia.loginandregistration.model.Entity;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Unwrapped;
import org.springframework.data.rest.core.annotation.RestResource;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "User")
public class User {
    @Id
    protected  ObjectId _id;
    @NotBlank
    @Size(max = 20)
    protected  String username;
    @NotBlank
    @Size(max = 50)
    @Email
    protected  String email;
    @NotBlank
    @Size(max = 120)
    protected  String password;
    @NotBlank
    @Size(max = 20)
    protected  String firstname;
    @NotBlank
    @Size(max = 20)
    protected  String lastname;
    protected  String status;
    @DBRef
//    @Unwrapped(onEmpty = Unwrapped.OnEmpty.USE_NULL)
    protected  Set<Role> roles = new HashSet<>();

    public User() {
    }
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(ObjectId _id, String username, String email, String password, String firstname, String lastname, String status, Set<Role> roles) {
        this._id = _id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.status = status;
        this.roles = roles;
    }

    public ObjectId getId() {
        return _id;
    }
    public void setId(ObjectId id) {
        this._id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
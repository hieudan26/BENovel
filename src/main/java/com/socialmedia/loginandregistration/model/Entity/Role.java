package com.socialmedia.loginandregistration.model.Entity;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "Role")
public class Role {
    @Id
    protected ObjectId _id;
    protected String name;
    public Role() {
    }

    public Role(ObjectId _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public Role(String name) {
        this.name = name;
    }
    public ObjectId getId() {
        return _id;
    }
    public void setId(ObjectId id) {
        this._id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
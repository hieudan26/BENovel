package com.socialmedia.loginandregistration.model.Entity;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(exported=false)
@Document(collection = "ChapterCollection")
public class Chapter {
    @Id
    protected ObjectId _id;
    protected int chap_number;
    protected String content;
    protected ObjectId dautruyenId;
    protected String tenchap;

    public Chapter() {
    }

    public Chapter(ObjectId _id, int chap_number, String content, ObjectId dautruyenId, String tenchap) {
        this._id = _id;
        this.chap_number = chap_number;
        this.content = content;
        this.dautruyenId = dautruyenId;
        this.tenchap = tenchap;
    }

    public ObjectId getId() {
        return _id;
    }
    public void setId(ObjectId id) {
        this._id = id;
    }

    public int getChap_number() {
        return chap_number;
    }

    public void setChap_number(int chap_number) {
        this.chap_number = chap_number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ObjectId getDautruyenId() {
        return dautruyenId;
    }

    public void setDautruyenId(ObjectId dautruyenId) {
        this.dautruyenId = dautruyenId;
    }

    public String getTenchap() {
        return tenchap;
    }

    public void setTenchap(String tenchap) {
        this.tenchap = tenchap;
    }
}

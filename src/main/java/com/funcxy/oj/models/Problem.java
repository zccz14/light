package com.funcxy.oj.models;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by wtupc96 on 2017/2/28.
 */

@Document(collection = "problem")
public class Problem {
    @Id
    @NotNull
    private ObjectId id;

    @Indexed
    @NotNull
    @DBRef(lazy = true)
    private User creator;

    @Indexed
    @NotNull
    @NotBlank
    private String title;

    @Indexed
    @NotBlank
    @NotNull
    private String type;

    @NotNull
    @NotBlank
    private String description;

    private String referenceAnswer;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferenceAnswer() {
        return referenceAnswer;
    }

    public void setReferenceAnswer(String referenceAnswer) {
        this.referenceAnswer = referenceAnswer;
    }

    @Override
    public boolean equals(Object obj) {
        if(this.getId().equals(((Problem)obj).getId()))
            return true;
        else
            return false;
    }
}

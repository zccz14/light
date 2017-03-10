package com.funcxy.oj.models;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by wtupc96 on 2017/2/28.
 *
 * @author Peter
 * @version 1.0
 */

@Document(collection = "problems")
public class Problem {
    @Id
    private String id;

    @Indexed
    private String creator;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type){
        this.type = type.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description.trim();
    }

    public String getReferenceAnswer() {
        return referenceAnswer;
    }

    public void setReferenceAnswer(String referenceAnswer) {
        this.referenceAnswer = referenceAnswer.trim();
    }

    @Override
    public boolean equals(Object obj) {
        if(this.getId() != null)
            return this.getId().equals(((Problem)obj).getId());
        else
            return super.equals(obj);
    }
}

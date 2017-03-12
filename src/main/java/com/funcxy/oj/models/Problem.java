package com.funcxy.oj.models;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * 题目模型类
 *
 * @author Peter
 * @version 1.0
 */

@Document(collection = "problems")
public class Problem extends Model {
    /**
     * 创建者 ID
     */
    @Indexed
    private String creator;
    /**
     * 题目标题
     */
    @Indexed
    @NotNull
    @NotBlank
    private String title;
    /**
     * 题目类型
     */
    @Indexed
    @NotBlank
    @NotNull
    private String type;
    /**
     * 题面
     */
    @NotNull
    @NotBlank
    private String description;
    /**
     * 参考答案
     */
    private String referenceAnswer;

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

    public void setType(String type) {
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
        if (this.getId() != null)
            return this.getId().equals(((Problem) obj).getId());
        else
            return super.equals(obj);
    }
}

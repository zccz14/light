package com.funcxy.oj.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by aak12 on 2017/3/4.
 */
@Document(collection = "groups")
public class Group {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;

    @Indexed
    @NotNull
    private ObjectId ownerId;
    @Indexed
    private String name;

    private List<ObjectId> memberId;
    private List<ObjectId> ownedProblemList;
    private GroupType type;
}

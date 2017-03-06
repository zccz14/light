package com.funcxy.oj.controllers;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by aak12 on 2017/3/4.
 */
@RestController
@RequestMapping("/groups")
public class GroupController {
    @RequestMapping(value = "/creat",method = POST)
    public ResponseEntity<Object> createGroup(){
        return null;
    }
}

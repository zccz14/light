package com.funcxy.oj;


import com.mongodb.DB;
import com.mongodb.Mongo;

import java.util.Set;

/**
 * Created by aak12 on 2017/2/28.
 */
public class TestMongodb {
    public  static  void  main(String args[]){
        Mongo mongo = new Mongo("127.0.0.1",27017);
        DB db = mongo.getDB("orange-juice");
        Set<String> collectionNames = db.getCollectionNames();
        for(String names: collectionNames){
            System.out.print(names);
        }
    }
}

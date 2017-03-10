package com.funcxy.oj.models;

/**
 * @author  aak1247 on 2017/3/10.
 */
public class Dispatcher {
    private String url;
    private String type;
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(String url){
        return this.url;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
}

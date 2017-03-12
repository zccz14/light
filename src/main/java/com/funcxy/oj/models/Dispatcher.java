package com.funcxy.oj.models;

/**
 * 代理
 *
 * @author aak1247 on 2017/3/10.
 */
class Dispatcher {
    /**
     * 代理地址
     */
    private String url;
    /**
     * 代理类型
     */
    private String type; // TODO: use enum

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

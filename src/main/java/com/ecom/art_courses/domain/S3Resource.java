package com.ecom.art_courses.domain;

public class S3Resource {

    private String key;
    private String body;

    public S3Resource(String key) {
        this.key = key;
    }

    public S3Resource(String key, String body) {
        this.key = key;
        this.body = body;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "S3Resource{" + "key='" + key + '\'' + ", body='" + body + '\'' + '}';
    }
}

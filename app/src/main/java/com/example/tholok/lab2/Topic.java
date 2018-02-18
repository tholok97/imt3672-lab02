package com.example.tholok.lab2;

/**
 * Created by tholok on 18.02.18.
 */

public class Topic {

    private int _id;
    private String _topic;
    private String _link;

    public Topic() {

    }

    public Topic(String _topic, String _link) {
        this._topic = _topic;
        this._link = _link;
    }

    public String toString() {
        return this.get_topic();
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_topic(String _topic) {
        this._topic = _topic;
    }

    public void set_link(String _link) {
        this._link = _link;
    }

    public int get_id() {
        return _id;
    }

    public String get_topic() {
        return _topic;
    }

    public String get_link() {
        return _link;
    }
}

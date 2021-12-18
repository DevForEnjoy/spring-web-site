package com.example.demo.domain;

import java.util.Date;

public class messShow extends Message {

    private String author;
    private String hostSh;


    public messShow(String author,String hostsh, Long sender, Long host, Boolean Doc, String text, Date d) {
        super(sender,host,Doc,text,d);
        this.author = author;
        this.hostSh = hostsh;

    }
    public messShow(String author,String hostsh,Message m) {

        super(m.getSender(),m.getHost(),m.isDoc(),m.getText(),m.getDate());
        this.author = author;
        this.hostSh = hostsh;
        this.setFilename(m.getFilename());

    }
    public messShow() {

    }
    public void setAuthor(String s){
        this.author =s;
    }
    public String getAuthor(){
        return this.author;
    }


    public void setHostSh(String s){
        this.hostSh =s;
    }
    public String getHostSh(){
        return this.hostSh;
    }
}

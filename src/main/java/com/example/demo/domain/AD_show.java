package com.example.demo.domain;

import java.util.Date;

public class AD_show  extends AD{
    private String author;
    private String hostSh;


    public AD_show(String author,String hostsh, Long sender, Long host, Boolean Doc, String text, Date d) {
        super(sender,Doc,text,d);
        this.author = author;
        this.hostSh = hostsh;

    }
    public AD_show(String author,AD m) {

        super(m.getSender(),m.isDoc(),m.getText(),m.getDate());
        this.author = author;
        this.setFilename(m.getFilename());
        this.setId(m.getId());

    }
    public AD_show() {

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

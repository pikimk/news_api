package com.example.newsapi.demo;
import jakarta.persistence.*;

@Entity
public class NewsObject {

    NewsObject(String title, String description, String url, String date){
        this.date = date;
        this.description = description;
        this.url = url;
        this.title = title;
    }

    NewsObject(){}      //default constructor, otherwise @Entity wont work

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")            // Description is long text value
    private String description;
    private String url;
    private String date;

    public String getTitle(){
        return title;
    }
    //no other getters and setters needed

}

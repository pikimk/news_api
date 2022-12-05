package com.example.newsapi.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;

    public NewsController(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    //Get all records
    @QueryMapping
    Iterable<NewsObject> news(){
        return newsRepository.findAll();
    };

    //get by id
    @QueryMapping
     NewsObject getById(@Argument("id") Long id){
        return newsRepository.findById(id).orElse(null);
    }

    // search by title
    @QueryMapping
        Iterable<NewsObject> getByTitle(@Argument("contains") String contains){
        List<NewsObject> newsList = new ArrayList<>();
        newsRepository.findAll().forEach(newsList::add);
        List<NewsObject> filtered = newsList.stream().filter(newsObject -> newsObject.getTitle().contains(contains)).toList();
        System.out.println(filtered);
        return filtered;
    };

}

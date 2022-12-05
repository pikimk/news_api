package com.example.newsapi.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;


    public List<NewsObject> getAllNews(){
        List<NewsObject> news= new ArrayList<>();
        newsRepository.findAll().forEach(news::add);
        System.out.println("Trigered");
        return news;
    }

    public void save(NewsObject news){
        newsRepository.save(news);
    }


    public void saveAdjacent(List<NewsObject> news){
        //find all in db
        Iterable<NewsObject> currentNews = newsRepository.findAll();

        //put in map
        Map<String,NewsObject> newsMap = new HashMap<>();
        Map<String,NewsObject> existingMap = new HashMap<>();
        currentNews.forEach(newsObject -> {
            newsMap.put(newsObject.getTitle(),newsObject);
        });

        //divide existing and non existing
        List<NewsObject> existing = new ArrayList<>();
        List<NewsObject> notExsiting = new ArrayList<>();
        List<NewsObject> toDelete = new ArrayList<>();
        news.forEach(newsObject -> {
            if(newsMap.containsKey(newsObject.getTitle())){
                existing.add(newsObject);
            }else{
                notExsiting.add(newsObject);
            }
        });

        //put existing in map
        existing.forEach(newsObject -> {
            existingMap.put(newsObject.getTitle(),newsObject);
        });

        //for each news in db, check if title doesnd exist, and create list of deletion
        currentNews.forEach(newsObject -> {
            if(!existingMap.containsKey(newsObject.getTitle())){
                toDelete.add(newsObject);
            }
        });

        //first delete all not existant
        newsRepository.deleteAll(toDelete);
        //save ones that dont exist
        newsRepository.saveAll(notExsiting);
    }

    //clear database, and save all records (not in use)
    public void clearDBAndSaveAll(List<NewsObject> news){
        newsRepository.deleteAll();
        newsRepository.saveAll(news);
    }


}

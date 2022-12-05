package com.example.newsapi.demo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface NewsRepository extends CrudRepository<NewsObject, Long > {

}

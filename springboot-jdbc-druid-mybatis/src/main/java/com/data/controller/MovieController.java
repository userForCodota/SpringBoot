package com.data.controller;

import com.data.mapper.MovieMapper;
import com.data.pojo.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {
    @Autowired
    MovieMapper movieMapper;

    @RequestMapping("/all")
    public List<Movie> queryMovies() {
        List<Movie> movies = movieMapper.queryMovies();
        return movies;
    }
}

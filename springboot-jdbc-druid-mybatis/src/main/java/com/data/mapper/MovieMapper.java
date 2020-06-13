package com.data.mapper;

import com.data.pojo.Movie;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MovieMapper {
    //增删改查
    @Select("SELECT id,title,time FROM movies LIMIT 0,10")
    List<Movie> queryMovies();
}

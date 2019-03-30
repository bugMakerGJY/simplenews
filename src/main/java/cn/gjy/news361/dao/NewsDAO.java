package cn.gjy.news361.dao;
  
import cn.gjy.news361.pojo.News;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;


public interface NewsDAO extends JpaRepository<News,Integer>{
    Page<News> findNewsByTag(String tag, Pageable pageable);
 
}
package cn.gjy.news361.service;


import cn.gjy.news361.dao.NewsDAO;
import cn.gjy.news361.pojo.News;
import cn.gjy.news361.pojo.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewsService {
    @Autowired
    NewsDAO newsDAO;
    @Autowired
    TagService tagService;

    public Page<News> list(int start, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        return newsDAO.findAll(pageable);
    }

    public List<News> list() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        return newsDAO.findAll();
    }

    public News findOneNews(int id) {
        return newsDAO.findOne(id);
    }

    public List<News> listByTag(int start, int size, String openId) {
        List<News> allNews = new ArrayList<>();
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        List<Tag> choosTagstag = tagService.GetChooseTags(openId);
        if (choosTagstag.size() == 0) {
            return list(start, size).getContent();
        } else {
            for (Tag tag : choosTagstag) {
                Page<News> newsList = newsDAO.findNewsByTag(tag.getName(), pageable);
                allNews.addAll(newsList.getContent());
            }

            return allNews;
        }

    }
}
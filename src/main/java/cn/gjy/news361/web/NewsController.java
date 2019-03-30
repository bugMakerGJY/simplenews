package cn.gjy.news361.web;


import cn.gjy.news361.pojo.News;
import cn.gjy.news361.service.NewsService;
import cn.gjy.news361.service.PicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NewsController {
    @Autowired
    NewsService newsService;
    @Autowired
    PicService picService;

    @GetMapping("/news")
    public Page<News> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start < 0 ? 0 : start;
        Page<News> page = newsService.list(start, size);
        picService.setFirstPics(page.getContent());
        return page;
    }

    @GetMapping("/news/{id}")
    public News findOneNews(@PathVariable("id") int id) {
        News news = newsService.findOneNews(id);
        picService.setNewsImages(news);
        return news;
    }

    @GetMapping("/mynews")
    public List<News> findOneNews(int start, int size, String openId) {
        List<News> newsList = newsService.listByTag(start, size, openId);
        picService.setFirstPics(newsList);
        return newsList;
    }
}

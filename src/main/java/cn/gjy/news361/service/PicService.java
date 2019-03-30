package cn.gjy.news361.service;

import cn.gjy.news361.dao.PicDAO;
import cn.gjy.news361.pojo.News;
import cn.gjy.news361.pojo.Pic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PicService {
    @Autowired
    PicDAO picDAO;

    public List<Pic> getPicByNews(News news){
        return picDAO.findPicByNewsOrderByIdDesc(news);
    }

    public void setFirstPic(News news){
        List<Pic> picList=getPicByNews(news);
        if(!picList.isEmpty())
            news.setFirstNewsImage(picList.get(0));
        else
            news.setFirstNewsImage(new Pic());
    }

    public void setFirstPics(List<News> list){
        for(News news:list){
            setFirstPic(news);
        }
    }

    public void setNewsImages(News news){
        List<Pic> picList=getPicByNews(news);
        news.setNewsImages(picList);
    }
}

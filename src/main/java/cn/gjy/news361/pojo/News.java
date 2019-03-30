package cn.gjy.news361.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.util.List;

@Entity
@Table(name = "news")
@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })

public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    int id;

    String title;


    String tag;

    String article;

    String source;

    String time;
    @Transient
    private Pic firstNewsImage;

    @Transient
    private List<Pic> NewsImages;

    public String getTitle() {
        return title;
    }

    public List<Pic> getNewsImages() {
        return NewsImages;
    }

    public void setNewsImages(List<Pic> newsImages) {
        NewsImages = newsImages;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Pic getFirstNewsImage() {
        return firstNewsImage;
    }

    public void setFirstNewsImage(Pic firstNewsImage) {
        this.firstNewsImage = firstNewsImage;
    }

}
package cn.gjy.news361.dao;

import cn.gjy.news361.pojo.News;
import cn.gjy.news361.pojo.Pic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PicDAO extends JpaRepository<Pic,Integer> {
    public List<Pic> findPicByNewsOrderByIdDesc(News news);
}

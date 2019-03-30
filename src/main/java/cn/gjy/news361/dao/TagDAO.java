package cn.gjy.news361.dao;

import cn.gjy.news361.pojo.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagDAO extends JpaRepository<Tag,Integer> {
    Tag findById(int id);
}

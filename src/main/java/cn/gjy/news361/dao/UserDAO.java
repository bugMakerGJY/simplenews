package cn.gjy.news361.dao;

import cn.gjy.news361.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Integer> {
    public boolean existsUserByOpenId(String openId);
    public User findByOpenId(String openId);
}

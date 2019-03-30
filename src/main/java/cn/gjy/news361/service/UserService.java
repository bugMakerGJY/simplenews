package cn.gjy.news361.service;

import cn.gjy.news361.dao.ChooseDAO;
import cn.gjy.news361.dao.TagDAO;
import cn.gjy.news361.dao.UserDAO;
import cn.gjy.news361.pojo.Choose;
import cn.gjy.news361.pojo.Tag;
import cn.gjy.news361.pojo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    @Autowired
    ChooseDAO chooseDAO;
    @Autowired
    TagDAO tagDAO;
    public boolean existsUser(String openId){
        return userDAO.existsUserByOpenId(openId);
    }

    public void createUser(User user){
        userDAO.save(user);
    }

    public void setTagList(User user){
        List<Choose> chooseList=chooseDAO.getChooseByUserId(user.getId());
        List<Tag> tagList=new ArrayList<>();
        for (Choose choose:chooseList){
            tagList.add(tagDAO.findById(choose.getTag().getId()));
        }
        user.setTagList(tagList);
    }

    public User findUser(String openid){
        User user=userDAO.findByOpenId(openid);
        setTagList(user);
        return user;
    }
}

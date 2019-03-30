package cn.gjy.news361.service;

import cn.gjy.news361.dao.ChooseDAO;
import cn.gjy.news361.dao.TagDAO;
import cn.gjy.news361.dao.UserDAO;
import cn.gjy.news361.pojo.Choose;
import cn.gjy.news361.pojo.Tag;
import cn.gjy.news361.pojo.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    @Autowired
    TagDAO tagDAO;
    @Autowired
    ChooseDAO chooseDAO;
    @Autowired
    UserDAO userDAO;
    public Page<Tag> list(int start, int size) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(start, size,sort);
        return tagDAO.findAll(pageable);
    }

    public List<Tag> listAll() {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        return tagDAO.findAll();
    }

    public List<Tag> GetChooseTags(String openId){
        List<Tag> chooseTags=new ArrayList<>();
        User user=userDAO.findByOpenId(openId);
        List<Choose> chooseList= chooseDAO.getChooseByUserId(user.getId());
        for(Choose choose:chooseList){
            chooseTags.add(tagDAO.findById(choose.getTag().getId()));
        }
        return chooseTags;
    }

    public List<Tag> GetRemainTags(List<Tag> chooseTags){
        List<Tag> allTags=tagDAO.findAll();
        List<Tag> remainTags=new ArrayList<>();
        for (Tag allTag:allTags){
            boolean flag=true;
            for(Tag chooseTag:chooseTags){
                if(allTag.getId()==chooseTag.getId()){
                    flag=false;
                    continue;
                }
            }
            if(flag)
                remainTags.add(allTag);
        }
        return remainTags;
    }

    @Transactional
    public void deleteTag(String openId,int tag_id){
        User user=userDAO.findByOpenId(openId);
        Tag tag=tagDAO.findById(tag_id);
        chooseDAO.deleteConnectionByUserIdAndTagId(user.getId(),tag_id);
    }
    public long count(){
        return tagDAO.count();
    }

}

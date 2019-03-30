package cn.gjy.news361.web;

import cn.gjy.news361.dao.ChooseDAO;
import cn.gjy.news361.dao.TagDAO;
import cn.gjy.news361.pojo.Choose;
import cn.gjy.news361.pojo.Tag;
import cn.gjy.news361.pojo.User;
import cn.gjy.news361.service.UserService;
import cn.gjy.news361.util.Result;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ChooseController  {
    @Autowired
    UserService userService;
    @Autowired
    ChooseDAO chooseDAO;
    @Autowired
    TagDAO tagDAO;
    @PostMapping("/choose")
    public Object createChoose(@RequestBody Map map){
        String openId=(String)map.get("openId");
        List<Map<String,Map>> tagMaps=(List<Map<String,Map>>)map.get("tags");
        String jsonString= JSONArray.toJSONString(tagMaps);
        List<Tag> tags = JSON.parseArray(jsonString, Tag.class);
        System.out.println("tags:" + tags);
        User user=userService.findUser(openId);
        try {
            for(Tag tag:tags)
                chooseDAO.save(new Choose(user,tag));
            return Result.success();
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail("添加用户标签关系失败");
        }


    }
}

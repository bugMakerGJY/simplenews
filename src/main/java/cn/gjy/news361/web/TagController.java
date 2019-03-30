package cn.gjy.news361.web;


import cn.gjy.news361.dao.ChooseDAO;
import cn.gjy.news361.pojo.Tag;
import cn.gjy.news361.service.TagService;
import cn.gjy.news361.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagController {
    @Autowired
    TagService tagService;
    @Autowired
    ChooseDAO chooseDAO;
//    @GetMapping("/tag")
//    public Page<Tag> list(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "13") int size)throws Exception{
//        start = start<0?0:start;
//        Page<Tag> page=tagService.list(start,size);
//        return page;
//    }

    @GetMapping("/tag")
    public List<Tag> listAll(){
        List<Tag> tags=tagService.listAll();
        return tags;
    }

    @GetMapping("/tag/{openId}")
    public List<Tag> getRemainTags(@PathVariable String openId){
        List<Tag> chooseTags=tagService.GetChooseTags(openId);
        List<Tag> tags=tagService.GetRemainTags(chooseTags);
        return tags;
    }

    @GetMapping("/mytag/{openId}")
    public List<Tag> getChooseTags(@PathVariable String openId){
        List<Tag> chooseTags=tagService.GetChooseTags(openId);
        return chooseTags;
    }

    @DeleteMapping("/mytag")
    public Object deleteTag(@RequestParam(value = "openId") String openId, @RequestParam(value = "tagId")int tagId){
        try {
            tagService.deleteTag(openId,tagId);
            return Result.success();
        }catch (Exception e){
            e.printStackTrace();
            return Result.fail("删除标签失败");
        }
    }
}

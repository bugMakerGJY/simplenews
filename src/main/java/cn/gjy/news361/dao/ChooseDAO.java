package cn.gjy.news361.dao;

import cn.gjy.news361.pojo.Choose;
import cn.gjy.news361.pojo.Choose;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChooseDAO extends JpaRepository<Choose,Integer> {
    /**
     * 1.1、用户关联查询
     * @param id
     * @return
     */
    @Query(value = "select * from choose where user_id=?1", nativeQuery = true)
    List<Choose> getChooseByUserId(Integer id);

    /**
     * 1.2、标签关联查询
     * @param id
     * @return
     */
    @Query(value = "select * from choose where tag_id=?1", nativeQuery = true)
    List<Choose> getChooseByTagId(Integer id);


    /**
     * 2.1、通过标签 id 删除选择关系
     * ① 在 dao 层中加上 @Modifying
     * ② 注意添加 @Transactional，否则 TransactionRequiredException
     * ③ @Transactional 建议还是在 Service 层中加上，不要在 Controller 层中
     */
    @Modifying
    @Query(value="delete from choose where tag_id=?1",nativeQuery=true)
    void deleteConnectionByTagId(Integer tagId);

    /**
     * 2.2、通过用户 id 删除选择关系
     * ① 在 dao 层中加上 @Modifying，否则 SQLException
     * ② 注意添加 @Transactional，否则 TransactionRequiredException
     * ③ @Transactional 建议还是在 Service 层中加上，不要在 Controller 层中
     */
    @Modifying
    @Query(value="delete from choose where user_id=?1",nativeQuery=true)
    void deleteConnectionByUserId(Integer userId);

    @Modifying
    @Query(value="delete from choose where user_id=?1 and tag_id=?2",nativeQuery=true)
    void deleteConnectionByUserIdAndTagId(Integer userId,Integer tagId);

//    void deleteChooseByUserAndAndTag(User user, Tag tag);
}

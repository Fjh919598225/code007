package com.java1234.repository;

import com.java1234.entity.Article;
import com.java1234.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户消息repository接口
 * @Date 2020/1/22 17:25
 * @Author JianHui
 */
public interface MessageRepository extends JpaRepository<Message,Integer>, JpaSpecificationExecutor<Message> {

    /**
     * 查询某个用户下未查看的信息
     * @param userId
     * @return
     */
    @Query(value = "select count(*) from t_message where is_see=false  and user_id=?1",nativeQuery = true)
    public Integer getCountByUserId(Integer userId);


    /**
     * 更新一查看系统消息
     * @param userId
     */
    @Transactional
    @Modifying
    @Query(value = "update t_message set is_see =true  where  user_id=?1",nativeQuery = true)
    public void  updateState(Integer userId);
}

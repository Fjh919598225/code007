package com.java1234.repository;

import com.java1234.entity.Comment;
import com.java1234.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户评论repository
 * @Date 2020/1/23 14:44
 * @Author JianHui
 */
public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {


    /**
     * 删除指定帖子的评论信息
     * @param articleId
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_comment where article_id=?1",nativeQuery = true)
    public void deleteByArticleId(Integer articleId);
}


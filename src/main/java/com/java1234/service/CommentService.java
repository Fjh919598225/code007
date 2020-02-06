package com.java1234.service;

import com.java1234.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 评论service接口
 * @Date 2020/1/23 14:45
 * @Author JianHui
 */
public interface CommentService {


    /**
     * 添加评论
     * @param article
     */
    public void save(Comment link);

    /**
     * 根据条件查询评论信息
     * @param comment
     * @param page
     * @param limit
     * @param direction
     * @param properties
     * @return
     */
    public List<Comment> list(Comment comment, Integer page, Integer pageSize, Sort.Direction direction,String... properties);


    /**
     * 根据条件获取总记录数
     * @param comment
     * @return
     */
    public Long getTotal(Comment comment);

    /**
     * 删除评论
     * @param id
     */
    public void delete(Integer id);

    /**
     * 获取评论实体
     * @param id
     * @return
     */
    public Comment get(Integer id);

    /**
     * 删除指定帖子的评论信息
     * @param articleId
     */
    public void deleteByArticleId(Integer articleId);

}

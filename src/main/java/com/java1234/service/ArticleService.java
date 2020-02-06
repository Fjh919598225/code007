package com.java1234.service;

import com.java1234.entity.Article;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * 资源service接口
 * @Date 2020/1/23 11:58
 * @Author JianHui
 */
public interface ArticleService {

    /**
     * 根据条件分页查询资源信息
     * @param article
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    public List<Article> list(Article article, Integer page , Integer pageSize , Sort.Direction direction,String...properties);

    /**
     * 根据条件查询总记录数
     * @param article
     * @return
     */
    public Long getTotal(Article article);

    /**
     * 根据Id获取实体
     * @param id
     * @return
     */
    public Article get(Integer id);

    /**
     * 添加或者修改帖子
     * @param article
     */
    public void save(Article article);

    /**
     * 删除帖子
     * @param id
     */
    public void delete(Integer id);

    /**
     * 查询所有帖子
     * @return
     */
    public List<Article> listAll();


    /**
     * 查询还没生成索引的帖子
     * @return
     */
    public List<Article> getArticleNoIndex();


    /**
     * 更改索引为true
     */
    public void updateArticleIndex(Integer id);

}

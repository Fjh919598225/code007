package com.java1234.repository;

import com.java1234.entity.ArcType;
import com.java1234.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 博客repository接口
 * @Date 2020/1/22 17:25
 * @Author JianHui
 */
public interface ArticleRepository extends JpaRepository<Article,Integer>, JpaSpecificationExecutor<Article> {

    /**
     * 查询还没生成索引的帖子
     * @return
     */
    @Query(value = "SELECT * FROM t_article WHERE index_state=0",nativeQuery = true)
    public List<Article> getArticleNoIndex();


    /**
     * 更改索引为true
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE t_article SET index_state = TRUE  WHERE id =?1",nativeQuery = true)
    public void updateArticleIndex(Integer id);
}

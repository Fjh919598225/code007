package com.java1234.repository;

import com.java1234.entity.Article;
import com.java1234.entity.UserDownload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户下载资源repository接口
 * @Date 2020/1/22 17:25
 * @Author JianHui
 */
public interface UserDownloadRepository extends JpaRepository<UserDownload,Integer>, JpaSpecificationExecutor<UserDownload> {

    /**
     * 查询某个用户下载某个资源的次数
     * @param userId
     * @param articleId
     * @return
     */
    @Query(value = "select count(*) from t_user_download where user_id=?1 and article_id =?2" ,nativeQuery = true)
    public Integer getCountByUserIdAndArticleId(Integer userId,Integer articleId);


    /**
     * 删除指定帖子的下载信息
     * @param articleId
     */
    @Transactional
    @Modifying
    @Query(value = "delete from t_user_download where article_id=?1",nativeQuery = true)
    public void deleteByArticleId(Integer articleId);

}

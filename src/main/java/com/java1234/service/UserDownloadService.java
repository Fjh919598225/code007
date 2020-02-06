package com.java1234.service;

import com.java1234.entity.UserDownload;
import com.java1234.entity.User;
import com.java1234.entity.UserDownload;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  用户下载接口
 * @Date 2020/2/3 15:44
 * @Author JianHui
 */
@Service("UserDownloadService")
public interface UserDownloadService {

    /**
     * 查询某个用户下载某个资源的次数
     * @param userId
     * @param articleId
     * @return
     */
    public Integer getCountByUserIdAndArticleId(Integer userId,Integer articleId);

    /**
     * 添加或者修改用户下载信息
     * @param userDownload
     */
    public void save(UserDownload userDownload);

    /**
     * 删除指定帖子的下载信息
     * @param articleId
     */
    public void deleteByArticleId(Integer articleId);


    /**
     * 根据条件分页查询用户下载信息
     * @param article
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    public List<UserDownload> list(UserDownload userDownload, Integer page , Integer pageSize , Sort.Direction direction, String...properties);

    /**
     * 根据条件查询总记录数
     * @param article
     * @return
     */
    public Long getTotal(UserDownload userDownload);

}

package com.java1234.service;

import com.java1234.entity.Article;
import com.java1234.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户Service接口
 * @Date 2020/1/26 13:27
 * @Author JianHui
 */
public interface UserService {


    /**
     * 添加或者修改用户信息
     * @param user
     */
    public void save(User user);

    /**
     * 根据用户名查找实体
     * @param userName
     * @return
     */
    public User findByUserName(String userName);

    /**
     * 根据用户名查找实体
     * @param userName
     * @return
     */
    public User findByEmail(String email);

    /**
     * 根据ID查找实体
     * @param id
     * @return
     */
    public User getById(Integer id);


    /**
     * 根据条件分页查询用户信息
     * @param article
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    public List<User> list(User user, Integer page , Integer pageSize , Sort.Direction direction, String...properties);

    /**
     * 根据条件查询总记录数
     * @param article
     * @return
     */
    public Long getTotal(User user);


    /**
     * 重置所有签到信息
     */
    public void updateAllSignInfo();
}

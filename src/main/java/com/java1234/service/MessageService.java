package com.java1234.service;

import com.java1234.entity.Article;
import com.java1234.entity.Message;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户消息service接口
 * @Date 2020/2/5 17:20
 * @Author JianHui
 */
public interface MessageService {

    /**
     * 查询某个用户下未查看的信息
     * @param userId
     * @return
     */
    public Integer getCountByUserId(Integer userId);

    /**
     * 添加用户消息
     * @param message
     */
    public void save(Message message);


    /**
     * 更新一查看系统消息
     * @param userId
     */
    public void  updateState(Integer userId);


    /**
     * 根据条件分页查询用户消息信息
     * @param article
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    public List<Message> list(Message message, Integer page , Integer pageSize , Sort.Direction direction, String...properties);

    /**
     * 根据条件查询总记录数
     * @param article
     * @return
     */
    public Long getTotal(Message message);

}

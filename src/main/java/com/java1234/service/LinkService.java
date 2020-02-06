package com.java1234.service;

import com.java1234.entity.Link;
import com.java1234.entity.Link;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @Date 2020/1/23 14:45
 * @Author JianHui
 */
public interface LinkService {

    /**
     * 查询所有友情链接
     * @param direction
     * @param properties
     * @return
     */
    public List<Link> listAll(Sort.Direction direction, String...properties);

    /**
     * 根据条件分页查询友情链接信息
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    public List<Link> list(Integer page , Integer pageSize , Sort.Direction direction, String...properties);


    /**
     * 根据条件查询总记录数
     * @param article
     * @return
     */
    public Long getTotal();

    /**
     * 根据Id获取实体
     * @param id
     * @return
     */
    public Link get(Integer id);

    /**
     * 添加或者修改友情链接类别
     * @param article
     */
    public void save(Link link);

    /**
     * 删除友情链接类别
     * @param id
     */
    public void delete(Integer id);

}

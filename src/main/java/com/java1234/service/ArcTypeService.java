package com.java1234.service;


import com.java1234.entity.ArcType;
import com.java1234.entity.ArcType;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * @Date 2020/1/22 17:27
 * @Author JianHui
 */
public interface ArcTypeService {

    /**
     * 查询所有资源类别
     * @param direction
     * @param properties
     * @return
     */
    public List<ArcType> listAll(Sort.Direction direction, String...properties);

    /**
     * 根据条件分页查询资源信息
     * @param page
     * @param pageSize
     * @param direction
     * @param properties
     * @return
     */
    public List<ArcType> list( Integer page , Integer pageSize , Sort.Direction direction,String...properties);


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
    public ArcType get(Integer id);

    /**
     * 添加或者修改资源类别
     * @param article
     */
    public void save(ArcType arcType);

    /**
     * 删除资源类别
     * @param id
     */
    public void delete(Integer id);


}

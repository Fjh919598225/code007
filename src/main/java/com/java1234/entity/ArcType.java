package com.java1234.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 帖子类别实体
 * @Date 2020/1/22 17:19
 * @Author JianHui
 */
@Data
@Entity
@Table(name = "t_arcType")
public class ArcType implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Integer id; //编号

    @Column(length = 100)
    private String name; //名称

    @Column(length = 1000)
    private String remark; //描述

    private Integer sort; // 排序


}

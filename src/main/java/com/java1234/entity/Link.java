package com.java1234.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Date 2020/1/23 14:43
 * @Author JianHui
 */
@Entity
@Table(name="t_link")
@Data
public class Link implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Integer id; // 编号

    @Column(length = 500)
    private String name; // 名称

    @Column(length = 500)
    private String url; // 链接地址

    private Integer sort; // 排序
}
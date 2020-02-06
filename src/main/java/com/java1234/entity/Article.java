package com.java1234.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Date 2020/1/23 11:21
 * @Author JianHui
 */
@Entity
@Table(name = "t_article")
@Data
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Integer id; // 编号

    @Column(length=200)
    private String name; // 资源名称

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    private Date publishDate; // 发布日期

    @Transient
    private String publishDateStr; // 发布日期字符串

    @ManyToOne
    @JoinColumn(name="userId")
    private User user; // 所属用户

    @ManyToOne
    @JoinColumn(name="typeId")
    private ArcType arcType; // 所属资源类别

    private Integer points; // 积分

    @Lob
    @Column(columnDefinition="longtext")
    private String content; // 资源描述

    @Column(length=200)
    private String download1; // 百度云下载地址

    @Column(length=10)
    private String password1; // 密码

    private boolean isHot=false; // 是否是热门资源 true 是 false 否

    private Integer state; // 审核状态 1 未审核  2 审核通过 3  审核未通过

    private String reason; // 审核未通过原因
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    private Date checkDate; // 审核日期

    private boolean isUseful=true; // 链接资源是否有效  true 有效 false 无效 默认有效
    private Integer view; // 访问次数

    private boolean IndexState = false;  //是否生成索引 默认false 每生产 ，true已经生成

}

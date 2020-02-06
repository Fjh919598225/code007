package com.java1234.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Date 2020/2/3 17:24
 * @Author JianHui
 */
@Entity
@Table(name = "t_comment")
@Data
public class Comment implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article; //评论的帖子

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user; //评论人

    @Column(length = 1000)
    private String content; //评论内容

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    private Date commentDate; //评论日期

    private Integer state; //审核状态0 1通过 2未通过
}

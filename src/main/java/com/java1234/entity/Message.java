package com.java1234.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 消息实体
 * @Date 2020/2/5 17:12
 * @Author JianHui
 */
@Data
@Entity
@Table(name = "t_message")
public class Message {

    @Id
    @GeneratedValue
    private Integer id; // 编号

    @Lob
    @Column(columnDefinition="longtext")
    private String content; // 消息实体

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    private Date publishDate; // 发布日期

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private boolean isSee; //是否查看


}

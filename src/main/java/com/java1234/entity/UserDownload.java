package com.java1234.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户下载实体
 * @Date 2020/2/3 15:20
 * @Author JianHui
 */
@Entity
@Table(name = "t_userDownload")
@Data
public class UserDownload implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Integer id; //编号

    @ManyToOne
    @JoinColumn(name = "articleId")
    private Article article; //下载资源

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user; //下载用户

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    private Date downloadDate; // 下载日期

}

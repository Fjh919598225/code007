package com.java1234.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Date 2020/1/21 16:10
 * @Author JianHui
 */
@Entity
@Table(name = "t_user")
@Data
public class User implements Serializable {


    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Integer id; // 编号

    @NotEmpty(message = "请输入用户名！")
    @Column(length = 100)
    private String userName; // 用户名

    @NotEmpty(message = "请输入密码！")
    @Column(length = 100)
    private String password; // 密码

    @Email(message = "邮箱地址有误！")
    @NotEmpty(message = "请输入邮箱地址！")
    @Column(length = 100)
    private String email; // 邮箱地址

    @Column(length = 100)
    private String imageName; //用户头像

    private Integer points = 0 ; // 用户积分

    private boolean isVip = false; //是否是VIP

    private boolean isOff = false; //是否封禁

    private String roleName= "会员"; // 角色名称会员 管理员两种 默认会员

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date registryDate; //注册日期

    @Transient
    private Integer messageCount; //未查看消息

    private boolean isSign=false; //是否签到，默认未签到false

    private Date signTime; // 签到时间

    private Integer signSort; //签到排序



}

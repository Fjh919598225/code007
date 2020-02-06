package com.java1234.entity;

/**
 * @Date 2020/1/29 22:04
 * @Author JianHui
 */
public class VaptchaMessage {

    private Integer success; //验证结果，1为通过，0为失败

    private Integer score; //可信度，区间[0, 100]

    private String msg; //信息描述

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

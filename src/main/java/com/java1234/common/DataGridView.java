package com.java1234.common;

import com.java1234.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Date 2020/1/30 13:34
 * @Author JianHui
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataGridView {

    private Integer code=0;
    private String msg="";
    private Long count=0L;
    private Object data;
    private boolean success;

    public DataGridView(boolean success) {
        this.success = success;
    }

    public DataGridView(Long count, Object data) {
        this.count = count;
        this.data = data;
    }

    public DataGridView(Object data) {
        this.data = data;
    }

    public DataGridView(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public DataGridView(Integer code, Long count, Object data) {
        this.code = code;
        this.count = count;
        this.data = data;
    }
}


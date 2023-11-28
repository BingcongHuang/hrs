package com.hrs.cloud.base;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class PageVO<T> {


    private  Integer code;
    private String msg;
    private Long count;
    private List<T> data = new ArrayList<>();

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<T> getData() {
        if(CollectionUtils.isEmpty(data)) {
            data = new ArrayList<>();
        }
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}

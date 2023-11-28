package com.hrs.cloud.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created on 2022/08/28.
 */
@ApiModel(value = "分页条件")
public class BaseDto implements Serializable {
    private static final long serialVersionUID = 7388418691306996651L;

    @ApiModelProperty(value = "页码")
    private Integer page = 1;
    @ApiModelProperty(value = "每页条数")
    private Integer limit = 10;
    @ApiModelProperty(value = "是否需要分页 0 分页 1 不分页")
    private int needPage;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public int getNeedPage() {
        return needPage;
    }

    public void setNeedPage(int needPage) {
        this.needPage = needPage;
    }
}

package com.njustc.onlinebiz.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 分页查询返回的结果
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {

    // 此结果代表的页码
    private int page;

    // 每页有多少条记录
    private int pageSize;

    // 总共有多少条记录
    private long total;

    // 此页的数据列表
    private List<T> list;

}

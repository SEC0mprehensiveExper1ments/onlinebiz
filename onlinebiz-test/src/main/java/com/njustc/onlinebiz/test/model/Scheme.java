package com.njustc.onlinebiz.test.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Scheme {
    // MongoDB 中存放此类型对象的 collection 的名字
    public static final String COLLECTION_NAME = "scheme";

    // 合同 id，由 MongoDB 自动生成
    private String id;

    // 此测试方案对应的创建者 id
    private Long creatorId;

    // 此测试方案对应的委托 id
    private String entrustId;

    // 合同内容
    private SchemeContent schemeContent;

    // 此测试方案状态
    private SchemeStatus schemeStatus;
}



package com.njustc.onlinebiz.common.model.test.scheme;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Scheme {
    // MongoDB 中存放此类型对象的 collection 的名字
    public static final String COLLECTION_NAME = "scheme";

    // 测试方案 id，由 MongoDB 自动生成
    private String id;

    // 此测试方案对应的创建者 id
    private Long creatorId;

    // 此测试方案对应的委托 id
    private String entrustId;

    // 此测试方案对应的项目 id
    private String projectId;

    // 测试方案内容
    private SchemeContent content;
}



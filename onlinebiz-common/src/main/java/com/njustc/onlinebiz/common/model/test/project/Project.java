package com.njustc.onlinebiz.common.model.test.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
/*
  测试项目模型，由该模块可以直接索引该测试项目文件
*/
public class Project {
    // MongoDB 中存放此类型对象的 collection 的名字
    public static final String COLLECTION_NAME = "project";

    // 项目 id，由 MongoDB 自动生成
    private String id;

    /** 测试项目的基本信息 **/
    private ProjectBaseInfo projectBaseInfo;

    /** 测试项目包含的各种表格 **/
    private ProjectFormIds projectFormIds;

    /** 测试项目状态 **/
    private ProjectStatus status;
}

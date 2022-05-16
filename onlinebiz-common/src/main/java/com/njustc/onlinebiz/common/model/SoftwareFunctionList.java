package com.njustc.onlinebiz.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SoftwareFunctionList {
    // MongoDB 中存放此类型对象的 collection 的名字
    public static final String COLLECTION_NAME = "softwareList";

    // 申请 id，由 MongoDB 自动生成
    private String id;

    // 软件名称
    private String softwareName;

    // 版本号
    private String version;

    //所有软件功能项目(JS003)
    private List<SoftwareFunction> softwareFunctionProject;

    // 委托申请方用户 id
    private Long principalId;
}

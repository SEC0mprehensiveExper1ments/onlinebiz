package com.njustc.onlinebiz.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
class SoftwareSubFunction {
    //子功能名称
    private String subFunctionName;
    //功能说明
    private String description;

    public String getSubFunctionName() {
        return subFunctionName;
    }

    public String getDescription() {
        return description;
    }
}

@Data
class SoftwareFunction {
    //功能项目名称
    private String functionName;
    //子功能
    private List<SoftwareSubFunction> subFunctionList;

    public String getFunctionName() {
        return functionName;
    }

    public List<SoftwareSubFunction> getSubFunctionList() {
        return subFunctionList;
    }

    public int getSubFunctionListSize() {
        return subFunctionList.size();
    }
}

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
    private List<SoftwareSubFunction> softwareFunctionProject;

    // 委托申请方用户 id
    private Long principalId;
}

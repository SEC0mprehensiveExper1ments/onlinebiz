package com.njustc.onlinebiz.apply.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

// 申请概要，详细信息保存在 Apply 对象中
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Outline {

    // 规定如何从 Apply 对象投影到 Outline 对象的 JSON 字符串
    public static final String PROJECT_FROM_CONTRACT =
            "{\n" +
            "    _id: 1,\n" +
            "    testType: 1,\n" +
            "    principal: {\n" +
            "        contact: 1\n" +
            "        companyCH: 1\n" +
            "    },\n" +
            "    testedSoftware: {\n" +
            "        softwareName: 1\n" +
            "        version: 1\n" +
            "    },\n" +
            "    acceptance: 1\n" +
            "    testSerialNumber: 1\n" +
            "}";

    // 测试类型
    private String[] testType;

    // 软件名称
    private String softwareName;

    // 版本号
    private String version;

    // 委托单位名称（中文）
    private String companyCH;

    // 委托单位联系人
    private String contact;

    // 受理意见
    private String acceptance;

    // 测试项目编号
    private String testSerialNumber;
}

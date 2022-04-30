package com.njustc.onlinebiz.contract.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

// 合同概要，详细信息保存在 Contract 对象中
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Outline {

    // 规定如何从 Contract 对象投影到 Outline 对象的 JSON 字符串
    public static final String PROJECT_FROM_CONTRACT =
            "{\n" +
            "    _id: 1,\n" +
            "    serialNumber: 1,\n" +
            "    projectName: 1,\n" +
            "    targetSoftware: 1,\n" +
            "    principal: {\n" +
            "        contact: 1\n" +
            "    },\n" +
            "    trustee: {\n" +
            "        contact: 1\n" +
            "    },\n" +
            "    price: 1\n" +
            "}";

    // 合同 id
    private String id;

    // 合同编号
    private String serialNumber;

    // 项目名称
    private String projectName;

    // 受测软件
    private String targetSoftware;

    // 甲方联系人
    private String principalContact;

    // 乙方联系人
    private String trusteeContact;

    // 合同价款
    private Double price;

}

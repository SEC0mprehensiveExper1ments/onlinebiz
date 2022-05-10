package com.example.onlinebizentrust.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

// 代表一份委托
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Document(Entrust.COLLECTION_NAME)
public class Entrust {

    // MongoDB 中存放此类型对象的 collection 的名字
    public static final String COLLECTION_NAME = "entrust";

    // 委托 id，由 MongoDB 自动生成
    private String id;

    // 委托编号
    private String serialNumber;

    // 项目名称
    private String projectName;

    // 项目委托方（甲方）
    private Party principal;

    // 项目委托方法人代表
    private String principalRepresentative;

    // 项目受托方法人代表
    private String trusteeRepresentative;

    // 签订日期
    private String signedDate;

    // 额外维护信息

    // 委托方用户 id
    private Long principalId;

    // 创建者的用户 id
    private Long creatorId;

    // 此委托对应的委托 id
    private String entrustId;

}


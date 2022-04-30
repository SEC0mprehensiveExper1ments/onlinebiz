package com.njustc.onlinebiz.contract.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

// 代表一份合同
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Document(Contract.COLLECTION_NAME)
public class Contract {

    // MongoDB 中存放此类型对象的 collection 的名字
    public static final String COLLECTION_NAME = "contract";

    // 合同 id，由 MongoDB 自动生成
    private String id;

    // 合同编号
    private String serialNumber;

    // 项目名称
    private String projectName;

    // 项目委托方（甲方）
    private Party principal;

    // 项目受托方（乙方）
    private Party trustee;

    // 签订地点
    private String signedAt;

    // 签订日期
    private String signedDate;

    // 受测软件
    private String targetSoftware;

    // 合同价款
    private Double price;

    // 履行期限
    private Integer totalWorkingDays;

    // 总整改次数
    private Integer rectificationLimit;

    // 每次整改不超过多少天
    private Integer rectificationDaysEachTime;

    // 额外维护信息

    // 委托方用户 id
    private Long principalId;

    // 创建者的用户 id
    private Long creatorId;

}

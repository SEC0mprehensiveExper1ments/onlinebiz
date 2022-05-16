package com.njustc.onlinebiz.common.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

// 代表一份申请
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Document(Apply.COLLECTION_NAME)
public class Apply {

    // MongoDB 中存放此类型对象的 collection 的名字
    public static final String COLLECTION_NAME = "apply";

    // 申请 id，由 MongoDB 自动生成
    private String id;

    /* 按照JS002表格字段命名，
       子项先从左往右，从上至下，递增 */

    // 测试类型
    private String[] testType;

    // 委托单位信息
    private ApplyParty principal;

    // 软件信息
    private Software testedSoftware;

    // 测试依据
    private String[] testStandard;

    // 技术指标
    private String[] techIndex;

    // 软件介质
    private String softwareMedium;

    // 文档资料
    private String document;

    // 样品处理方式
    private String sampleHandling;

    // 希望完成时间
    private String expectedTime;

    // 密级
    private String securityLevel;

    // 查杀病毒
    private String checkVirus;

    // 材料检查
    private String[] checkMaterious;

    // 确认意见
    private String confirmation;

    // 受理意见
    private String acceptance;

    // 测试项目编号
    private String testSerialNumber;

    /* 额外维护信息 */

    // 委托申请方用户 id
    private Long principalId;

    // 审核者的用户 id
    private Long auditorId;

    // 审核阶段
    private String auditorStage;

    /* 员工分配信息 */

    // 分配市场部人员的用户id
    private String assignedMarketId;

    // 分配测试部人员的用户id
    private String assignedTestId;
}

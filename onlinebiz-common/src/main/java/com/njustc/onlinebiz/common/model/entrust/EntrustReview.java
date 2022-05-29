package com.njustc.onlinebiz.common.model.entrust;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 由市场部人员填写的委托评审结果
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntrustReview {

    // 密级
    private String securityLevel;

    // 查杀病毒
    private String checkVirus;

    // 材料检查
    private List<String> checkMaterial;

    // 确认意见
    private String confirmation;

    // 受理意见
    private String acceptance;

    // 测试项目编号
    private String serialNumber;

}

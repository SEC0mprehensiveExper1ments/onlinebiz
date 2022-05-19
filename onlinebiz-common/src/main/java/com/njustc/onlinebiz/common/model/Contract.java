package com.njustc.onlinebiz.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 代表一份合同
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contract {

    // 合同编号
    private String serialNumber;

    // 项目名称
    private String projectName;

    // 项目委托方（甲方）
    private PartyDetail principal;

    // 项目受托方（乙方）
    private PartyDetail trustee;

    // 签订地点
    private String signedAt;

    // 签订日期
    private String signedDate;

    // 受测软件名称
    private String targetSoftware;

    // 合同价款
    private Double price;

    // 履行期限
    private Integer totalWorkingDays;

    // 总整改次数
    private Integer rectificationLimit;

    // 每次整改不超过多少天
    private Integer rectificationDaysEachTime;

    // 合同扫描件的文件路径
    private String scannedCopyPath;

}

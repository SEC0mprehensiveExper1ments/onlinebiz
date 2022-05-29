package com.njustc.onlinebiz.common.model.contract;

import com.njustc.onlinebiz.common.model.PartyDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 代表一份合同
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contract {

    // 合同ID
    private String id;

    // 负责此合同的市场部人员ID
    private Long marketerId;

    // 签订此合同的客户ID
    private Long customerId;

    // 与此合同相关的委托ID
    private String entrustId;

    // 合同目前的状态
    private ContractStatus status;

    // 合同编号
    private String serialNumber;

    // 项目名称
    private String projectName;

    // 项目委托方（甲方）
    private PartyDetail partyA;

    // 项目受托方（乙方）
    private PartyDetail partyB;

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

    // 合同对应的保密协议
    private NonDisclosureAgreement nonDisclosureAgreement;

}

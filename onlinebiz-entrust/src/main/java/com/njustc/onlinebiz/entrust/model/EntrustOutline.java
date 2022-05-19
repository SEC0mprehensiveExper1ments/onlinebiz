package com.njustc.onlinebiz.entrust.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 委托的概要信息
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntrustOutline {

    // 委托ID
    private String id;

    // 客户ID
    private Long customerId;

    // 市场部人员ID
    private Long marketerId;

    // 测试部人员ID
    private Long testerId;

    // 委托涉及的软件名称
    private String softwareName;

    // 委托涉及的软件版本号
    private String softwareVersion;

    // 委托状态
    private EntrustStatus status;

    public EntrustOutline(Entrust entrust) {
        this.id = entrust.getId();
        this.customerId = entrust.getCustomerId();
        this.marketerId = entrust.getMarketerId();
        this.testerId = entrust.getTesterId();
        this.softwareName = entrust.getContent().getSoftware().getName();
        this.softwareVersion = entrust.getContent().getSoftware().getVersion();
        this.status = entrust.getStatus();
    }

}

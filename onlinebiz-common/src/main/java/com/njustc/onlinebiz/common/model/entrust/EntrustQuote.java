package com.njustc.onlinebiz.common.model.entrust;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 委托报价单
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntrustQuote {

    // 报价日期
    private String quotationDate;

    // 报价有效期
    private String effectiveDate;

    // 开户银行
    private String bankName;

    // 账号
    private String account;

    // 户名
    private String accountName;

    // 软件名称
    private String softwareName;

    // 报价单前面几行
    private List<EntrustQuoteRow> rowList;

    // 小计
    private Double subTotal;

    // 税率
    private Double taxRate;

    // 总计
    private Double total;

    // 报价提供人
    private String provider;

    // 签字
    private String signature;

    // 报价单的前两行
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EntrustQuoteRow {

        // 项目
        private String projectName;

        // 分项
        private String subProject;

        // 单价
        private Double price;

        // 说明
        private String description;

        // 行合计
        private Double rowTotal;

    }

}

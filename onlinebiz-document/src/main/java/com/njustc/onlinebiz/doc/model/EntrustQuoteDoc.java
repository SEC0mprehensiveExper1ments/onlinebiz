package com.njustc.onlinebiz.doc.model;

import com.njustc.onlinebiz.common.model.entrust.EntrustQuote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EntrustQuoteDoc {
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
    private List<EntrustQuote.EntrustQuoteRow> rowList=new ArrayList<>();
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

    public EntrustQuoteDoc(EntrustQuote entrustQuote) {
        this.quotationDate = entrustQuote.getQuotationDate();
        this.effectiveDate = entrustQuote.getEffectiveDate();
        this.bankName = entrustQuote.getBankName();
        this.account = entrustQuote.getAccount();
        this.accountName = entrustQuote.getAccountName();
        this.softwareName = entrustQuote.getSoftwareName();
        this.subTotal = entrustQuote.getSubTotal();
        this.taxRate = entrustQuote.getTaxRate();
        this.total = entrustQuote.getTotal();
        this.provider = entrustQuote.getProvider();
        this.signature = entrustQuote.getSignature();
    }
}

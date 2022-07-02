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

    public String safe(String s) {
        return s == null ? "" : s;
    }
    private Double safe(Double s) {
        return s == null ? 0f : s;
    }
    public EntrustQuoteDoc(EntrustQuote entrustQuote) {
        if (entrustQuote != null) {
            this.quotationDate = safe(entrustQuote.getQuotationDate());
            this.effectiveDate = safe(entrustQuote.getEffectiveDate());
            this.bankName = safe(entrustQuote.getBankName());
            this.account = safe(entrustQuote.getAccount());
            this.accountName = safe(entrustQuote.getAccountName());
            this.softwareName = safe(entrustQuote.getSoftwareName());
            this.subTotal = safe(entrustQuote.getSubTotal());
            this.taxRate = safe(entrustQuote.getTaxRate());
            this.total = safe(entrustQuote.getTotal());
            this.provider = safe(entrustQuote.getProvider());
            this.signature = safe(entrustQuote.getSignature());
        }
    }

}

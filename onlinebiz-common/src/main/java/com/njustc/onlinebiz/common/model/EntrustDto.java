package com.njustc.onlinebiz.common.model;


import com.njustc.onlinebiz.entrust.model.Entrust;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntrustDto {


    // 市场部生成项目人(市场部指定员工) id
    private Long marketerId;
    // 测试部指定员工 id
    private Long testerId;
    // 软件名称
    private String software;
    //委托对应的客户id
    private Long customerId;

    public EntrustDto(Entrust entrust) {
        this.marketerId = entrust.getMarketerId();
        this.testerId = entrust.getTesterId();
        this.software = entrust.getContent().getSoftware().getName();
        this.customerId = entrust.getCustomerId();
    }
}

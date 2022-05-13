package com.njustc.onlinebiz.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

// 代表合同当事人（甲方、乙方）
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ContractParty {

    // 单位全称
    private String company;

    // 授权代表
    private String authorizedRepresentative;

    // 签章日期
    private String sigDate;

    // 联系人
    private String contact;

    // 通讯地址
    private String address;

    // 电话
    private String phoneNumber;

    // 邮编
    private String zipCode;

    // 传真
    private String fax;

    // 开户银行
    private String bankName;

    // 账号
    private String account;

    // 银行户名，可以省略不填
    private String accountName;

}

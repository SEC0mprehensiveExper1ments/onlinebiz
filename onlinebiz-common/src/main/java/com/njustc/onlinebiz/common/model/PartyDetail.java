package com.njustc.onlinebiz.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 委托双方的详细信息
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyDetail {

    // 单位全称（中文）
    private String companyCH;

    // 单位全称（英文）
    private String companyEN;

    // 单位电话
    private String companyPhone;

    // 单位网址
    private String companyWebsite;

    // 单位地址
    private String companyAddress;

    // 联系人名称
    private String contact;

    // 联系人电话
    private String contactPhone;

    // 联系人邮箱
    private String contactEmail;

    // 授权代表
    private String representative;

    // 签章日期
    private String sigDate;

    // 邮编
    private String zipCode;

    // 传真
    private String fax;

    // 开户银行
    private String bankName;

    // 账号
    private String account;

    // 银行户名
    private String accountName;

}

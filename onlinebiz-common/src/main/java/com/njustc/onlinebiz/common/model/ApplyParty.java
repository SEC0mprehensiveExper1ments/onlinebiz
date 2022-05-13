package com.njustc.onlinebiz.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

// 代表委托申请的委托单位
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ApplyParty {

    // 委托单位名称（英文）
    private String companyEN;

    // 委托单位名称（中文）
    private String companyCH;

    // 委托单位电话
    private String phoneNumber;

    // 委托单位传真
    private String fax;

    // 委托单位地址
    private String address;

    // 委托单位邮编
    private String zipCode;

    // 委托单位联系人
    private String contact;

    // 委托单位手机
    private String cellphoneNumber;

    // 委托单位E-mail
    private String email;

    // 委托单位网址
    private String website;

}

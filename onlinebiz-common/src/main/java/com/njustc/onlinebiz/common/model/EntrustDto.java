package com.njustc.onlinebiz.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntrustDto {

    // 测试部人员ID
    private Long customerId;

    // 市场部生成项目人(市场部指定员工) id
    private Long marketerId;

    // 测试部指定员工 id
    private Long testerId;

    // 软件名称
    private String software;

    // 项目编号（手填）
    private String serialNumber;

    // 测试项目id
    private String projectId;

}

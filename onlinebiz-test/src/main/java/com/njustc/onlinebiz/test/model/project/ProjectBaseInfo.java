package com.njustc.onlinebiz.test.model.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProjectBaseInfo {
    /** 测试项目的基本信息 */
    // 测试项目对应的委托 id
    private String entrustId;
    // 市场部生成项目人(市场部指定员工) id
    private Long marketerId;
    // 测试部指定员工 id
    private Long testerId;
    // 质量部指定员工 id
    private Long qaId;
    // 软件名称
    private String softwareName;


}

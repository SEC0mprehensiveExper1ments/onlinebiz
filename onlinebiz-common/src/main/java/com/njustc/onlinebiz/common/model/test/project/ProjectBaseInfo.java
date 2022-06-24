package com.njustc.onlinebiz.common.model.test.project;

import com.njustc.onlinebiz.common.model.EntrustDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
    //委托对应的客户id
    private Long customerId;

    // 软件名称
    private String softwareName;
    // 项目编号（手填）
    private String serialNumber;

    public ProjectBaseInfo(String entrustId, EntrustDto entrustDto) {
        this.setEntrustId(entrustId);
        if (entrustDto != null) {
            this.setMarketerId(entrustDto.getMarketerId());
            this.setTesterId(entrustDto.getTesterId());
            this.setSoftwareName(entrustDto.getSoftware());
            this.setCustomerId(entrustDto.getCustomerId());
            this.setSerialNumber(entrustDto.getSerialNumber());
        }
    }


}

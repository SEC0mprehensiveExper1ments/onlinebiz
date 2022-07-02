package com.njustc.onlinebiz.common.model.entrust;

import com.njustc.onlinebiz.common.model.EntrustDto;
import com.njustc.onlinebiz.common.model.Software;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 代表一份委托
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entrust {

    // 委托ID
    private String id;

    // 提交委托的用户ID
    private Long customerId;

    // 分配的市场部人员ID
    private Long marketerId;

    // 分配的测试部人员ID
    private Long testerId;

    // 委托对应的合同ID
    private String contractId;

    // 委托对应的测试项目ID
    private String projectId;

    // 委托状态
    private EntrustStatus status;

    // 委托申请内容
    private EntrustContent content;

    // 软件文档评审
    private SoftwareDocReview softwareDocReview;

    // 委托评审结果
    private EntrustReview review;

    // 委托报价
    private EntrustQuote quote;

    public EntrustDto toEntrustDto() {
        EntrustDto entrustDto = new EntrustDto();
        if (content != null) {
            Software software = content.getSoftware();
            if (software != null) {
                entrustDto.setSoftware(software.getName());
            }
        }
        entrustDto.setProjectId(projectId);
        entrustDto.setCustomerId(customerId);
        entrustDto.setMarketerId(marketerId);
        entrustDto.setTesterId(testerId);
        if (review != null) {
            entrustDto.setSerialNumber(review.getSerialNumber());
        }
        return entrustDto;
    }

}

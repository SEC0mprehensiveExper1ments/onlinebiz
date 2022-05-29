package com.njustc.onlinebiz.common.model.test.project;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectOutline {

    // 测试项目ID
    private String id;
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
    // 测试项目状态
    private ProjectStatus status;

    public ProjectOutline(Project project) {
        this.id = project.getId();
        this.entrustId = project.getProjectBaseInfo().getEntrustId();
        this.marketerId = project.getProjectBaseInfo().getMarketerId();
        this.testerId = project.getProjectBaseInfo().getTesterId();
        this.qaId = project.getProjectBaseInfo().getQaId();
        this.softwareName = project.getProjectBaseInfo().getSoftwareName();
        this.status = project.getStatus();
    }
}

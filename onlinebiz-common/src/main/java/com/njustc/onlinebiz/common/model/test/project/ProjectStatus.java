package com.njustc.onlinebiz.common.model.test.project;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectStatus {
    // 项目目前所处阶段
    private ProjectStage stage;
    // 附加的说明信息
    private String message;

}

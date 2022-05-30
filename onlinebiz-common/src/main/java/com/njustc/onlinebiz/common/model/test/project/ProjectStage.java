package com.njustc.onlinebiz.common.model.test.project;

public enum ProjectStage {
    // 等待分配质量人员
    WAIT_FOR_QA,
    // (已分配质量人员) 项目正在进行中，未归档
    NOT_FILE,
    // 项目已结束，已归档
    FILED
}

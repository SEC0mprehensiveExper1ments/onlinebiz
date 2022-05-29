package com.njustc.onlinebiz.common.model.entrust;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 软件文档评审表
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoftwareDocReview {

    // 软件名称
    private String softwareName;

    // 版本号
    private String softwareVersion;

    // 委托单位名称
    private String companyName;

    // 高校评审组
    private String reviewTeam;

    // 评审人
    private String reviewer;

    // 评审完成年
    private int year;

    // 评审完成月
    private int month;

    // 评审完成日
    private int day;

    // 评审结果与评审结果说明
    List<ReviewRow> comments;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewRow {

        // 评审结果
        private String result;

        // 评审结果说明
        private String description;

    }

}

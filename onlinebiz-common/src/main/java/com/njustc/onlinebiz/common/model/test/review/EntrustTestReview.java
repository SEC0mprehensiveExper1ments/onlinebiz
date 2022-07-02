package com.njustc.onlinebiz.common.model.test.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EntrustTestReview {
    //MongoDB 中存放此类型对象的 collection 名称
    public static final String COLLECTION_NAME = "entrustTestReview";
    /** 一般不更新的部分 **/
    // 项目工作检查表 id，由 MongoDB 自动生成
    private String id;
    // 对应的项目 id
    private String projectId;
    /** 每次会经常更新的部分 **/
    // 软件名称
    private String softwareName;
    // 版本号
    private String version;
    // 申报单位
    private String principal;
    // 起始时间
    private String startDate;
    // 预计完成时间
    private String expectFinishDate;
    // 主测人
    private String mainTester;
    // 实际完成时间
    private String finishDate;
    // 各项检查结果
    List<String> conclusions;
}

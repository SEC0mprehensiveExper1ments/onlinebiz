package com.njustc.onlinebiz.common.model.entrust;

import com.njustc.onlinebiz.common.model.PartyDetail;
import com.njustc.onlinebiz.common.model.Software;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 委托申请的内容
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntrustContent {

    // 测试类型
    private List<String> testType;

    // 委托单位信息
    private PartyDetail principal;

    // 软件信息
    private Software software;

    // 测试依据
    private List<String> testStandard;

    // 技术指标
    private List<String> techIndex;

    // 软件介质
    private String softwareMedium;

    // 文档资料
    private String document;

    // 样品处理方式
    private String sampleHandling;

    // 希望完成时间
    private String expectedTime;

}

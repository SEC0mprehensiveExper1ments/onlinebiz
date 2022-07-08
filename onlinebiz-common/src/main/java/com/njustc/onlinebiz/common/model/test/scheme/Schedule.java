package com.njustc.onlinebiz.common.model.test.scheme;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Schedule {
    //    工作量
    private String workload;
    //    开始时间
    private String startDate;
    //    结束时间
    private String endDate;
}

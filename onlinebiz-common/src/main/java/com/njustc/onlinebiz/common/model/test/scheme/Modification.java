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
public class Modification{
    //    版本号
    private String version;
    //    修改日期
    private String modificationDate;
    //    操作
    public enum Method{A,M,D}
    Method method;
    //    修订者
    private String modifier;
    //    说明
    private String illustration;
}

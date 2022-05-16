package com.njustc.onlinebiz.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
public class SoftwareFunction {
    //功能项目名称
    private String functionName;
    //子功能
    private List<SoftwareSubFunction> subFunctionList;
}

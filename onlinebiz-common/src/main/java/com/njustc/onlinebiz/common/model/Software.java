package com.njustc.onlinebiz.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 委托申请测试软件的软件信息
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Software {

    // 软件名称
    private String name;

    // 版本号
    private String version;

    // 开发单位
    private String developer;

    // 开发单位性质
    private String developerType;

    // 软件用户对象描述
    private String userDescription;

    // 主要功能简介
    private String functionIntro;

    // 软件功能数
    private String functionNums;

    // 软件功能点数
    private String functionPoint;

    // 软件代码行数
    private String codeLine;

    // 软件类型
    private String type;

    // 客户端操作系统
    private List<String> clientOS;

    // 客户端内存要求
    private String clientMemoryRequirement;

    // 客户端其他要求
    private String clientOtherRequirement;

    // 服务器端硬件架构
    private List<String> serverHardArch;

    // 服务器端硬件内存要求
    private String servHardMemoryRequirement;

    // 服务器端硬盘要求
    private String servHardDiskRequirement;

    // 服务器端其他要求
    private String servHardOtherRequirement;

    // 服务器端软件操作系统
    private String servSoftOS;

    // 服务器端软件版本
    private String servSoftVersion;

    // 服务器端软件编程语言
    private String servSoftProgramLang;

    // 服务器端软件构架
    private List<String> servSoftArch;

    // 服务器端软件中间件
    private String servSoftMiddleware;

    // 服务器端软件数据库
    private String servSoftDatabase;

    // 服务器端软件其他支撑软件
    private String serverSideOtherSupport;

    // 软件网络环境
    private String networkEnvironment;

    // 软件包含的模块
    private List<Module> modules;

    // 软件功能模块
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Module {

        // 模块名称
        private String moduleName;

        // 模块功能列表
        private List<ModuleFunction> functions;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModuleFunction {

        // 功能名称
        private String functionName;

        // 功能说明
        private String functionDescription;

    }

}

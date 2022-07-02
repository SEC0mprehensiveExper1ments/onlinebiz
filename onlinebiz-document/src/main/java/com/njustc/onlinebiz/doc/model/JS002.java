package com.njustc.onlinebiz.doc.model;


import com.njustc.onlinebiz.common.model.PartyDetail;
import com.njustc.onlinebiz.common.model.Software;
import com.njustc.onlinebiz.common.model.entrust.Entrust;
import com.njustc.onlinebiz.common.model.entrust.EntrustReview;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JS002 {
    /* 子项先从左往右，从上至下，递增 */
    private String multiCeShiLeiXing01 = "";
    private String multiCeShiLeiXing02 = "";
    private String inputCeShiLeiXing02 = "";
    private String multiCeShiLeiXing03 = "";
    private String multiCeShiLeiXing04 = "";
    private String inputRuanJianMingChen = "";
    private String inputBanBenHao = "";
    private String inputWeiTuoDanWeiZhongWen = "";
    private String inputWeiTuoDanWeiYingWen = "";
    private String inputKaiFaDanWei = "";
    private String singleDanWeiXingZhi01 = "";
    private String singleDanWeiXingZhi02 = "";
    private String singleDanWeiXingZhi03 = "";
    private String singleDanWeiXingZhi04 = "";
    private String singleDanWeiXingZhi05 = "";
    private String singleDanWeiXingZhi06 = "";
    private String inputRuanJianMiaoShu = "";
    private String inputZhuYaoGongNeng = "";
    private String multiCeShiYiJu01 = "";
    private String multiCeShiYiJu02 = "";
    private String multiCeShiYiJu03 = "";
    private String multiCeShiYiJu04 = "";
    private String multiCeShiYiJu05 = "";
    private String inputCeShiYiJu05 = "";
    private String multiJiShuZhiBiao01 = "";
    private String multiJiShuZhiBiao02 = "";
    private String multiJiShuZhiBiao03 = "";
    private String multiJiShuZhiBiao04 = "";
    private String multiJiShuZhiBiao05 = "";
    private String multiJiShuZhiBiao06 = "";
    private String multiJiShuZhiBiao07 = "";
    private String multiJiShuZhiBiao08 = "";
    private String multiJiShuZhiBiao09 = "";
    private String multiJiShuZhiBiao010 = "";
    private String multiJiShuZhiBiao011 = "";
    private String multiJiShuZhiBiao012 = "";
    private String multiJiShuZhiBiao013 = "";
    private String inputJiShuZhiBiao013 = "";
    private String inputRuanJianGuiMo01 = "";
    private String inputRuanJianGuiMo02 = "";
    private String inputRuanJianGuiMo03 = "";
    private String singleRuanJianLeiXing0XiTong01 = "";
    private String singleRuanJianLeiXing0XiTong02 = "";
    private String singleRuanJianLeiXing0XiTong03 = "";
    private String singleRuanJianLeiXing0XiTong04 = "";
    private String singleRuanJianLeiXing0XiTong05 = "";
    private String singleRuanJianLeiXing0ZhiChi01 = "";
    private String singleRuanJianLeiXing0ZhiChi02 = "";
    private String singleRuanJianLeiXing0ZhiChi03 = "";
    private String singleRuanJianLeiXing0ZhiChi04 = "";
    private String singleRuanJianLeiXing0ZhiChi05 = "";
    private String singleRuanJianLeiXing0ZhiChi06 = "";
    private String singleRuanJianLeiXing0YingYong01 = "";
    private String singleRuanJianLeiXing0YingYong02 = "";
    private String singleRuanJianLeiXing0YingYong03 = "";
    private String singleRuanJianLeiXing0YingYong04 = "";
    private String singleRuanJianLeiXing0YingYong05 = "";
    private String singleRuanJianLeiXing0YingYong06 = "";
    private String singleRuanJianLeiXing0YingYong07 = "";
    private String singleRuanJianLeiXing0YingYong08 = "";
    private String singleRuanJianLeiXing0YingYong09 = "";
    private String singleRuanJianLeiXing0YingYong010 = "";
    private String singleRuanJianLeiXing0YingYong011 = "";
    private String singleRuanJianLeiXing0YingYong012 = "";
    private String singleRuanJianLeiXing0YingYong013 = "";
    private String singleRuanJianLeiXing0QiTa01 = "";
    private String multiKeHuDuan0Windows = "";
    private String inputKeHuDuan0Windows = "";
    private String multiKeHuDuan0Linux = "";
    private String inputKeHuDuan0Linux = "";
    private String multiKeHuDuan0QiTa = "";
    private String inputKeHuDuan0QiTa = "";
    private String inputKeHuDuan0NeiCunYaoQiu = "";
    private String inputKeHuDuan0QiTaYaoQiu = "";
    private String multiFuWuQiYingJian0PC = "";
    private String multiFuWuQiYingJian0Linux = "";
    private String multiFuWuQiYingJian0QiTa = "";
    private String inputFuWuQiYingJian0QiTa = "";
    private String inputFuWuQiYingJian0NeiCunYaoQiu = "";
    private String inputFuWuQiYingJian0YingPanYaoQiu = "";
    private String inputFuWuQiYingJian0QiTaYaoQiu = "";
    private String inputFuWuQiRuanJian0CaoZuoXiTong = "";
    private String inputFuWuQiRuanJian0BanBen = "";
    private String inputFuWuQiRuanJian0BianChengYuYan = "";
    private String multiFuWuQiRuanJian0GouJia0CS = "";
    private String multiFuWuQiRuanJian0GouJia0BS = "";
    private String multiFuWuQiRuanJian0GouJia0QiTa = "";
    private String inputFuWuQiRuanJian0ShuJuKu = "";
    private String inputFuWuQiRuanJian0ZhongJianJian = "";
    private String inputFuWuQiRuanJian0QiTaZhiCheng = "";
    private String inputWangLuoHuanJing = "";
    private String singleRuanJianJieZhi0GuangPan = "";
    private String singleRuanJianJieZhi0UPan = "";
    private String singleRuanJianJieZhi0QiTa = "";
    private String inputRuanJianJieZhi0QiTa = "";
    private String inputWenDangZiLiao = "";
    private String singleYangPingChuLi01 = "";
    private String singleYangPingChuLi02 = "";
    private String inputWeiTuoDanWei0DianHua = "";
    private String inputWeiTuoDanWei0ChuanZhen = "";
    private String inputWeiTuoDanWei0DiZhi = "";
    private String inputWeiTuoDanWei0YouBian = "";
    private String inputWeiTuoDanWei0LianXiRen = "";
    private String inputWeiTuoDanWei0ShouJi = "";
    private String inputWeiTuoDanWei0Email = "";
    private String inputWeiTuoDanWei0WangZhi = "";
    private String singleMiJi01 = "";
    private String singleMiJi02 = "";
    private String singleMiJi03 = "";
    private String singleChaShaBingDu01 = "";
    private String singleChaShaBingDu02 = "";
    private String inputChaShaBingDu02 = "";
    private String multiCaiLiaoJianCha0CeshiYangPing01 = "";
    private String multiCaiLiaoJianCha0CeshiYangPing02 = "";
    private String multiCaiLiaoJianCha0XuQiuWenDang01 = "";
    private String multiCaiLiaoJianCha0XuQiuWenDang02 = "";
    private String multiCaiLiaoJianCha0XuQiuWenDang03 = "";
    private String multiCaiLiaoJianCha0YongHuWenDang01 = "";
    private String multiCaiLiaoJianCha0YongHuWenDang02 = "";
    private String multiCaiLiaoJianCha0CaoZuoWenDang01 = "";
    private String multiCaiLiaoJianCha0CaoZuoWenDang02 = "";
    private String multiCaiLiaoJianCha0CaoZuoWenDang03 = "";
    private String multiCaiLiaoJianCha0CaoZuoWenDang04 = "";
    private String inputCaiLiaoJianCha0QiTa = "";
    private String singleQueRenYiJian01 = "";
    private String singleQueRenYiJian02 = "";
    private String singleQueRenYiJian03 = "";
    private String singleQueRenYiJian04 = "";
    private String singleShouLiYiJian01 = "";
    private String singleShouLiYiJian02 = "";
    private String singleShouLiYiJian03 = "";
    private String inputCeShiXiangMuBianHao = "";
    private String inputBeiZhu = "";

    /**
     * 从Entrust对象中抽取填充JS002
     */
    public String safe(String s) {
        return s == null ? "" : s;
    }

    public JS002(Entrust entrust) {
        if (entrust == null) {
            throw new NullPointerException("entrust is null");
        }

        // 委托单位信息
        PartyDetail principal = entrust.getContent().getPrincipal();

        // 测试类型
        List<String> testType = entrust.getContent().getTestType();
        if (testType != null) {
            for (String value : testType) {
                if (safe(value).equals("软件确认测试")) {
                    this.multiCeShiLeiXing01 = "1";
                } else if (safe(value).startsWith("_0641#toReplaceA1C1_0")) {
                    this.multiCeShiLeiXing02 = "1";
                    Pattern pattern = Pattern.compile("(?<=_0641#toReplaceA1C1_0).*?(?=_0641#toReplaceA2C2_0)");
                    Matcher matcher = pattern.matcher(value);
                    if (matcher.find()) {
                        this.inputCeShiLeiXing02 = matcher.group();
                    }
                } else if (safe(value).equals("成果/技术鉴定测试")) {
                    this.multiCeShiLeiXing03 = "1";
                } else if (safe(value).equals("专项资金验收测试")) {
                    this.multiCeShiLeiXing04 = "1";
                }
            }
        }

        // 委托申请测试软件的软件信息
        Software software = entrust.getContent().getSoftware();
        if (software != null) {
            this.inputRuanJianMingChen = safe(software.getName());
            this.inputBanBenHao = safe(software.getVersion());
            this.inputWeiTuoDanWeiZhongWen = safe(principal.getCompanyCH());
            this.inputWeiTuoDanWeiYingWen = safe(principal.getCompanyEN());
            this.inputKaiFaDanWei = safe(software.getDeveloper());
            if (safe(software.getDeveloperType()).equals("1")) {
                this.singleDanWeiXingZhi01 = "1";
            } else if (safe(software.getDeveloperType()).equals("2")) {
                this.singleDanWeiXingZhi02 = "1";
            } else if (safe(software.getDeveloperType()).equals("3")) {
                this.singleDanWeiXingZhi03 = "1";
            } else if (safe(software.getDeveloperType()).equals("4")) {
                this.singleDanWeiXingZhi04 = "1";
            } else if (safe(software.getDeveloperType()).equals("5")) {
                this.singleDanWeiXingZhi05 = "1";
            } else if (safe(software.getDeveloperType()).equals("6")) {
                this.singleDanWeiXingZhi06 = "1";
            }
            System.out.println("Test : : " + software.getDeveloperType() + "\n");
            this.inputRuanJianMiaoShu = safe(software.getUserDescription());
            this.inputZhuYaoGongNeng = safe(software.getFunctionIntro());

            this.inputRuanJianGuiMo01 = safe(software.getFunctionNums());
            this.inputRuanJianGuiMo02 = safe(software.getFunctionPoint());
            this.inputRuanJianGuiMo03 = safe(software.getCodeLine());
            //
            if (safe(software.getType()).equals("操作系统")) {
                this.singleRuanJianLeiXing0XiTong01 = "1";
            } else if (safe(software.getType()).equals("中文处理系统")) {
                this.singleRuanJianLeiXing0XiTong02 = "1";
            } else if (safe(software.getType()).equals("网络系统")) {
                this.singleRuanJianLeiXing0XiTong03 = "1";
            } else if (safe(software.getType()).equals("嵌入式操作系统")) {
                this.singleRuanJianLeiXing0XiTong04 = "1";
            } else if (safe(software.getType()).equals("其他")) {
                this.singleRuanJianLeiXing0XiTong05 = "1";
            } else if (safe(software.getType()).equals("程序设计语言")) {
                this.singleRuanJianLeiXing0ZhiChi01 = "1";
            } else if (safe(software.getType()).equals("数据库系统设计")) {
                this.singleRuanJianLeiXing0ZhiChi02 = "1";
            } else if (safe(software.getType()).equals("工具软件")) {
                this.singleRuanJianLeiXing0ZhiChi03 = "1";
            } else if (safe(software.getType()).equals("网络通信软件")) {
                this.singleRuanJianLeiXing0ZhiChi04 = "1";
            } else if (safe(software.getType()).equals("中间件")) {
                this.singleRuanJianLeiXing0ZhiChi05 = "1";
            } else if (safe(software.getType()).equals("其他")) {
                this.singleRuanJianLeiXing0ZhiChi06 = "1";
            } else if (safe(software.getType()).equals("行业管理软件")) {
                this.singleRuanJianLeiXing0YingYong01 = "1";
            } else if (safe(software.getType()).equals("办公软件")) {
                this.singleRuanJianLeiXing0YingYong02 = "1";
            } else if (safe(software.getType()).equals("模式识别软件")) {
                this.singleRuanJianLeiXing0YingYong03 = "1";
            } else if (safe(software.getType()).equals("图形图像软件")) {
                this.singleRuanJianLeiXing0YingYong04 = "1";
            } else if (safe(software.getType()).equals("控制软件")) {
                this.singleRuanJianLeiXing0YingYong05 = "1";
            } else if (safe(software.getType()).equals("网络应用软件")) {
                this.singleRuanJianLeiXing0YingYong06 = "1";
            } else if (safe(software.getType()).equals("信息管理软件")) {
                this.singleRuanJianLeiXing0YingYong07 = "1";
            } else if (safe(software.getType()).equals("数据库管理应用软件")) {
                this.singleRuanJianLeiXing0YingYong08 = "1";
            } else if (safe(software.getType()).equals("安全与保密软件")) {
                this.singleRuanJianLeiXing0YingYong09 = "1";
            } else if (safe(software.getType()).equals("嵌入式应用软件")) {
                this.singleRuanJianLeiXing0YingYong010 = "1";
            } else if (safe(software.getType()).equals("教育软件")) {
                this.singleRuanJianLeiXing0YingYong011 = "1";
            } else if (safe(software.getType()).equals("游戏软件")) {
                this.singleRuanJianLeiXing0YingYong012 = "1";
            } else if (safe(software.getType()).equals("其他")) {
                this.singleRuanJianLeiXing0YingYong013 = "1";
            } else if (safe(software.getType()).equals("其他")) {
                this.singleRuanJianLeiXing0QiTa01 = "1";
            }

            List<String> clientOS = software.getClientOS();
            if (clientOS != null) {
                for (String value : clientOS) {
                    if (safe(value).startsWith("Windows")) {
                        this.multiKeHuDuan0Windows = "1";
                        String[] tmp = value.split("\\s+");
                        Pattern pattern = Pattern.compile("(?<=_0641#toReplaceA1C1_1).*?(?=_0641#toReplaceA2C2_1)");
                        Matcher matcher = pattern.matcher(tmp[1]);
                        if (matcher.find()) {
                            this.inputKeHuDuan0Windows = matcher.group();
                        }
                    } else if (safe(value).startsWith("Linux")) {
                        this.multiKeHuDuan0Linux = "1";
                        String[] tmp = value.split("\\s+");
                        Pattern pattern = Pattern.compile("(?<=_0641#toReplaceA1C1_2).*?(?=_0641#toReplaceA2C2_2)");
                        Matcher matcher = pattern.matcher(tmp[1]);
                        if (matcher.find()) {
                            this.inputKeHuDuan0Linux = matcher.group();
                        }
                    } else if (value != null) {
                        this.multiKeHuDuan0QiTa = "1";
                        Pattern pattern = Pattern.compile("(?<=_0641#toReplaceA1C1_3).*?(?=_0641#toReplaceA2C2_3)");
                        Matcher matcher = pattern.matcher(value);
                        if (matcher.find()) {
                            this.inputKeHuDuan0QiTa = matcher.group();
                        }
                    }
                }
            }
            this.inputKeHuDuan0NeiCunYaoQiu = safe(software.getClientMemoryRequirement());
            this.inputKeHuDuan0QiTaYaoQiu = safe(software.getClientOtherRequirement());

            List<String> serverHardArch = software.getServerHardArch();
            if (serverHardArch != null) {
                for (String value : serverHardArch) {
                    if (value.equals("PC服务器")) {
                        this.multiFuWuQiYingJian0PC = "1";
                    } else if (value.equals("UNIX/Linux服务器")) {
                        this.multiFuWuQiYingJian0Linux = "1";
                    } else if (value != null) {
                        this.multiFuWuQiYingJian0QiTa = "1";
                        Pattern pattern = Pattern.compile("(?<=_0641#toReplaceA1C1_4).*?(?=_0641#toReplaceA2C2_4)");
                        Matcher matcher = pattern.matcher(value);
                        if (matcher.find()) {
                            this.inputFuWuQiYingJian0QiTa = matcher.group();
                        }
                    }
                }
            }
            this.inputFuWuQiYingJian0NeiCunYaoQiu = safe(software.getServHardMemoryRequirement());
            this.inputFuWuQiYingJian0YingPanYaoQiu = safe(software.getServHardDiskRequirement());
            this.inputFuWuQiYingJian0QiTaYaoQiu = safe(software.getServHardOtherRequirement());
            this.inputFuWuQiRuanJian0CaoZuoXiTong = safe(software.getServSoftOS());
            this.inputFuWuQiRuanJian0BanBen = safe(software.getServSoftVersion());
            this.inputFuWuQiRuanJian0BianChengYuYan = safe(software.getServSoftProgramLang());

            List<String> servSoftArch = software.getServSoftArch();
            if (servSoftArch != null) {
                for (String value : servSoftArch) {
                    if (value.equals("C/S")) {
                        this.multiFuWuQiRuanJian0GouJia0CS = "1";
                    } else if (value.equals("B/S")) {
                        this.multiFuWuQiRuanJian0GouJia0BS = "1";
                    } else if (value.equals("其他")) {
                        this.multiFuWuQiRuanJian0GouJia0QiTa = "1";
                    }
                }
            }
            this.inputFuWuQiRuanJian0ShuJuKu = safe(software.getServSoftDatabase());
            this.inputFuWuQiRuanJian0ZhongJianJian = safe(software.getServSoftMiddleware());
            this.inputFuWuQiRuanJian0QiTaZhiCheng = safe(software.getServerSideOtherSupport());
            this.inputWangLuoHuanJing = safe(software.getNetworkEnvironment());
        }

        // 测试依据
        List<String> testStandard = entrust.getContent().getTestStandard();
        if (testStandard != null) {
            for (String value : testStandard) {
                if (value.equals("GB/T 25000.51-2010")) {
                    this.multiCeShiYiJu01 = "1";
                } else if (value.equals("GB/T 16260.1-2006")) {
                    this.multiCeShiYiJu02 = "1";
                } else if (value.equals("NST-03-WI12-2011")) {
                    this.multiCeShiYiJu03 = "1";
                } else if (value.equals("NST-03-WI13-2011")) {
                    this.multiCeShiYiJu04 = "1";
                } else if (value.startsWith("_0641#toReplaceA1C1_5")) {
                    this.multiCeShiYiJu05 = "1";
                    System.out.println("Teset");
                    Pattern pattern = Pattern.compile("(?<=_0641#toReplaceA1C1_5).*?(?=_0641#toReplaceA2C2_5)");
                    Matcher matcher = pattern.matcher(value);
                    if (matcher.find()) {
                        this.inputCeShiYiJu05 = matcher.group();
                    }
                }
            }
        }

        // 技术指标
        List<String> techIndex = entrust.getContent().getTechIndex();
        if (techIndex != null) {
            for (String value : techIndex) {
                if (value.equals("功能性")) {
                    this.multiJiShuZhiBiao01 = "1";
                } else if (value.equals("可靠性")) {
                    this.multiJiShuZhiBiao02 = "1";
                } else if (value.equals("易用性性")) {
                    this.multiJiShuZhiBiao03 = "1";
                } else if (value.equals("效率")) {
                    this.multiJiShuZhiBiao04 = "1";
                } else if (value.equals("可维护性")) {
                    this.multiJiShuZhiBiao05 = "1";
                } else if (value.equals("可移植性")) {
                    this.multiJiShuZhiBiao06 = "1";
                } else if (value.equals("代码覆盖度")) {
                    this.multiJiShuZhiBiao07 = "1";
                } else if (value.equals("缺陷检测率")) {
                    this.multiJiShuZhiBiao08 = "1";
                } else if (value.equals("代码风格符合度")) {
                    this.multiJiShuZhiBiao09 = "1";
                } else if (value.equals("代码不符合项检测率")) {
                    this.multiJiShuZhiBiao010 = "1";
                } else if (value.equals("产品说明要求")) {
                    this.multiJiShuZhiBiao011 = "1";
                } else if (value.equals("用户文档集要求")) {
                    this.multiJiShuZhiBiao012 = "1";
                } else if (value.startsWith("_0641#toReplaceA1C1_6")) {
                    this.multiJiShuZhiBiao013 = "1";
                    Pattern pattern = Pattern.compile("(?<=_0641#toReplaceA1C1_6).*?(?=_0641#toReplaceA2C2_6)");
                    Matcher matcher = pattern.matcher(value);
                    if (matcher.find()) {
                        this.inputJiShuZhiBiao013 = matcher.group();
                    }
                    System.out.println("Test: : " + this.inputJiShuZhiBiao013 + "\n");
                }
            }
        }

        // 软件介质
        String softwareMedium = entrust.getContent().getSoftwareMedium();
        if (safe(softwareMedium).equals("1")) {
            this.singleRuanJianJieZhi0GuangPan = "1";
        } else if (safe(softwareMedium).equals("2")) {
            this.singleRuanJianJieZhi0UPan = "1";
        } else if (softwareMedium != null) {
            this.singleRuanJianJieZhi0QiTa = "1";
            Pattern pattern = Pattern.compile("(?<=_0641#toReplaceA1C1_7).*?(?=_0641#toReplaceA2C2_7)");
            Matcher matcher = pattern.matcher(softwareMedium);
            if (matcher.find()) {
                this.inputRuanJianJieZhi0QiTa = matcher.group();
            }
        }
        // 文档资料
        String softwareDoc = entrust.getContent().getDocument();
        this.inputWenDangZiLiao = safe(softwareDoc);
        // 样品处理方式
        String sampleHandling = entrust.getContent().getSampleHandling();
        if (safe(sampleHandling).equals("1")) {
            this.singleYangPingChuLi01 = "1";
        } else if (sampleHandling != null) {
            this.singleYangPingChuLi02 = "1";
        }

        this.inputWeiTuoDanWei0DianHua = safe(principal.getCompanyPhone());
        this.inputWeiTuoDanWei0ChuanZhen = safe(principal.getFax());
        this.inputWeiTuoDanWei0DiZhi = safe(principal.getCompanyAddress());
        this.inputWeiTuoDanWei0YouBian = safe(principal.getZipCode());
        this.inputWeiTuoDanWei0LianXiRen = safe(principal.getContact());
        this.inputWeiTuoDanWei0ShouJi = safe(principal.getContactPhone());
        this.inputWeiTuoDanWei0Email = safe(principal.getContactEmail());
        this.inputWeiTuoDanWei0WangZhi = safe(principal.getCompanyWebsite());

        // 从委托评审里获取，注意判断null
        EntrustReview entrustReview = entrust.getReview();
        if (entrustReview != null) {
            // 密级
            String securityLevel = entrustReview.getSecurityLevel();
            if (safe(securityLevel).equals("无密级")) {
                this.singleMiJi01 = "1";
            } else if (safe(securityLevel).equals("秘密")) {
                this.singleMiJi02 = "1";
            } else if (safe(securityLevel).equals("机密")) {
                this.singleMiJi03 = "1";
            }
            // 查杀病毒
            String checkVirus = entrustReview.getCheckVirus();
            if (safe(checkVirus).equals("已完成")) {
                this.singleChaShaBingDu01 = "1";
            } else if (safe(checkVirus).startsWith("无法完成")) {
                this.singleChaShaBingDu02 = "1";
                this.inputChaShaBingDu02 = checkVirus.substring(4);
            }

            // 材料检查
            List<String> checkMaterial = entrustReview.getCheckMaterial();
            if (checkMaterial != null) {
                for (String value : checkMaterial) {
                    if (value.equals("源代码")) {
                        this.multiCaiLiaoJianCha0CeshiYangPing01 = "1";
                    } else if (value.equals("可执行文件")) {
                        this.multiCaiLiaoJianCha0CeshiYangPing02 = "1";
                    } else if (value.equals("项目计划任务书")) {
                        this.multiCaiLiaoJianCha0XuQiuWenDang01 = "1";
                    } else if (value.equals("需求分析报告")) {
                        this.multiCaiLiaoJianCha0XuQiuWenDang02 = "1";
                    } else if (value.equals("合同")) {
                        this.multiCaiLiaoJianCha0XuQiuWenDang03 = "1";
                    } else if (value.equals("用户手册")) {
                        this.multiCaiLiaoJianCha0YongHuWenDang01 = "1";
                    } else if (value.equals("用户指南")) {
                        this.multiCaiLiaoJianCha0YongHuWenDang02 = "1";
                    } else if (value.equals("操作员手册")) {
                        this.multiCaiLiaoJianCha0CaoZuoWenDang01 = "1";
                    } else if (value.equals("安装手册")) {
                        this.multiCaiLiaoJianCha0CaoZuoWenDang02 = "1";
                    } else if (value.equals("诊断手册")) {
                        this.multiCaiLiaoJianCha0CaoZuoWenDang03 = "1";
                    } else if (value.equals("支持手册")) {
                        this.multiCaiLiaoJianCha0CaoZuoWenDang04 = "1";
                    }
                }
            }

            // 确认意见
            String confirmation = entrustReview.getConfirmation();
            if (safe(confirmation).equals("0")) {
                this.singleQueRenYiJian01 = "1";
            } else if (safe(confirmation).equals("1")) {
                this.singleQueRenYiJian02 = "1";
            } else if (safe(confirmation).equals("2")) {
                this.singleQueRenYiJian03 = "1";
            } else if (safe(confirmation).equals("3")) {
                this.singleQueRenYiJian04 = "1";
            }
            // 受理意见
            String acceptance = entrustReview.getAcceptance();
            if (safe(acceptance).equals("0")) {
                this.singleShouLiYiJian01 = "1";
            } else if (safe(acceptance).equals("1")) {
                this.singleShouLiYiJian02 = "1";
            } else if (safe(acceptance).equals("2")) {
                this.singleShouLiYiJian03 = "1";
            }
            // 测试项目编号
            this.inputCeShiXiangMuBianHao = safe(entrustReview.getSerialNumber());
        }

    }
}

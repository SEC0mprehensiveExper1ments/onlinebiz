package com.njustc.onlinebiz.doc.model;

import com.njustc.onlinebiz.common.model.PartyDetail;
import com.njustc.onlinebiz.common.model.contract.Contract;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JS004 {
  private String inputXiangMuMingChen = "";
  private String inputJiaFang = "";
  private String inputYiFang = "";
  private String inputQianDingDiDian = "";
  private String inputQianDingRiQi0Nian = "";
  private String inputQianDingRiQi0Yue = "";
  private String inputQianDingRiQi0Ri = "";
  private String inputShouCeRuanJian = "";
  private String inputZhiLiangTeXing = "";
  private String inputCeShiFeiYong = "";
  private String inputCeShiFeiYong0Yuan = "";
  private String inputLvXingQiXian = "";
  private String inputZhengGaiCishu = "";
  private String inputZhengGaiTianShu = "";
  private String inputJiaFang0ShouQuanDaiBiao = "";
  private String inputJiaFang0QianZhangRiQi = "";
  private String inputJiaFang0LianXiRen = "";
  private String inputJiaFang0TongXunDiZhi = "";
  private String inputJiaFang0DianHua = "";
  private String inputJiaFang0ChuanZhen = "";
  private String inputJiaFang0KaiHuYinHang = "";
  private String inputJiaFang0ZhangHao = "";
  private String inputJiaFang0YouBian = "";
  private String inputYiFang0ShouQuanDaiBiao = "";
  private String inputYiFang0QianZhangRiQi = "";
  private String inputYiFang0LianXiRen = "";
  private String inputYiFang0TongXunDiZhi = "";
  private String inputYiFang0DianHua = "";
  private String inputYiFang0ChuanZhen = "";
  private String inputYiFang0HuMing = "";
  private String inputYiFang0KaiHuYinHang = "";
  private String inputYiFang0ZhangHao = "";
  private String inputYiFang0YouBian = "";

  public JS004(Contract contract) {
    this.inputXiangMuMingChen = contract.getProjectName();
    //
    PartyDetail jiaFang = contract.getPartyA();
    PartyDetail yiFang = contract.getPartyB();
    this.inputJiaFang = jiaFang.getCompanyCH();
    this.inputYiFang = yiFang.getCompanyCH();
    this.inputQianDingDiDian = contract.getSignedAt();
    // TODO: 签订日期需要确定格式
//    this.inputQianDingRiQi0Nian = ;
//    this.inputQianDingRiQi0Yue = ;
//    this.inputQianDingRiQi0Ri = ;

    this.inputShouCeRuanJian = contract.getTargetSoftware();

    // TODO: 质量特性
//    this.inputZhiLiangTeXing = ;

    // TODO: 合同价款
//    this.inputCeShiFeiYong = contract;
//    this.inputCeShiFeiYong0Yuan = ;

    this.inputLvXingQiXian = Integer.toString(contract.getTotalWorkingDays());
    this.inputZhengGaiCishu = Integer.toString(contract.getRectificationLimit());
    this.inputZhengGaiTianShu = Integer.toString(contract.getRectificationDaysEachTime());

    this.inputJiaFang0ShouQuanDaiBiao = jiaFang.getRepresentative();
    // this.inputJiaFang0QianZhangRiQi = "";
    this.inputJiaFang0LianXiRen = jiaFang.getContact();
    this.inputJiaFang0TongXunDiZhi = jiaFang.getCompanyAddress();
    this.inputJiaFang0DianHua = jiaFang.getCompanyPhone();
    this.inputJiaFang0ChuanZhen = jiaFang.getFax();
    this.inputJiaFang0KaiHuYinHang = jiaFang.getBankName();
    this.inputJiaFang0ZhangHao = jiaFang.getAccount();
    this.inputJiaFang0YouBian = jiaFang.getZipCode();
    this.inputYiFang0ShouQuanDaiBiao = yiFang.getRepresentative();
    // this.inputYiFang0QianZhangRiQi = "";
    this.inputYiFang0LianXiRen = yiFang.getContact();
    this.inputYiFang0TongXunDiZhi = yiFang.getCompanyAddress();
    this.inputYiFang0DianHua = yiFang.getCompanyPhone();
    this.inputYiFang0ChuanZhen = yiFang.getFax();
    this.inputYiFang0HuMing = yiFang.getAccountName();
    this.inputYiFang0KaiHuYinHang = yiFang.getBankName();
    this.inputYiFang0ZhangHao = yiFang.getAccount();
    this.inputYiFang0YouBian = yiFang.getZipCode();
  }

}

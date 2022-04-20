package com.njustc.onlinebiz.doc.domain;

import lombok.Data;

import java.util.List;

@Data
class ZiGongNeng {
  private String inputZiGongNengMingCheng;
  private String inputGongNengShuoMing;

  public String getInputZiGongNengMingCheng() {
    return inputZiGongNengMingCheng;
  }

  public String getInputGongNengShuoMing() {
    return inputGongNengShuoMing;
  }
}

@Data
class GongNeng {
  private String inputGongNengMingCheng;
  private List<ZiGongNeng> inputZiGongNeng;

  public String getInputGongNengMingCheng() {
    return inputGongNengMingCheng;
  }

  public List<ZiGongNeng> getInputZiGongNeng() {
    return inputZiGongNeng;
  }

  public int getZiGongNengSum() {
    return inputZiGongNeng.size();
  }
}

@Data
public class JS003 {
  private String inputRuanJianMingCheng;
  private String inputBanBenHao;
  private List<GongNeng> inputRuanJianGongNengXiangMu;

  public String getInputRuanJianMingCheng() {
    return inputRuanJianMingCheng;
  }

  public String getInputBanBenHao() {
    return inputBanBenHao;
  }

  public List<GongNeng> getInputRuanJianGongNengXiangMu() {
    return inputRuanJianGongNengXiangMu;
  }

  public int getGongNengSum() {
    return inputRuanJianGongNengXiangMu.size();
  }

  public int getZiGongNengSum(int index) {
    return inputRuanJianGongNengXiangMu.get(index).getZiGongNengSum();
  }

  public String getGongNengName(int index) {
    return inputRuanJianGongNengXiangMu.get(index).getInputGongNengMingCheng();
  }

  public String getZiGongNengName(int index, int j) {
    return inputRuanJianGongNengXiangMu
        .get(index)
        .getInputZiGongNeng()
        .get(j)
        .getInputZiGongNengMingCheng();
  }

  public String getZiGongNengShuoMing(int index, int j) {
    return inputRuanJianGongNengXiangMu
        .get(index)
        .getInputZiGongNeng()
        .get(j)
        .getInputGongNengShuoMing();
  }
}

package com.njustc.onlinebiz.doc.model.JS003;

import com.njustc.onlinebiz.common.model.Software;
import com.njustc.onlinebiz.common.model.entrust.Entrust;
import com.njustc.onlinebiz.common.model.Software.Module;
import com.njustc.onlinebiz.common.model.Software.ModuleFunction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JS003 {
    private String inputRuanJianMingCheng = "";
    private String inputBanBenHao = "";
    private List<GongNeng> inputRuanJianGongNengXiangMu = new ArrayList<>();

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

  public JS003(Entrust entrust) {
    if (entrust == null || entrust.getContent() == null) {
      throw new IllegalArgumentException("entrust is null");
    }
    Software software = entrust.getContent().getSoftware();
    if (software != null) {
        this.inputRuanJianMingCheng = software.getName();
        this.inputBanBenHao = software.getVersion();
        // 软件功能列表
        List<Module> modules = software.getModules();
        if (modules != null) {
            for (Module module : modules) {
                GongNeng gongNeng = new GongNeng(module);
                this.inputRuanJianGongNengXiangMu.add(gongNeng);
            }
        }
    }
    //非空性检查
    if (this.inputRuanJianMingCheng == null) {
      this.inputRuanJianMingCheng = "";
    }
    if (this.inputBanBenHao == null) {
      this.inputBanBenHao = "";
    }
    if (this.inputRuanJianGongNengXiangMu == null) {
      this.inputRuanJianGongNengXiangMu = new ArrayList<>();
    }
  }

}

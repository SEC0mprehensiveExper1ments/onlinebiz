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
    //  private String inputZhiLiangTeXing = "";
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
        if (contract == null) {
            throw new IllegalArgumentException("contract is null");
        }
        this.inputXiangMuMingChen = contract.getProjectName();

        PartyDetail jiaFang = contract.getPartyA();
        PartyDetail yiFang = contract.getPartyB();


        this.inputQianDingDiDian = contract.getSignedAt();

        String signedDate = contract.getSignedDate();
        if (signedDate.length() >= 10) {
            this.inputQianDingRiQi0Nian = signedDate.substring(0, 4);
            this.inputQianDingRiQi0Yue = signedDate.substring(5, 7);
            this.inputQianDingRiQi0Ri = signedDate.substring(8, 10);
        }
        this.inputShouCeRuanJian = contract.getTargetSoftware();

        this.inputCeShiFeiYong = contract.getPrice().toString();
//    this.inputCeShiFeiYong0Yuan = ;

        this.inputLvXingQiXian = Integer.toString(contract.getTotalWorkingDays());
        this.inputZhengGaiCishu = Integer.toString(contract.getRectificationLimit());
        this.inputZhengGaiTianShu = Integer.toString(contract.getRectificationDaysEachTime());

        if (jiaFang != null) {
            this.inputJiaFang = jiaFang.getCompanyCH();
            this.inputJiaFang0ShouQuanDaiBiao = jiaFang.getRepresentative();
            // this.inputJiaFang0QianZhangRiQi = "";
            this.inputJiaFang0LianXiRen = jiaFang.getContact();
            this.inputJiaFang0TongXunDiZhi = jiaFang.getCompanyAddress();
            this.inputJiaFang0DianHua = jiaFang.getCompanyPhone();
            this.inputJiaFang0ChuanZhen = jiaFang.getFax();
            this.inputJiaFang0KaiHuYinHang = jiaFang.getBankName();
            this.inputJiaFang0ZhangHao = jiaFang.getAccount();
            this.inputJiaFang0YouBian = jiaFang.getZipCode();
        }
        if (yiFang != null) {
            this.inputYiFang = yiFang.getCompanyCH();
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
        //增加字段的非空性检查
        if (this.inputXiangMuMingChen == null) this.inputXiangMuMingChen = "";
        if (this.inputJiaFang == null) this.inputJiaFang = "";
        if (this.inputYiFang == null) this.inputYiFang = "";
        if (this.inputQianDingDiDian == null) this.inputQianDingDiDian = "";
        if (this.inputShouCeRuanJian == null) this.inputShouCeRuanJian = "";
        if (this.inputCeShiFeiYong == null) this.inputCeShiFeiYong = "";
        if (this.inputJiaFang0ShouQuanDaiBiao == null) this.inputJiaFang0ShouQuanDaiBiao = "";
        if (this.inputJiaFang0LianXiRen == null) this.inputJiaFang0LianXiRen = "";
        if (this.inputJiaFang0TongXunDiZhi == null) this.inputJiaFang0TongXunDiZhi = "";
        if (this.inputJiaFang0DianHua == null) this.inputJiaFang0DianHua = "";
        if (this.inputJiaFang0ChuanZhen == null) this.inputJiaFang0ChuanZhen = "";
        if (this.inputJiaFang0KaiHuYinHang == null) this.inputJiaFang0KaiHuYinHang = "";
        if (this.inputJiaFang0ZhangHao == null) this.inputJiaFang0ZhangHao = "";
        if (this.inputJiaFang0YouBian == null) this.inputJiaFang0YouBian = "";
        if (this.inputYiFang0ShouQuanDaiBiao == null) this.inputYiFang0ShouQuanDaiBiao = "";
        if (this.inputYiFang0LianXiRen == null) this.inputYiFang0LianXiRen = "";
        if (this.inputYiFang0TongXunDiZhi == null) this.inputYiFang0TongXunDiZhi = "";
        if (this.inputYiFang0DianHua == null) this.inputYiFang0DianHua = "";
        if (this.inputYiFang0ChuanZhen == null) this.inputYiFang0ChuanZhen = "";
        if (this.inputYiFang0HuMing == null) this.inputYiFang0HuMing = "";
        if (this.inputYiFang0KaiHuYinHang == null) this.inputYiFang0KaiHuYinHang = "";
        if (this.inputYiFang0ZhangHao == null) this.inputYiFang0ZhangHao = "";
        if (this.inputYiFang0YouBian == null) this.inputYiFang0YouBian = "";
    }

}

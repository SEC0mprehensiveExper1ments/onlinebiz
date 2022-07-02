package com.njustc.onlinebiz.doc.model;


import com.njustc.onlinebiz.common.model.PartyDetail;
import com.njustc.onlinebiz.common.model.contract.Contract;
import com.njustc.onlinebiz.common.model.contract.NonDisclosureAgreement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JS005 {
    private String inputJiaFang = "";
    private String inputWeiTuoXiangMu = "";

    public String safe(String s) {
        return s == null ? "" : s;
    }
    public JS005(Contract contract) {
        // NonDisclosureAgreement nonDisclosureAgreement = contract.getNonDisclosureAgreement();
        if (contract == null) {
            throw new IllegalArgumentException("contract is null");
        }
        PartyDetail jiaFang = contract.getPartyA();
        if (jiaFang != null) {
            this.inputJiaFang = jiaFang.getCompanyCH();
        }
        this.inputWeiTuoXiangMu = contract.getProjectName();
        //非空性检查
        if (this.inputJiaFang == null) {
            this.inputJiaFang = "";
        }
        if (this.inputWeiTuoXiangMu == null) {
            this.inputWeiTuoXiangMu = "";
        }
    }
}

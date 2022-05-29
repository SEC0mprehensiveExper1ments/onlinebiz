package com.njustc.onlinebiz.doc.model;


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

    public JS005(Contract contract) {
        // NonDisclosureAgreement nonDisclosureAgreement = contract.getNonDisclosureAgreement();
        this.inputJiaFang = contract.getPartyA().getCompanyCH();
        this.inputWeiTuoXiangMu = contract.getProjectName();
    }
}

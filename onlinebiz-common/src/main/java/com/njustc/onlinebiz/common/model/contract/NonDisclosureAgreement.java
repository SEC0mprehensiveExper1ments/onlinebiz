package com.njustc.onlinebiz.common.model.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NonDisclosureAgreement {

    // 甲方名称
    private String partyAName;

    // 乙方名称
    private String partyBName;

}

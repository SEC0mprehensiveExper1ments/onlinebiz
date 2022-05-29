package com.njustc.onlinebiz.doc.model.JS003;

import com.njustc.onlinebiz.common.model.Software.ModuleFunction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ZiGongNeng {
    private String inputZiGongNengMingCheng = "";
    private String inputGongNengShuoMing = "";

    public String getInputZiGongNengMingCheng() {
        return inputZiGongNengMingCheng;
    }

    public String getInputGongNengShuoMing() {
        return inputGongNengShuoMing;
    }

    public ZiGongNeng(ModuleFunction function) {
        this.inputZiGongNengMingCheng = function.getFunctionName();
        this.inputGongNengShuoMing = function.getFunctionDescription();
    }
}

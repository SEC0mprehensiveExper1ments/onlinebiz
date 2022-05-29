package com.njustc.onlinebiz.doc.model.JS003;

import com.njustc.onlinebiz.common.model.Software;
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
public class GongNeng {
    private String inputGongNengMingCheng = "";
    private List<ZiGongNeng> inputZiGongNeng = new ArrayList<>();

    public String getInputGongNengMingCheng() {
        return inputGongNengMingCheng;
    }

    public List<ZiGongNeng> getInputZiGongNeng() {
        return inputZiGongNeng;
    }

    public int getZiGongNengSum() {
        return inputZiGongNeng.size();
    }

    public GongNeng(Module module) {
        this.inputGongNengMingCheng = module.getModuleName();
        List<ModuleFunction> functions = module.getFunctions();
        for(ModuleFunction function: functions) {
            ZiGongNeng ziGongNeng = new ZiGongNeng(function);
            this.inputZiGongNeng.add(ziGongNeng);
        }
    }
}
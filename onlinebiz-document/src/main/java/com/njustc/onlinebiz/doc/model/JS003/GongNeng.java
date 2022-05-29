package com.njustc.onlinebiz.doc.model.JS003;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public
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

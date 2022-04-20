package com.njustc.onlinebiz.doc.domain;

import lombok.Data;

@Data
public class JS004 {
    private String xiangMuMingCheng;
    private String weiTuoFangJiaFang;
    private String shouTuoFangYiFang;
    private String qianDingDiDian;
    private String qianDingRiQi_Nian;
    private String qianDingRiQi_Yue;
    private String qianDingRiQi_Ri;

    public String getXiangMuMingCheng() {
        return xiangMuMingCheng;
    }

    public String getWeiTuoFangJiaFang() {
        return weiTuoFangJiaFang;
    }

    public String getShouTuoFangYiFang() {
        return shouTuoFangYiFang;
    }

    public String getQianDingDiDian() {
        return qianDingDiDian;
    }

    public String getQianDingRiQi_Nian() {
        return qianDingRiQi_Nian;
    }

    public String getQianDingRiQi_Yue() {
        return qianDingRiQi_Yue;
    }

    public String getQianDingRiQi_Ri() {
        return qianDingRiQi_Ri;
    }
}

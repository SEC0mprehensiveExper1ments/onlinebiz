package com.njustc.onlinebiz.doc.model;

import com.njustc.onlinebiz.common.model.entrust.Entrust;
import com.njustc.onlinebiz.common.model.entrust.EntrustContent;
import com.njustc.onlinebiz.common.model.entrust.SoftwareDocReview;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JS014 {

    private String inputRuanJianMingChen = "";
    private String inputBanBenHao = "";
    private String inputWeiTuoDanWei = "";
    private String inputPingShenRen = "";
    private String inputPingShenShiJian0Nian = "";
    private String inputPingShenShiJian0Yue = "";
    private String inputPingShenShiJian0Ri = "";
    private String inputPingShenJieGuo011 = "";
    private String inputPingShenJieGuo0121 = "";
    private String inputPingShenJieGuo0122 = "";
    private String inputPingShenJieGuo0123 = "";
    private String inputPingShenJieGuo0131 = "";
    private String inputPingShenJieGuo0132 = "";
    private String inputPingShenJieGuo0133 = "";
    private String inputPingShenJieGuo014 = "";
    private String inputPingShenJieGuo015 = "";
    private String inputPingShenJieGuo016 = "";
    private String inputPingShenJieGuo017 = "";
    private String inputPingShenJieGuo018 = "";
    private String inputPingShenJieGuo019 = "";
    private String inputPingShenJieGuo0110 = "";
    private String inputPingShenShuoMing011 = "";
    private String inputPingShenShuoMing0121 = "";
    private String inputPingShenShuoMing0122 = "";
    private String inputPingShenShuoMing0123 = "";
    private String inputPingShenShuoMing0131 = "";
    private String inputPingShenShuoMing0132 = "";
    private String inputPingShenShuoMing0133 = "";
    private String inputPingShenShuoMing014 = "";
    private String inputPingShenShuoMing015 = "";
    private String inputPingShenShuoMing016 = "";
    private String inputPingShenShuoMing017 = "";
    private String inputPingShenShuoMing018 = "";
    private String inputPingShenShuoMing019 = "";
    private String inputPingShenShuoMing0110 = "";
    private String inputPingShenJieGuo0211 = "";
    private String inputPingShenJieGuo0212 = "";
    private String inputPingShenJieGuo0213 = "";
    private String inputPingShenJieGuo0214 = "";
    private String inputPingShenJieGuo0215 = "";
    private String inputPingShenJieGuo0216 = "";
    private String inputPingShenJieGuo0217 = "";
    private String inputPingShenJieGuo0218 = "";
    private String inputPingShenJieGuo0219 = "";
    private String inputPingShenJieGuo02110 = "";
    private String inputPingShenJieGuo02111 = "";
    private String inputPingShenJieGuo0221 = "";
    private String inputPingShenJieGuo0222 = "";
    private String inputPingShenJieGuo0231 = "";
    private String inputPingShenJieGuo0241 = "";
    private String inputPingShenJieGuo0242 = "";
    private String inputPingShenJieGuo0251 = "";
    private String inputPingShenJieGuo0261 = "";
    private String inputPingShenJieGuo0262 = "";
    private String inputPingShenJieGuo0263 = "";
    private String inputPingShenShuoMing0211 = "";
    private String inputPingShenShuoMing0212 = "";
    private String inputPingShenShuoMing0213 = "";
    private String inputPingShenShuoMing0214 = "";
    private String inputPingShenShuoMing0215 = "";
    private String inputPingShenShuoMing0216 = "";
    private String inputPingShenShuoMing0217 = "";
    private String inputPingShenShuoMing0218 = "";
    private String inputPingShenShuoMing0219 = "";
    private String inputPingShenShuoMing02110 = "";
    private String inputPingShenShuoMing02111 = "";
    private String inputPingShenShuoMing0221 = "";
    private String inputPingShenShuoMing0222 = "";
    private String inputPingShenShuoMing0231 = "";
    private String inputPingShenShuoMing0241 = "";
    private String inputPingShenShuoMing0242 = "";
    private String inputPingShenShuoMing0251 = "";
    private String inputPingShenShuoMing0261 = "";
    private String inputPingShenShuoMing0262 = "";
    private String inputPingShenShuoMing0263 = "";

    public String safe(String s) {
        return s == null ? "" : s;
    }

    public JS014(Entrust entrust) {
        SoftwareDocReview softwareDocReview = entrust.getSoftwareDocReview();
        //检查各个字段的非空性
        if (softwareDocReview == null) {
            throw new IllegalArgumentException("SoftwareDocReview is null");
        }
        EntrustContent entrustContent = entrust.getContent();
        if (entrustContent != null) {
            this.inputRuanJianMingChen = safe(entrustContent.getSoftware().getName());
            this.inputBanBenHao = safe(entrustContent.getSoftware().getVersion());
        }
        this.inputWeiTuoDanWei = "高校评审组";
        this.inputPingShenRen = safe(softwareDocReview.getReviewer());
        this.inputPingShenShiJian0Nian = Integer.toString(entrust.getSoftwareDocReview().getYear());
        this.inputPingShenShiJian0Yue = Integer.toString(entrust.getSoftwareDocReview().getMonth());
        this.inputPingShenShiJian0Ri = Integer.toString(entrust.getSoftwareDocReview().getDay());

        List<SoftwareDocReview.ReviewRow> reviewRows = entrust.getSoftwareDocReview().getComments();
        if (reviewRows != null && reviewRows.size() == 34) {
            //检查各个字段的非空性
            this.inputPingShenJieGuo011 = safe(reviewRows.get(0).getResult());
            this.inputPingShenJieGuo0121 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0122 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0123 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0131 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0132 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0133 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo014 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo015 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo016 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo017 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo018 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo019 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0110 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0211 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0212 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0213 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0214 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0215 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0216 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0217 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0218 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0219 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo02110 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo02111 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0221 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0222 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0231 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0241 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0242 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0251 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0261 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0262 = safe(reviewRows.get(1).getResult());
            this.inputPingShenJieGuo0263 = safe(reviewRows.get(1).getResult());


            this.inputPingShenShuoMing011 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0121 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0122 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0123 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0131 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0132 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0133 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing014 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing015 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing016 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing017 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing018 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing019 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0110 = safe(reviewRows.get(0).getDescription());

            this.inputPingShenShuoMing0211 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0212 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0213 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0214 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0215 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0216 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0217 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0218 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0219 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing02110 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing02111 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0221 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0222 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0231 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0241 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0242 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0251 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0261 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0262 = safe(reviewRows.get(0).getDescription());
            this.inputPingShenShuoMing0263 = safe(reviewRows.get(0).getDescription());

        }
    }
}

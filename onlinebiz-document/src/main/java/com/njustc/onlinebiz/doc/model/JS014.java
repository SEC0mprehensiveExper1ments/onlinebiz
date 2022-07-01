package com.njustc.onlinebiz.doc.model;

import com.njustc.onlinebiz.common.model.entrust.Entrust;
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

    public JS014(Entrust entrust) {
        //检查各个字段的非空性
        if (entrust.getSoftwareDocReview().getSoftwareName() != null){
            this.inputRuanJianMingChen = entrust.getSoftwareDocReview().getSoftwareName();
        }
        if (entrust.getSoftwareDocReview().getSoftwareVersion() != null){
            this.inputBanBenHao = entrust.getSoftwareDocReview().getSoftwareVersion();
        }
        this.inputWeiTuoDanWei = "高校评审组";
        if (entrust.getSoftwareDocReview().getReviewer() != null){
            this.inputPingShenRen = entrust.getSoftwareDocReview().getReviewer();
        }
        this.inputPingShenShiJian0Nian = Integer.toString(entrust.getSoftwareDocReview().getYear());
        this.inputPingShenShiJian0Yue = Integer.toString(entrust.getSoftwareDocReview().getMonth());
        this.inputPingShenShiJian0Ri = Integer.toString(entrust.getSoftwareDocReview().getDay());
        List<SoftwareDocReview.ReviewRow> reviewRows = entrust.getSoftwareDocReview().getComments();
        if (reviewRows != null) {
            //检查各个字段的非空性
            if (reviewRows.get(0).getResult() != null) {
                this.inputPingShenJieGuo011 = reviewRows.get(0).getResult();
            }
            if (reviewRows.get(1).getResult() != null) {
                this.inputPingShenJieGuo0121 = reviewRows.get(1).getResult();
            }
            if (reviewRows.get(2).getResult() != null) {
                this.inputPingShenJieGuo0122 = reviewRows.get(2).getResult();
            }
            if (reviewRows.get(3).getResult() != null) {
                this.inputPingShenJieGuo0123 = reviewRows.get(3).getResult();
            }
            if (reviewRows.get(4).getResult() != null) {
                this.inputPingShenJieGuo0131 = reviewRows.get(4).getResult();
            }
            if (reviewRows.get(5).getResult() != null) {
                this.inputPingShenJieGuo0132 = reviewRows.get(5).getResult();
            }
            if (reviewRows.get(6).getResult() != null) {
                this.inputPingShenJieGuo0133 = reviewRows.get(6).getResult();
            }
            if (reviewRows.get(7).getResult() != null) {
                this.inputPingShenJieGuo014 = reviewRows.get(7).getResult();
            }
            if (reviewRows.get(8).getResult() != null) {
                this.inputPingShenJieGuo015 = reviewRows.get(8).getResult();
            }
            if (reviewRows.get(9).getResult() != null) {
                this.inputPingShenJieGuo016 = reviewRows.get(9).getResult();
            }
            if (reviewRows.get(10).getResult() != null) {
                this.inputPingShenJieGuo017 = reviewRows.get(10).getResult();
            }
            if (reviewRows.get(11).getResult() != null) {
                this.inputPingShenJieGuo018 = reviewRows.get(11).getResult();
            }
            if (reviewRows.get(12).getResult() != null) {
                this.inputPingShenJieGuo019 = reviewRows.get(12).getResult();
            }
            if (reviewRows.get(13).getResult() != null) {
                this.inputPingShenJieGuo0110 = reviewRows.get(13).getResult();
            }

            if (reviewRows.get(0).getDescription() != null) {
                this.inputPingShenShuoMing011 = reviewRows.get(0).getDescription();
            }
            if (reviewRows.get(1).getDescription() != null) {
                this.inputPingShenShuoMing0121 = reviewRows.get(1).getDescription();
            }
            if (reviewRows.get(2).getDescription() != null) {
                this.inputPingShenShuoMing0122 = reviewRows.get(2).getDescription();
            }
            if (reviewRows.get(3).getDescription() != null) {
                this.inputPingShenShuoMing0123 = reviewRows.get(3).getDescription();
            }
            if (reviewRows.get(4).getDescription() != null) {
                this.inputPingShenShuoMing0131 = reviewRows.get(4).getDescription();
            }
            if (reviewRows.get(5).getDescription() != null) {
                this.inputPingShenShuoMing0132 = reviewRows.get(5).getDescription();
            }
            if (reviewRows.get(6).getDescription() != null) {
                this.inputPingShenShuoMing0133 = reviewRows.get(6).getDescription();
            }
            if (reviewRows.get(7).getDescription() != null) {
                this.inputPingShenShuoMing014 = reviewRows.get(7).getDescription();
            }
            if (reviewRows.get(8).getDescription() != null) {
                this.inputPingShenShuoMing015 = reviewRows.get(8).getDescription();
            }
            if (reviewRows.get(9).getDescription() != null) {
                this.inputPingShenShuoMing016 = reviewRows.get(9).getDescription();
            }
            if (reviewRows.get(10).getDescription() != null) {
                this.inputPingShenShuoMing017 = reviewRows.get(10).getDescription();
            }
            if (reviewRows.get(11).getDescription() != null) {
                this.inputPingShenShuoMing018 = reviewRows.get(11).getDescription();
            }
            if (reviewRows.get(12).getDescription() != null) {
                this.inputPingShenShuoMing019 = reviewRows.get(12).getDescription();
            }
            if (reviewRows.get(13).getDescription() != null) {
                this.inputPingShenShuoMing0110 = reviewRows.get(13).getDescription();
            }

            if (reviewRows.get(14).getResult() != null) {
                this.inputPingShenJieGuo0211 = reviewRows.get(14).getResult();
            }
            if (reviewRows.get(15).getResult() != null) {
                this.inputPingShenJieGuo0221 = reviewRows.get(15).getResult();
            }
            if (reviewRows.get(16).getResult() != null) {
                this.inputPingShenJieGuo0222 = reviewRows.get(16).getResult();
            }
            if (reviewRows.get(17).getResult() != null) {
                this.inputPingShenJieGuo0231 = reviewRows.get(17).getResult();
            }
            if (reviewRows.get(18).getResult() != null) {
                this.inputPingShenJieGuo0241 = reviewRows.get(18).getResult();
            }
            if (reviewRows.get(19).getResult() != null) {
                this.inputPingShenJieGuo0242 = reviewRows.get(19).getResult();
            }
            if (reviewRows.get(20).getResult() != null) {
                this.inputPingShenJieGuo0251 = reviewRows.get(20).getResult();
            }
            if (reviewRows.get(21).getResult() != null) {
                this.inputPingShenJieGuo0261 = reviewRows.get(21).getResult();
            }
            if (reviewRows.get(22).getResult() != null) {
                this.inputPingShenJieGuo0262 = reviewRows.get(22).getResult();
            }
            if (reviewRows.get(23).getResult() != null) {
                this.inputPingShenJieGuo0263 = reviewRows.get(23).getResult();
            }

            if (reviewRows.get(14).getDescription() != null) {
                this.inputPingShenShuoMing0211 = reviewRows.get(14).getDescription();
            }
            if (reviewRows.get(15).getDescription() != null) {
                this.inputPingShenShuoMing0221 = reviewRows.get(15).getDescription();
            }
            if (reviewRows.get(16).getDescription() != null) {
                this.inputPingShenShuoMing0222 = reviewRows.get(16).getDescription();
            }
            if (reviewRows.get(17).getDescription() != null) {
                this.inputPingShenShuoMing0231 = reviewRows.get(17).getDescription();
            }
            if (reviewRows.get(18).getDescription() != null) {
                this.inputPingShenShuoMing0241 = reviewRows.get(18).getDescription();
            }
            if (reviewRows.get(19).getDescription() != null) {
                this.inputPingShenShuoMing0242 = reviewRows.get(19).getDescription();
            }
            if (reviewRows.get(20).getDescription() != null) {
                this.inputPingShenShuoMing0251 = reviewRows.get(20).getDescription();
            }
            if (reviewRows.get(21).getDescription() != null) {
                this.inputPingShenShuoMing0261 = reviewRows.get(21).getDescription();
            }
            if (reviewRows.get(22).getDescription() != null) {
                this.inputPingShenShuoMing0262 = reviewRows.get(22).getDescription();
            }
            if (reviewRows.get(23).getDescription() != null) {
                this.inputPingShenShuoMing0263 = reviewRows.get(23).getDescription();
            }

        }
    }
}

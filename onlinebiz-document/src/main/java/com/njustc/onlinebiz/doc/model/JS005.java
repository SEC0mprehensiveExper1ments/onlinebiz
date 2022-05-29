package com.njustc.onlinebiz.doc.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JS005 {
    private String inputJiaFang;
    private String inputWeiTuoXiangMu;
}

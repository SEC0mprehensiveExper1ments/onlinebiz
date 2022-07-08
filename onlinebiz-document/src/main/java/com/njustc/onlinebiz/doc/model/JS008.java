package com.njustc.onlinebiz.doc.model;


import com.njustc.onlinebiz.common.model.test.testcase.Testcase;
import com.njustc.onlinebiz.common.model.test.testcase.Testcase.TestcaseList;
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
public class JS008 {
    //所有测试用例记录
    private List<TestcaseList> testcases = new ArrayList<>();

    public JS008(Testcase testcase) {
        if (testcase == null) {
            throw new IllegalArgumentException("testcase is null");
        }
        this.testcases = testcase.getTestcases();
        if (this.testcases == null) {
            this.testcases = new ArrayList<>();
        }
    }

}

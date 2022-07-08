package com.njustc.onlinebiz.doc.model;

import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList;
import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList.TestRecord;
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
public class JS009 {

    //所有软件测试记录
    private List<TestRecord> testRecords = new ArrayList<>();

    public JS009(TestRecordList testRecordList) {
        if (testRecordList == null) {
            throw new IllegalArgumentException("testRecordList is null");
        }
        this.testRecords = testRecordList.getTestRecords();
        if (this.testRecords == null) {
            this.testRecords = new ArrayList<>();
        }
    }
}

package com.njustc.onlinebiz.doc;

import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList.TestRecord;
import com.njustc.onlinebiz.doc.model.JS009;
import com.njustc.onlinebiz.doc.service.DocServiceJS009;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class JS009Test {

    @Autowired
    DocServiceJS009 docServiceJS009;

    @Test
    void testFillJS009Success() {
        List<TestRecord> testRecords = new ArrayList<>();
        testRecords.add(new TestRecord("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        testRecords.add(new TestRecord("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        testRecords.add(new TestRecord("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        testRecords.add(new TestRecord("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        testRecords.add(new TestRecord("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        testRecords.add(new TestRecord("你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1", "你好1"));
        JS009 newJson = new JS009().setTestRecords(testRecords);
        System.out.println(docServiceJS009.fill("1111", newJson));
    }
}

package com.njustc.onlinebiz.doc;


import com.netflix.discovery.converters.Auto;
import com.njustc.onlinebiz.doc.service.DocServiceJS001;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JS001Test {

    @Autowired
    DocServiceJS001 docServiceJS001;

    @Test
    void testFillJS002Success() {
        System.out.println(docServiceJS001.fill());
    }
}

package com.njustc.onlinebiz.doc;

import com.njustc.onlinebiz.doc.service.DocServiceJS001;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application.yml")
public class JS001Test {

    @Autowired
    DocServiceJS001 docServiceJS001;

    @Test
    void testFillJS001Success() {
        System.out.println(docServiceJS001.fill());
    }
}

package com.njustc.onlinebiz.doc;

import com.njustc.onlinebiz.common.model.entrust.EntrustQuote;
import com.njustc.onlinebiz.doc.model.EntrustQuoteDoc;
import com.njustc.onlinebiz.doc.service.DocServiceEntrustQuote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class EntrustQuoteDocTest {

    @Autowired
    DocServiceEntrustQuote docServiceEntrustQuote;

    @Test
    void testFillEntrustQuoteSuccess() {
        List<EntrustQuote.EntrustQuoteRow> rowList=new ArrayList<>();
        rowList.add(new EntrustQuote.EntrustQuoteRow("1","1",10.1,"1",10.1));
        rowList.add(new EntrustQuote.EntrustQuoteRow("2","2",20.2,"2",20.2));
        EntrustQuoteDoc newJson= new EntrustQuoteDoc()
                  .setQuotationDate("2020-01-01")
                  .setEffectiveDate("2020-01-01")
                  .setBankName("中国银行")
                  .setAccount("123456789")
                  .setAccountName("张三")
                  .setSoftwareName("软件名称")
                  .setRowList(rowList)
                  .setSubTotal(30.3)
                  .setTaxRate(0.1)
                  .setTotal(30.3)
                  .setProvider("张三")
                  .setSignature("张三");
        System.out.println(docServiceEntrustQuote.fill("1111",newJson));
    }
}

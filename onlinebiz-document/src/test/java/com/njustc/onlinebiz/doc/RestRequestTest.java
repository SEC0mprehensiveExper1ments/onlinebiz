package com.njustc.onlinebiz.doc;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.contract.Contract;
import com.njustc.onlinebiz.common.model.entrust.Entrust;
import com.njustc.onlinebiz.common.model.test.review.EntrustTestReview;
import com.njustc.onlinebiz.common.model.test.review.ReportReview;
import com.njustc.onlinebiz.common.model.test.review.SchemeReview;
import com.njustc.onlinebiz.common.model.test.testcase.Testcase;
import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList;
import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList;
import com.njustc.onlinebiz.doc.exception.DownloadDAOFailureException;
import com.njustc.onlinebiz.doc.exception.DownloadNotFoundException;
import com.njustc.onlinebiz.doc.exception.DownloadPermissionDeniedException;
import com.njustc.onlinebiz.doc.service.RestRequestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;


public class RestRequestTest {

    private final RestTemplate restTemplate = mock(RestTemplate .class);
    private final RestRequestService restRequestService = new RestRequestService(restTemplate);

    @Test
    public void testGetEntrustById() {
        Entrust entrustMock = mock(Entrust.class);
        // 测试成功返回
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.ok(entrustMock));
        Assertions.assertDoesNotThrow(()->
                restRequestService.getEntrustById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 bad_request
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.badRequest().build());
        Assertions.assertThrows(DownloadPermissionDeniedException.class, ()->
                restRequestService.getEntrustById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 not_found
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.notFound().build());
        Assertions.assertThrows(DownloadNotFoundException.class, ()->
                restRequestService.getEntrustById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 其他问题(INTERNAL_SERVER_ERROR)
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.internalServerError().build());
        Assertions.assertThrows(DownloadDAOFailureException.class, ()->
                restRequestService.getEntrustById(new Object().toString(), 2L, Role.MARKETER));
    }

    @Test
    public void testGetContractById() {
        Contract contractMock = mock(Contract.class);
        // 测试成功返回
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.ok(contractMock));
        Assertions.assertDoesNotThrow(()->
                restRequestService.getContractById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 bad_request
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.badRequest().build());
        Assertions.assertThrows(DownloadPermissionDeniedException.class, ()->
                restRequestService.getContractById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 not_found
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.notFound().build());
        Assertions.assertThrows(DownloadNotFoundException.class, ()->
                restRequestService.getContractById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 其他问题(INTERNAL_SERVER_ERROR)
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.internalServerError().build());
        Assertions.assertThrows(DownloadDAOFailureException.class, ()->
                restRequestService.getContractById(new Object().toString(), 2L, Role.MARKETER));
    }

    @Test
    public void testGetTestcase() {
        Testcase testcaseMock = mock(Testcase.class);
        // 测试成功返回
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.ok(testcaseMock));
        Assertions.assertDoesNotThrow(()->
                restRequestService.getTestcaseById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 bad_request
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.badRequest().build());
        Assertions.assertThrows(DownloadPermissionDeniedException.class, ()->
                restRequestService.getTestcaseById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 not_found
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.notFound().build());
        Assertions.assertThrows(DownloadNotFoundException.class, ()->
                restRequestService.getTestcaseById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 其他问题(INTERNAL_SERVER_ERROR)
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.internalServerError().build());
        Assertions.assertThrows(DownloadDAOFailureException.class, ()->
                restRequestService.getTestcaseById(new Object().toString(), 2L, Role.MARKETER));
    }

    @Test
    public void testGetTestRecordListById() {
        TestRecordList testRecordListMock = mock(TestRecordList.class);
        // 测试成功返回
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.ok(testRecordListMock));
        Assertions.assertDoesNotThrow(()->
                restRequestService.getTestRecordListById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 bad_request
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.badRequest().build());
        Assertions.assertThrows(DownloadPermissionDeniedException.class, ()->
                restRequestService.getTestRecordListById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 not_found
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.notFound().build());
        Assertions.assertThrows(DownloadNotFoundException.class, ()->
                restRequestService.getTestRecordListById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 其他问题(INTERNAL_SERVER_ERROR)
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.internalServerError().build());
        Assertions.assertThrows(DownloadDAOFailureException.class, ()->
                restRequestService.getTestRecordListById(new Object().toString(), 2L, Role.MARKETER));
    }

    @Test
    public void testGetTestIssueListById() {
        TestIssueList testIssueListMock = mock(TestIssueList.class);
        // 测试成功返回
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.ok(testIssueListMock));
        Assertions.assertDoesNotThrow(()->
                restRequestService.getTestIssueListById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 bad_request
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.badRequest().build());
        Assertions.assertThrows(DownloadPermissionDeniedException.class, ()->
                restRequestService.getTestIssueListById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 not_found
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.notFound().build());
        Assertions.assertThrows(DownloadNotFoundException.class, ()->
                restRequestService.getTestIssueListById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 其他问题(INTERNAL_SERVER_ERROR)
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.internalServerError().build());
        Assertions.assertThrows(DownloadDAOFailureException.class, ()->
                restRequestService.getTestIssueListById(new Object().toString(), 2L, Role.MARKETER));
    }

    @Test
    public void testGetEntrustTestReviewById() {
        EntrustTestReview entrustTestReviewMock = mock(EntrustTestReview.class);
        // 测试成功返回
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.ok(entrustTestReviewMock));
        Assertions.assertDoesNotThrow(()->
                restRequestService.getEntrustTestReviewById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 bad_request
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.badRequest().build());
        Assertions.assertThrows(DownloadPermissionDeniedException.class, ()->
                restRequestService.getEntrustTestReviewById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 not_found
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.notFound().build());
        Assertions.assertThrows(DownloadNotFoundException.class, ()->
                restRequestService.getEntrustTestReviewById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 其他问题(INTERNAL_SERVER_ERROR)
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.internalServerError().build());
        Assertions.assertThrows(DownloadDAOFailureException.class, ()->
                restRequestService.getEntrustTestReviewById(new Object().toString(), 2L, Role.MARKETER));
    }

    @Test
    public void testGetSchemeReviewById() {
        SchemeReview schemeReviewMock = mock(SchemeReview.class);
        // 测试成功返回
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.ok(schemeReviewMock));
        Assertions.assertDoesNotThrow(()->
                restRequestService.getSchemeReviewById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 bad_request
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.badRequest().build());
        Assertions.assertThrows(DownloadPermissionDeniedException.class, ()->
                restRequestService.getSchemeReviewById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 not_found
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.notFound().build());
        Assertions.assertThrows(DownloadNotFoundException.class, ()->
                restRequestService.getSchemeReviewById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 其他问题(INTERNAL_SERVER_ERROR)
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.internalServerError().build());
        Assertions.assertThrows(DownloadDAOFailureException.class, ()->
                restRequestService.getSchemeReviewById(new Object().toString(), 2L, Role.MARKETER));
    }

    @Test
    public void testGetReportReviewById() {
        ReportReview reportReviewMock = mock(ReportReview.class);
        // 测试成功返回
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.ok(reportReviewMock));
        Assertions.assertDoesNotThrow(()->
                restRequestService.getReportReviewById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 bad_request
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.badRequest().build());
        Assertions.assertThrows(DownloadPermissionDeniedException.class, ()->
                restRequestService.getReportReviewById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 not_found
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.notFound().build());
        Assertions.assertThrows(DownloadNotFoundException.class, ()->
                restRequestService.getReportReviewById(new Object().toString(), 2L, Role.MARKETER));
        // 测试返回 其他问题(INTERNAL_SERVER_ERROR)
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.internalServerError().build());
        Assertions.assertThrows(DownloadDAOFailureException.class, ()->
                restRequestService.getReportReviewById(new Object().toString(), 2L, Role.MARKETER));
    }

}

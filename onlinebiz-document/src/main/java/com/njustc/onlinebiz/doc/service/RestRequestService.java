package com.njustc.onlinebiz.doc.service;


import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.contract.Contract;
import com.njustc.onlinebiz.common.model.entrust.Entrust;
import com.njustc.onlinebiz.common.model.entrust.EntrustQuote;
import com.njustc.onlinebiz.common.model.test.report.Report;
import com.njustc.onlinebiz.common.model.test.review.EntrustTestReview;
import com.njustc.onlinebiz.common.model.test.review.ReportReview;
import com.njustc.onlinebiz.common.model.test.review.SchemeReview;
import com.njustc.onlinebiz.common.model.test.scheme.Scheme;
import com.njustc.onlinebiz.common.model.test.testcase.Testcase;
import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList;
import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList;
import com.njustc.onlinebiz.doc.exception.DownloadDAOFailureException;
import com.njustc.onlinebiz.doc.exception.DownloadNotFoundException;
import com.njustc.onlinebiz.doc.exception.DownloadPermissionDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestRequestService {

    private static final String ENTRUST_SERVICE = "http://onlinebiz-entrust";
    private static final String CONTRACT_SERVICE = "http://onlinebiz-contract";
    private static final String TEST_SERVICE = "http://onlinebiz-test";
    private final RestTemplate restTemplate;

    public RestRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 通过 entrustId 向entrust服务获取对象，以供后续生成文档并下载
     * @param entrustId 待下载的委托 id
     * @param userId 操作的用户 id
     * @param userRole 操作的用户角色
     * @return 若成功从entrust服务中获得对象，则返回；否则，返回异常信息
     * */
    public Entrust getEntrustById(String entrustId, Long userId, Role userRole) {
        // 调用entrust服务的getEntrust的接口
        String params = "?userId=" + userId + "&userRole=" + userRole;
        String url = ENTRUST_SERVICE + "/api/entrust/" + entrustId;
        ResponseEntity<Entrust> responseEntity = restTemplate.getForEntity(url + params, Entrust.class);
        // 检查委托 id 及权限的有效性
        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN || responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new DownloadPermissionDeniedException("无权下载该文件");
        }
        else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new DownloadNotFoundException("未找到该委托ID");
        }
        else if (responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new DownloadDAOFailureException("其他问题");
        }
        Entrust entrust = responseEntity.getBody();

        return entrust;
    }

    /**
     * 通过 contracId 向contract服务获取对象，以供后续生成文档并下载
     * @param contracId 待下载的合同 id
     * @param userId 操作的用户 id
     * @param userRole 操作的用户角色
     * @return 若成功从contract服务中获得对象，则返回；否则，返回异常信息
     * */
    public Contract getContractById(String contracId, Long userId, Role userRole) {
        // 调用contract服务的getContract的接口
        String params = "?userId=" + userId + "&userRole=" + userRole;
        String url = CONTRACT_SERVICE + "/api/contract/" + contracId;
        ResponseEntity<Contract> responseEntity = restTemplate.getForEntity(url + params, Contract.class);
        // 检查委托 id 及权限的有效性
        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN || responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new DownloadPermissionDeniedException("无权下载该文件");
        }
        else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new DownloadNotFoundException("未找到该委托ID");
        }
        else if (responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new DownloadDAOFailureException("其他问题");
        }
        Contract contract = responseEntity.getBody();

        return contract;
    }

    public Scheme getScheme(String schemeId, Long userId, Role userRole) {
        String params = "?userId=" + userId + "&userRole=" + userRole;
        String url = TEST_SERVICE + "/api/test/scheme/" + schemeId;
        ResponseEntity<Scheme> responseEntity = restTemplate.getForEntity(url + params, Scheme.class);
        // 检查测试方案 id 及权限有效性
        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN) {
            throw new DownloadPermissionDeniedException("无权下载该文件");
        } else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new DownloadNotFoundException("未找到该测试方案ID");
        } else if (responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new DownloadDAOFailureException("其他问题");
        }
        Scheme scheme = responseEntity.getBody();
        return scheme;
    }

    public Report getReport(String reportId, Long userId, Role userRole) {
        String params = "?userId=" + userId + "&userRole=" + userRole;
        String url = TEST_SERVICE + "/api/test/report/" + reportId;
        ResponseEntity<Report> responseEntity = restTemplate.getForEntity(url + params, Report.class);
        // 检查测试报告 id 及权限有效性
        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN) {
            throw new DownloadPermissionDeniedException("无权下载该文件");
        } else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new DownloadNotFoundException("未找到该测试报告ID");
        } else if (responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new DownloadDAOFailureException("其他问题");
        }
        Report report = responseEntity.getBody();
        return report;
    }

    /**
     * 通过 testcaseId 向test服务获取对象，以供后续生成文档并下载
     * @param testcaseId 待下载的测试用例表 id
     * @param userId 操作的用户 id
     * @param userRole 操作的用户角色
     * @return 若成功从test服务中获得对象，则返回；否则，返回异常信息
     * */
    public Testcase getTestcaseById(String testcaseId, Long userId, Role userRole) {
        // 调用test服务的getTestCase接口
        String params = "?userId=" + userId + "&userRole=" + userRole;
        String url = TEST_SERVICE + "/api/test/testcase/" + testcaseId;
        ResponseEntity<Testcase> responseEntity = restTemplate.getForEntity(url + params, Testcase.class);
        // 检查测试用例表 id 及权限有效性
        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN || responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new DownloadPermissionDeniedException("无权下载该文件");
        }
        else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new DownloadNotFoundException("未找到该测试用例表ID");
        }
        else if (responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new DownloadDAOFailureException("其他问题");
        }
        Testcase testcase = responseEntity.getBody();

        return testcase;
    }

    /**
     * 通过 testRecordId 向test服务获取对象，以供后续生成文档并下载
     * @param testRecordId 待下载的测试记录表 id
     * @param userId 操作的用户 id
     * @param userRole 操作的用户角色
     * @return 若成功从test服务中获得对象，则返回；否则，返回异常信息
     * */
    public TestRecordList getTestRecordListById(String testRecordId, Long userId, Role userRole) {
        // 调用test服务的getTestRecord接口
        String params = "?userId=" + userId + "&userRole=" + userRole;
        String url = TEST_SERVICE + "/api/test/testRecord/" + testRecordId;
        ResponseEntity<TestRecordList> responseEntity = restTemplate.getForEntity(url + params, TestRecordList.class);
        // 检查测试记录表 id 及权限有效性
        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN || responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new DownloadPermissionDeniedException("无权下载该文件");
        }
        else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new DownloadNotFoundException("未找到该测试记录表ID");
        }
        else if (responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new DownloadDAOFailureException("其他问题");
        }
        TestRecordList testRecordList = responseEntity.getBody();

        return testRecordList;
    }

    /**
     * 通过 testRecordId 向test服务获取对象，以供后续生成文档并下载
     * @param testIssueId 待下载的测试问题表 id
     * @param userId 操作的用户 id
     * @param userRole 操作的用户角色
     * @return 若成功从test服务中获得对象，则返回；否则，返回异常信息
     * */
    public TestIssueList getTestIssueListById(String testIssueId, Long userId, Role userRole) {
        // 调用test服务的getTestIssue接口
        String params = "?userId=" + userId + "&userRole=" + userRole;
        String url = TEST_SERVICE + "/api/test/testIssue/" + testIssueId;
        ResponseEntity<TestIssueList> responseEntity = restTemplate.getForEntity(url + params, TestIssueList.class);
        // 检查测试问题表 id 及权限有效性
        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN || responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new DownloadPermissionDeniedException("无权下载该文件");
        }
        else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new DownloadNotFoundException("未找到该测试问题表ID");
        }
        else if (responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new DownloadDAOFailureException("其他问题");
        }
        TestIssueList testIssueList = responseEntity.getBody();

        return testIssueList;
    }

    /**
     * 通过 entrustTestReviewId 向test服务获取对象，以供后续生成文档并下载
     * @param entrustTestReviewId 待下载的工作检查表 id
     * @param userId 操作的用户 id
     * @param userRole 操作的用户角色
     * @return 若成功从test服务中获得对象，则返回；否则，返回异常信息
     * */
    public EntrustTestReview getEntrustTestReviewById(String entrustTestReviewId, Long userId, Role userRole) {
        // 调用test服务的getEntrustReview接口
        String params = "?userId=" + userId + "&userRole=" + userRole;
        String url = TEST_SERVICE + "/api/review/entrustTest/" + entrustTestReviewId;
        ResponseEntity<EntrustTestReview> responseEntity = restTemplate.getForEntity(url + params, EntrustTestReview.class);
        // 检查测试工作检查表 id 及权限有效性
        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN || responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new DownloadPermissionDeniedException("无权下载该文件");
        }
        else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new DownloadNotFoundException("未找到该工作检查表ID");
        }
        else if (responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new DownloadDAOFailureException("其他问题");
        }
        EntrustTestReview entrustTestReview = responseEntity.getBody();

        return entrustTestReview;
    }

    /**
     * 通过 schemeReviewId 向test服务获取对象，以供后续生成文档并下载
     * @param schemeReviewId 待下载的方案评审表 id
     * @param userId 操作的用户 id
     * @param userRole 操作的用户角色
     * @return 若成功从test服务中获得对象，则返回；否则，返回异常信息
     * */
    public SchemeReview getSchemeReviewById(String schemeReviewId, Long userId, Role userRole) {
        // 调用test服务的getSchemeReview接口
        String params = "?userId=" + userId + "&userRole=" + userRole;
        String url = TEST_SERVICE + "/api/review/scheme/" + schemeReviewId;
        ResponseEntity<SchemeReview> responseEntity = restTemplate.getForEntity(url + params, SchemeReview.class);
        // 检查方案评审表 id 及权限有效性
        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN || responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new DownloadPermissionDeniedException("无权下载该文件");
        }
        else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new DownloadNotFoundException("未找到该测试方案评审表ID");
        }
        else if (responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new DownloadDAOFailureException("其他问题");
        }
        SchemeReview schemeReview = responseEntity.getBody();

        return schemeReview;
    }

    /**
     * 通过 schemeReviewId 向test服务获取对象，以供后续生成文档并下载
     * @param reportReviewId 测试报告表 id
     * @param userId 操作的用户 id
     * @param userRole 操作的用户角色
     * @return 若成功从test服务中获得对象，则返回；否则，返回异常信息
     * */
    public ReportReview getReportReviewById(String reportReviewId, Long userId, Role userRole) {
        // 调用test服务的getSchemeReview接口
        String params = "?userId=" + userId + "&userRole=" + userRole;
        String url = TEST_SERVICE + "/api/review/report/" + reportReviewId;
        ResponseEntity<ReportReview> responseEntity = restTemplate.getForEntity(url + params, ReportReview.class);
        // 检查测试报告检查表 id 及权限有效性
        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN || responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new DownloadPermissionDeniedException("无权下载该文件");
        }
        else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new DownloadNotFoundException("未找到该测试方案评审表ID");
        }
        else if (responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new DownloadDAOFailureException("其他问题");
        }
        ReportReview reportReview = responseEntity.getBody();

        return reportReview;
    }

    public EntrustQuote getEntrustQuoteById(String entrustId, Long userId, Role userRole) {
        // 调用entrust服务的getEntrust的接口
        String params = "?userId=" + userId + "&userRole=" + userRole;
        String url = ENTRUST_SERVICE + "/api/entrust/" + entrustId;
        ResponseEntity<Entrust> responseEntity = restTemplate.getForEntity(url + params, Entrust.class);
        if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN || responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            throw new DownloadPermissionDeniedException("无权下载该文件");
        }
        else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new DownloadNotFoundException("未找到该委托报价单ID");
        }
        else if (responseEntity.getStatusCode() != HttpStatus.OK && responseEntity.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new DownloadDAOFailureException("其他问题");
        }
        Entrust entrust = responseEntity.getBody();
        if (entrust == null) {
            throw new DownloadNotFoundException("未找到报价单对应的委托");
        }
        EntrustQuote entrustQuote = entrust.getQuote();

        return entrustQuote;
    }
}

package com.njustc.onlinebiz.entrust;

import com.njustc.onlinebiz.common.model.PageResult;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.Software;
import com.njustc.onlinebiz.common.model.entrust.*;
import com.njustc.onlinebiz.entrust.dao.EntrustDAO;
import com.njustc.onlinebiz.entrust.exception.EntrustInvalidArgumentException;
import com.njustc.onlinebiz.entrust.exception.EntrustInvalidStageException;
import com.njustc.onlinebiz.entrust.exception.EntrustNotFoundException;
import com.njustc.onlinebiz.entrust.exception.EntrustPermissionDeniedException;
import com.njustc.onlinebiz.entrust.service.DefaultEntrustService;
import com.njustc.onlinebiz.entrust.service.EntrustService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

public class DefaultEntrustServiceTest {

    private final EntrustDAO entrustDAO = mock(EntrustDAO.class);

    private final EntrustService entrustService = new DefaultEntrustService(entrustDAO);

    private String getNonExistId() {
        return new ObjectId(new Date(0)).toString();
    }

    @Test
    public void testCreateEntrustNoAuthority() {
        EntrustContent content = new EntrustContent();
        Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                entrustService.createEntrust(content, 1L, Role.TESTER));
    }

    @Test
    public void testCreateEntrustSuccess() {
        EntrustContent content = new EntrustContent();
        when(entrustDAO.insertEntrust(any(Entrust.class))).thenReturn(new Entrust());
        when(entrustDAO.updateStatus(any(String.class), any(EntrustStatus.class))).thenReturn(true);
        Assertions.assertDoesNotThrow(() ->
                entrustService.createEntrust(content, 1L, Role.CUSTOMER));
    }

    @Nested
    public class FindEntrustTest {

        private Entrust entrust;

        @BeforeEach
        public void prepareData() {
            // 准备一条数据
            entrust = new Entrust();
            entrust.setId(new ObjectId().toString());
            entrust.setCustomerId(1L);
            entrust.setMarketerId(2L);
            entrust.setTesterId(3L);
            when(entrustDAO.findEntrustById(entrust.getId())).thenReturn(entrust);
        }

        @Test
        public void testFindEntrustNotExist() {
            Assertions.assertThrows(EntrustNotFoundException.class, () ->
                    entrustService.findEntrust(getNonExistId(), 1L, Role.ADMIN));
        }

        @Test
        public void testFindEntrustNoAuthority() {
            // 人员ID不匹配不能看委托
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.findEntrust(entrust.getId(), 4L, Role.CUSTOMER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.findEntrust(entrust.getId(), 5L, Role.MARKETER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.findEntrust(entrust.getId(), 6L, Role.TESTER));
        }

        @Test
        public void testFindEntrustSuccess() {
            Assertions.assertEquals(entrust, entrustService.findEntrust(entrust.getId(), 1L, Role.CUSTOMER));
            Assertions.assertEquals(entrust, entrustService.findEntrust(entrust.getId(), 2L, Role.MARKETER));
            Assertions.assertEquals(entrust, entrustService.findEntrust(entrust.getId(), 3L, Role.TESTER));
            Assertions.assertEquals(entrust, entrustService.findEntrust(entrust.getId(), 4L, Role.ADMIN));
            Assertions.assertEquals(entrust, entrustService.findEntrust(entrust.getId(), 5L, Role.MARKETING_SUPERVISOR));
            Assertions.assertEquals(entrust, entrustService.findEntrust(entrust.getId(), 6L, Role.TESTING_SUPERVISOR));
        }

    }

    @Nested
    public class FindEntrustOutlinesTest {

        private final List<EntrustOutline> entrustOutlines = new ArrayList<>();

        @BeforeEach
        public void prepare() {
            // 准备一组数据
            for (int i = 0; i < 100; ++i) {
                Software software = new Software();
                software.setName("受测软件");
                software.setVersion("0.0.1");
                EntrustContent content = new EntrustContent();
                content.setSoftware(software);
                Entrust entrust = new Entrust();
                entrust.setId(new ObjectId().toString());
                entrust.setContent(content);
                entrust.setCustomerId(i / 10L);
                entrust.setMarketerId(i / 20L);
                entrust.setTesterId(i / 2L);
                entrustOutlines.add(new EntrustOutline(entrust));
            }
            // 准备mock返回的结果
            for (long i = 0; i < 10; ++i) {
                long j = i;
                List<EntrustOutline> result = entrustOutlines.stream().filter(outline -> outline.getCustomerId().equals(j)).collect(Collectors.toList());
                when(entrustDAO.findEntrustsByCustomerId(i, 1, 10)).thenReturn(result);
                when(entrustDAO.countByCustomerId(i)).thenReturn(10L);
            }
            for (long i = 0; i < 5; ++i) {
                long j = i;
                List<EntrustOutline> result = entrustOutlines.stream().filter(outline -> outline.getMarketerId().equals(j)).collect(Collectors.toList());
                when(entrustDAO.findEntrustsByMarketerId(i, 1, 20)).thenReturn(result);
                when(entrustDAO.countByMarketerId(i)).thenReturn(20L);
            }
            for (long i = 0; i < 50; ++i) {
                long j = i;
                List<EntrustOutline> result = entrustOutlines.stream().filter(outline -> outline.getTesterId().equals(j)).collect(Collectors.toList());
                when(entrustDAO.findEntrustsByTesterId(i, 1, 2)).thenReturn(result);
                when(entrustDAO.countByTesterId(i)).thenReturn(2L);
            }
            when(entrustDAO.findAllEntrusts(1, 100)).thenReturn(entrustOutlines);
            when(entrustDAO.countAll()).thenReturn(100L);
        }

        @Test
        public void testFindEntrustOutlinesNoAuthority() {
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () -> entrustService.findEntrustOutlines(1, 20, 1L, Role.QA));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () -> entrustService.findEntrustOutlines(1, 20, 1L, Role.QA_SUPERVISOR));
        }

        @Test
        public void testFindEntrustOutlinesFromAdminOrSupervisor() {
            Role [] roles = new Role[]{Role.ADMIN, Role.MARKETING_SUPERVISOR, Role.TESTING_SUPERVISOR};
            for (Role userRole : roles) {
                PageResult<EntrustOutline> result = entrustService.findEntrustOutlines(1, 100, 1L, userRole);
                Assertions.assertEquals(100, result.getTotal());
                Assertions.assertSame(entrustOutlines, result.getList());
            }
        }

        @Test
        public void testFindEntrustOutlinesFromCustomer() {
            for (long i = 0; i < 10; ++i) {
                PageResult<EntrustOutline> result = entrustService.findEntrustOutlines(1, 10, i, Role.CUSTOMER);
                Assertions.assertEquals(10L, result.getTotal());
                long j = i;
                result.getList().forEach(outline -> Assertions.assertEquals(j, outline.getCustomerId()));
            }
        }

        @Test
        public void testFindEntrustOutlinesFromMarketer() {
            for (long i = 0; i < 5; ++i) {
                PageResult<EntrustOutline> result = entrustService.findEntrustOutlines(1, 20, i, Role.MARKETER);
                Assertions.assertEquals(20L, result.getTotal());
                long j = i;
                result.getList().forEach(outline -> Assertions.assertEquals(j, outline.getMarketerId()));
            }
        }

        @Test
        public void testFindEntrustOutlinesFromTester() {
            for (long i = 0; i < 50; ++i) {
                PageResult<EntrustOutline> result = entrustService.findEntrustOutlines(1, 2, i, Role.TESTER);
                Assertions.assertEquals(2L, result.getTotal());
                long j = i;
                result.getList().forEach(outline -> Assertions.assertEquals(j, outline.getTesterId()));
            }
        }

        @Test
        public void testFindEntrustOutlinesWithInvalidPage() {
            Assertions.assertThrows(EntrustInvalidArgumentException.class, () ->
                    entrustService.findEntrustOutlines(-1, 10, 1L, Role.CUSTOMER));
            Assertions.assertThrows(EntrustInvalidArgumentException.class, () ->
                    entrustService.findEntrustOutlines(1, -10, 1L, Role.ADMIN));
        }

    }

    @Nested
    public class ModificationTest {

        private final Entrust entrust = new Entrust();

        private final EntrustContent content = new EntrustContent();

        private final EntrustStatus status = new EntrustStatus(EntrustStage.WAIT_FOR_MARKETER, "");

        private final SoftwareDocReview softwareDocReview = new SoftwareDocReview();

        private final EntrustReview review = new EntrustReview();

        private final EntrustQuote quote = new EntrustQuote();

        private final String contractId = new ObjectId().toString();

        @BeforeEach
        public void prepareData() {
            // 准备一条数据
            entrust.setId(new ObjectId().toString());
            entrust.setCustomerId(1L);
            entrust.setMarketerId(2L);
            entrust.setTesterId(3L);
            entrust.setStatus(status);
            when(entrustDAO.findEntrustById(entrust.getId())).thenReturn(entrust);
            when(entrustDAO.updateContent(entrust.getId(), content)).thenReturn(true);
            when(entrustDAO.updateStatus(anyString(), any(EntrustStatus.class))).thenReturn(true);
            when(entrustDAO.updateSoftwareDocReview(entrust.getId(), softwareDocReview)).thenReturn(true);
            when(entrustDAO.updateReview(entrust.getId(), review)).thenReturn(true);
            when(entrustDAO.updateQuote(entrust.getId(), quote)).thenReturn(true);
            when(entrustDAO.updateMarketerId(entrust.getId(), 4L)).thenReturn(true);
            when(entrustDAO.updateTesterId(entrust.getId(), 4L)).thenReturn(true);
            when(entrustDAO.updateCustomerId(entrust.getId(), 4L)).thenReturn(true);
            when(entrustDAO.updateContractId(entrust.getId(), contractId)).thenReturn(true);
            when(entrustDAO.deleteEntrust(entrust.getId())).thenReturn(true);
        }

        @Test
        public void testUpdateContentInvalidStage() {
            status.setStage(EntrustStage.MARKETER_AUDITING);
            Assertions.assertThrows(EntrustInvalidStageException.class, () ->
                    entrustService.updateContent(entrust.getId(), content, 1L, Role.CUSTOMER));
        }

        @Test
        public void testUpdateContentNoAuthority() {
           status.setStage(EntrustStage.TESTER_DENIED);
           // 用户角色不对
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateContent(entrust.getId(), content, 2L, Role.MARKETER));
            // 用户ID不匹配
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateContent(entrust.getId(), content, 4L, Role.CUSTOMER));
        }

        @Test
        public void testUpdateContentSuccess() {
            EntrustStage[] stages = new EntrustStage[]{EntrustStage.MARKETER_DENIED, EntrustStage.TESTER_DENIED};
            for (EntrustStage stage : stages) {
                status.setStage(stage);
                Assertions.assertDoesNotThrow(() -> entrustService.updateContent(entrust.getId(), content, 1L, Role.CUSTOMER));
                Assertions.assertDoesNotThrow(() -> entrustService.updateContent(entrust.getId(), content, 0L, Role.ADMIN));
            }
        }

        @Test
        public void testDenyContentInvalidStage() {
            status.setStage(EntrustStage.WAIT_FOR_TESTER);
            Assertions.assertThrows(EntrustInvalidStageException.class, () ->
                    entrustService.denyContent(entrust.getId(), "", 3L, Role.TESTER));
        }

        @Test
        public void testDenyContentNoAuthority() {
            // 阶段和角色不对应
            status.setStage(EntrustStage.MARKETER_AUDITING);
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.denyContent(entrust.getId(), "", 3L, Role.TESTER));
            status.setStage(EntrustStage.TESTER_AUDITING);
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.denyContent(entrust.getId(), "", 2L, Role.MARKETER));
            // 用户ID不匹配
            status.setStage(EntrustStage.MARKETER_AUDITING);
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.denyContent(entrust.getId(), "", 4L, Role.MARKETER));
            status.setStage(EntrustStage.TESTER_AUDITING);
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.denyContent(entrust.getId(), "", 5L, Role.TESTER));
        }

        @Test
        public void testDenyContentSuccess() {
            status.setStage(EntrustStage.MARKETER_AUDITING);
            Assertions.assertDoesNotThrow(() ->
                    entrustService.denyContent(entrust.getId(), "", 2L, Role.MARKETER));
            status.setStage(EntrustStage.TESTER_AUDITING);
            Assertions.assertDoesNotThrow(() ->
                    entrustService.denyContent(entrust.getId(), "", 3L, Role.TESTER));
        }

        @Test
        public void testApproveContentInvalidStage() {
            status.setStage(EntrustStage.CUSTOMER_CHECK_QUOTE);
            Assertions.assertThrows(EntrustInvalidStageException.class, () ->
                    entrustService.approveContent(entrust.getId(), 2L, Role.MARKETER));
        }

        @Test
        public void testApproveContentNoAuthority() {
            status.setStage(EntrustStage.MARKETER_AUDITING);
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.approveContent(entrust.getId(), 3L, Role.TESTER));
            status.setStage(EntrustStage.TESTER_AUDITING);
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.approveContent(entrust.getId(), 2L, Role.MARKETER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.approveContent(entrust.getId(), 1L, Role.CUSTOMER));
        }

        @Test
        public void testApproveContentSuccess() {
            status.setStage(EntrustStage.MARKETER_AUDITING);
            Assertions.assertDoesNotThrow(() ->
                    entrustService.approveContent(entrust.getId(), 2L, Role.MARKETER));
            status.setStage(EntrustStage.TESTER_AUDITING);
            Assertions.assertDoesNotThrow(() ->
                    entrustService.approveContent(entrust.getId(), 3L, Role.TESTER));
        }

        @Test
        public void testUpdateSoftwareDocReviewInvalidStage() {
            status.setStage(EntrustStage.CUSTOMER_CHECK_QUOTE);
            Assertions.assertThrows(EntrustInvalidStageException.class, () ->
                    entrustService.updateSoftwareDocReview(entrust.getId(), softwareDocReview, 3L, Role.TESTER));
        }

        @Test
        public void testUpdateSoftwareDocReviewNoAuthority() {
            status.setStage(EntrustStage.TESTER_AUDITING);
            // 角色不对
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateSoftwareDocReview(entrust.getId(), softwareDocReview, 1L, Role.CUSTOMER));
            // 用户ID不匹配
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateSoftwareDocReview(entrust.getId(), softwareDocReview, 4L, Role.TESTER));
        }

        @Test
        public void testUpdateSoftwareDocReviewSuccess() {
            status.setStage(EntrustStage.TESTER_AUDITING);
            Assertions.assertDoesNotThrow(() ->
                    entrustService.updateSoftwareDocReview(entrust.getId(), softwareDocReview, 3L, Role.TESTER));
        }

        @Test
        public void testUpdateReviewNoAuthority() {
            // 角色不对
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateReview(entrust.getId(), review, 1L, Role.CUSTOMER));
            // 用户ID不匹配
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateReview(entrust.getId(), review, 4L, Role.MARKETER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateReview(entrust.getId(), review, 5L, Role.TESTER));
        }

        @Test
        public void testUpdateReviewSuccess() {
            Assertions.assertDoesNotThrow(() ->
                    entrustService.updateReview(entrust.getId(), review, 0L, Role.ADMIN));
            Assertions.assertDoesNotThrow(() ->
                    entrustService.updateReview(entrust.getId(), review, 2L, Role.MARKETER));
            Assertions.assertDoesNotThrow(() ->
                    entrustService.updateReview(entrust.getId(), review, 3L, Role.TESTER));
        }

        @Test
        public void testUpdateQuoteInvalidStage() {
            status.setStage(EntrustStage.TESTER_DENIED);
            Assertions.assertThrows(EntrustInvalidStageException.class, () ->
                    entrustService.updateQuote(entrust.getId(), quote, 2L, Role.MARKETER));
        }

        @Test
        public void testUpdateQuoteNoAuthority() {
            status.setStage(EntrustStage.AUDITING_PASSED);
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateQuote(entrust.getId(), quote, 1L, Role.CUSTOMER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateQuote(entrust.getId(), quote, 3L, Role.TESTER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateQuote(entrust.getId(), quote, 4L, Role.MARKETER));
        }

        @Test
        public void testUpdateQuoteSuccess() {
            status.setStage(EntrustStage.AUDITING_PASSED);
            Assertions.assertDoesNotThrow(() ->
                    entrustService.updateQuote(entrust.getId(), quote, 2L, Role.MARKETER));
            status.setStage(EntrustStage.CUSTOMER_DENY_QUOTE);
            Assertions.assertDoesNotThrow(() ->
                    entrustService.updateQuote(entrust.getId(), quote, 2L, Role.MARKETER));
        }

        @Test
        public void testDenyQuoteInvalidStage() {
            status.setStage(EntrustStage.CUSTOMER_ACCEPT_QUOTE);
            Assertions.assertThrows(EntrustInvalidStageException.class, () ->
                    entrustService.denyQuote(entrust.getId(), "", 1L, Role.CUSTOMER));
        }

        @Test
        public void testDenyQuoteNoAuthority() {
            status.setStage(EntrustStage.CUSTOMER_CHECK_QUOTE);
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.denyQuote(entrust.getId(), "", 2L, Role.MARKETER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.denyQuote(entrust.getId(), "", 3L, Role.TESTER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.denyQuote(entrust.getId(), "", 4L, Role.CUSTOMER));
        }

        @Test
        public void testDenyQuoteSuccess() {
            status.setStage(EntrustStage.CUSTOMER_CHECK_QUOTE);
            Assertions.assertDoesNotThrow(() ->
                    entrustService.denyQuote(entrust.getId(), "", 1L, Role.CUSTOMER));
        }

        @Test
        public void testApproveQuoteInvalidStage() {
            status.setStage(EntrustStage.AUDITING_PASSED);
            Assertions.assertThrows(EntrustInvalidStageException.class, () ->
                    entrustService.approveQuote(entrust.getId(), 1L, Role.CUSTOMER));
        }

        @Test
        public void testApproveQuoteNoAuthority() {
            status.setStage(EntrustStage.CUSTOMER_CHECK_QUOTE);
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.approveQuote(entrust.getId(), 2L, Role.MARKETER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.approveQuote(entrust.getId(), 3L, Role.TESTER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.approveQuote(entrust.getId(), 4L, Role.CUSTOMER));
        }

        @Test
        public void testApproveQuoteSuccess() {
            status.setStage(EntrustStage.CUSTOMER_CHECK_QUOTE);
            Assertions.assertDoesNotThrow(() ->
                    entrustService.approveQuote(entrust.getId(), 1L, Role.CUSTOMER));
        }

        @Test
        public void testTerminateEntrustInvalidStage() {
            status.setStage(EntrustStage.CUSTOMER_ACCEPT_QUOTE);
            Assertions.assertThrows(EntrustInvalidStageException.class, () ->
                    entrustService.terminateEntrust(entrust.getId(), 1L, Role.CUSTOMER));
        }

        @Test
        public void testTerminateEntrustNoAuthority() {
            status.setStage(EntrustStage.CUSTOMER_CHECK_QUOTE);
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.terminateEntrust(entrust.getId(), 2L, Role.MARKETER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.terminateEntrust(entrust.getId(), 3L, Role.TESTER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.terminateEntrust(entrust.getId(), 4L, Role.CUSTOMER));
        }

        @Test
        public void testTerminateEntrustSuccess() {
            status.setStage(EntrustStage.CUSTOMER_CHECK_QUOTE);
            Assertions.assertDoesNotThrow(() ->
                    entrustService.terminateEntrust(entrust.getId(), 1L, Role.CUSTOMER));
        }

        @Test
        public void testUpdateMarketerInvalidStage() {
            status.setStage(EntrustStage.WAIT_FOR_TESTER);
            Assertions.assertThrows(EntrustInvalidStageException.class, () ->
                    entrustService.updateMarketer(entrust.getId(), 4L, 5L, Role.MARKETING_SUPERVISOR));
        }

        @Test
        public void testUpdateMarketerNoAuthority() {
            status.setStage(EntrustStage.WAIT_FOR_MARKETER);
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateMarketer(entrust.getId(), 4L, 2L, Role.MARKETER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateMarketer(entrust.getId(), 4L, 1L, Role.CUSTOMER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateMarketer(entrust.getId(), 4L, 3L, Role.TESTER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateMarketer(entrust.getId(), 4L, 6L, Role.TESTING_SUPERVISOR));
        }

        @Test
        public void testUpdateMarketerSuccess() {
            status.setStage(EntrustStage.WAIT_FOR_MARKETER);
            Assertions.assertDoesNotThrow(() ->
                    entrustService.updateMarketer(entrust.getId(), 4L, 5L, Role.MARKETING_SUPERVISOR));
        }

        @Test
        public void testUpdateTesterInvalidStage() {
            status.setStage(EntrustStage.WAIT_FOR_MARKETER);
            Assertions.assertThrows(EntrustInvalidStageException.class, () ->
                    entrustService.updateTester(entrust.getId(), 4L, 5L, Role.TESTING_SUPERVISOR));
        }

        @Test
        public void testUpdateTesterNoAuthority() {
            status.setStage(EntrustStage.WAIT_FOR_TESTER);
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateTester(entrust.getId(), 4L, 2L, Role.MARKETER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateTester(entrust.getId(), 4L, 1L, Role.CUSTOMER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateTester(entrust.getId(), 4L, 3L, Role.TESTER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateTester(entrust.getId(), 4L, 6L, Role.MARKETING_SUPERVISOR));
        }

        @Test
        public void testUpdateTesterSuccess() {
            status.setStage(EntrustStage.WAIT_FOR_TESTER);
            Assertions.assertDoesNotThrow(() ->
                    entrustService.updateTester(entrust.getId(), 4L, 5L, Role.TESTING_SUPERVISOR));
        }

        @Test
        public void testUpdateCustomerNoAuthority() {
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateCustomer(entrust.getId(), 4L, 1L, Role.CUSTOMER));
        }

        @Test
        public void testUpdateCustomerSuccess() {
            Assertions.assertDoesNotThrow(() ->
                    entrustService.updateCustomer(entrust.getId(), 4L, 0L, Role.ADMIN));
        }

        @Test
        public void testUpdateStatusNoAuthority() {
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.updateStatus(entrust.getId(), status, 1L, Role.CUSTOMER));
        }

        @Test
        public void testUpdateStatusSuccess() {
            Assertions.assertDoesNotThrow(() ->
                    entrustService.updateStatus(entrust.getId(), status, 0L, Role.ADMIN));
        }

        @Test
        public void testRemoveEntrustNotExist() {
            Assertions.assertThrows(EntrustNotFoundException.class, () ->
                    entrustService.removeEntrust(getNonExistId(), 0L, Role.ADMIN));
        }

        @Test
        public void testRemoveEntrustNoAuthority() {
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.removeEntrust(entrust.getId(), 1L, Role.CUSTOMER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.removeEntrust(entrust.getId(), 2L, Role.MARKETER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.removeEntrust(entrust.getId(), 3L, Role.TESTER));
        }

        @Test
        public void testRemoveEntrustSuccess() {
            Assertions.assertDoesNotThrow(() ->
                    entrustService.removeEntrust(entrust.getId(), 0L, Role.ADMIN));
        }

        @Test
        public void testCheckConsistencyInvalidStage() {
            status.setStage(EntrustStage.CUSTOMER_CHECK_QUOTE);
            Assertions.assertThrows(EntrustInvalidStageException.class, () ->
                    entrustService.checkConsistencyWithContract(entrust.getId(), 2L, Role.MARKETER));
        }

        @Test
        public void testCheckConsistencyNoAuthority() {
            status.setStage(EntrustStage.CUSTOMER_ACCEPT_QUOTE);
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.checkConsistencyWithContract(entrust.getId(), 3L, Role.TESTER));
            Assertions.assertThrows(EntrustPermissionDeniedException.class, () ->
                    entrustService.checkConsistencyWithContract(entrust.getId(), 4L, Role.MARKETER));
        }

        @Test
        public void testCheckConsistencySuccess() {
            status.setStage(EntrustStage.CUSTOMER_ACCEPT_QUOTE);
            Assertions.assertDoesNotThrow(() ->
                    entrustService.checkConsistencyWithContract(entrust.getId(), 2L, Role.MARKETER));
        }

        @Test
        public void testRegisterContractSuccess() {
            Assertions.assertDoesNotThrow(() ->
                    entrustService.registerContract(entrust.getId(), contractId));
        }

        @Test
        public void testGetTesterIdNotExist() {
            Assertions.assertThrows(EntrustNotFoundException.class, () ->
                    entrustService.getTesterId(getNonExistId()));
        }

        @Test
        public void testGetTesterIdSuccess() {
            Assertions.assertEquals(3L, entrustService.getTesterId(entrust.getId()));
        }

    }

}

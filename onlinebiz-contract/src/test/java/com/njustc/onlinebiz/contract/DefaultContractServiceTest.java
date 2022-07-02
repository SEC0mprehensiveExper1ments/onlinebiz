package com.njustc.onlinebiz.contract;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.contract.dao.ContractDAO;
import com.njustc.onlinebiz.contract.exception.ContractCreateFailureException;
import com.njustc.onlinebiz.contract.exception.ContractInvalidStageException;
import com.njustc.onlinebiz.contract.exception.ContractNotFoundException;
import com.njustc.onlinebiz.contract.exception.ContractPermissionDeniedException;
import com.njustc.onlinebiz.common.model.contract.Contract;
import com.njustc.onlinebiz.common.model.contract.ContractStage;
import com.njustc.onlinebiz.common.model.contract.ContractStatus;
import com.njustc.onlinebiz.common.model.contract.NonDisclosureAgreement;
import com.njustc.onlinebiz.contract.service.ContractService;
import com.njustc.onlinebiz.contract.service.DefaultContractService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

import static org.mockito.Mockito.*;

public class DefaultContractServiceTest {

    private final ContractDAO contractDAO = mock(ContractDAO.class);

    private final RestTemplate restTemplate = mock(RestTemplate.class);

    private final ContractService contractService = new DefaultContractService(contractDAO, restTemplate);

    @Test
    public void testCreateContractNoAuthority() {
        String entrustId = new ObjectId().toString();
        Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                contractService.createContract(entrustId, 1L, Role.CUSTOMER));
        Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                contractService.createContract(entrustId, 3L, Role.TESTER));
    }

    @Test
    public void testCreateContractInconsistent() {
        when(restTemplate.getForObject(anyString(), any())).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        Assertions.assertThrows(ContractCreateFailureException.class, () ->
                contractService.createContract(new ObjectId().toString(), 2L, Role.MARKETER));

    }

    @Test
    public void testCreateContractSuccess() {
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.ok(1L));
        when(restTemplate.postForEntity(anyString(), any(), any())).thenReturn(ResponseEntity.ok().build());
        when(contractDAO.insertContract(any(Contract.class))).thenReturn(new Contract());
        Assertions.assertDoesNotThrow(() ->
                contractService.createContract(new ObjectId().toString(), 2L, Role.MARKETER));
    }

    @Nested
    public class ModifyContractTest {

        private final Contract contract = new Contract();

        private final ContractStatus status = new ContractStatus(ContractStage.FILL_CONTRACT, "");

        private final NonDisclosureAgreement nonDisclosureAgreement = new NonDisclosureAgreement();

        private String getNonExistId() {
            return new ObjectId(new Date(0)).toString();
        }

        @BeforeEach
        public void prepareData() {
            String contractId = new ObjectId().toString();
            contract.setId(contractId);
            contract.setMarketerId(2L);
            contract.setCustomerId(1L);
            contract.setEntrustId(new ObjectId().toString());
            contract.setStatus(status);
            contract.setNonDisclosureAgreement(nonDisclosureAgreement);
            when(contractDAO.findContractById(contractId)).thenReturn(contract);
            when(contractDAO.updateContract(contractId, contract)).thenReturn(true);
            when(contractDAO.updateStatus(anyString(), any(ContractStatus.class))).thenReturn(true);
            when(contractDAO.updateNonDisclosure(contractId, nonDisclosureAgreement)).thenReturn(true);
            String scannedCopyPath = "";
            when(contractDAO.updateScannedCopyPath(contractId, scannedCopyPath)).thenReturn(true);
            when(contractDAO.updateCustomerId(contractId, 4L)).thenReturn(true);
            when(contractDAO.updateMarketerId(contractId, 4L)).thenReturn(true);
            when(contractDAO.deleteContractById(contractId)).thenReturn(true);
        }

        @Test
        public void testFindContractNotExist() {
            Assertions.assertThrows(ContractNotFoundException.class, () ->
                    contractService.findContract(getNonExistId(), 0L, Role.ADMIN));
        }

        @Test
        public void testFindContractNoAuthority() {
            Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                    contractService.findContract(contract.getId(), 4L, Role.MARKETER));
            Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                    contractService.findContract(contract.getId(), 4L, Role.CUSTOMER));
        }

        @Test
        public void testFindContractSuccess() {
            Assertions.assertDoesNotThrow(() ->
                    contractService.findContract(contract.getId(), 2L, Role.MARKETER));
            Assertions.assertDoesNotThrow(() ->
                    contractService.findContract(contract.getId(), 1L, Role.CUSTOMER));
            Assertions.assertDoesNotThrow(() ->
                    contractService.findContract(contract.getId(), 0L, Role.ADMIN));
        }

        @Test
        public void testUpdateContractInvalidStage() {
            status.setStage(ContractStage.CUSTOMER_CHECKING);
            Assertions.assertThrows(ContractInvalidStageException.class, () ->
                    contractService.updateContract(contract.getId(), contract, 2L, Role.MARKETER));
            status.setStage(ContractStage.MARKETER_CHECKING);
            Assertions.assertThrows(ContractInvalidStageException.class, () ->
                    contractService.updateContract(contract.getId(), contract, 1L, Role.CUSTOMER));
        }

        @Test
        public void testUpdateContractNoAuthority() {
            status.setStage(ContractStage.FILL_CONTRACT);
            Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                    contractService.updateContract(contract.getId(), contract, 4L, Role.MARKETER));
            status.setStage(ContractStage.MARKETER_DENY);
            Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                    contractService.updateContract(contract.getId(), contract, 4L, Role.CUSTOMER));
        }

        @Test
        public void testUpdateContractInconsistent() {
            status.setStage(ContractStage.FILL_CONTRACT);
            Contract another = new Contract();
            another.setId(getNonExistId());
            Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                    contractService.updateContract(contract.getId(), another, 2L, Role.MARKETER));
        }

        @Test
        public void testUpdateContractSuccess() {
            status.setStage(ContractStage.CUSTOMER_DENY);
            Assertions.assertDoesNotThrow(() ->
                    contractService.updateContract(contract.getId(), contract, 2L, Role.MARKETER));
            status.setStage(ContractStage.MARKETER_DENY);
            Assertions.assertDoesNotThrow(() ->
                    contractService.updateContract(contract.getId(), contract, 1L, Role.CUSTOMER));
        }

        @Test
        public void testApproveContractInvalidStage() {
            status.setStage(ContractStage.FILL_CONTRACT);
            Assertions.assertThrows(ContractInvalidStageException.class, () ->
                    contractService.approveContract(contract.getId(), 1L, Role.CUSTOMER));
        }

        @Test
        public void testApproveContractNoAuthority() {
            status.setStage(ContractStage.MARKETER_CHECKING);
            Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                    contractService.approveContract(contract.getId(), 4L, Role.MARKETER));
            status.setStage(ContractStage.CUSTOMER_CHECKING);
            Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                    contractService.approveContract(contract.getId(), 4L, Role.CUSTOMER));
        }

        @Test
        public void testApproveContractSuccess() {
            status.setStage(ContractStage.MARKETER_CHECKING);
            Assertions.assertDoesNotThrow(() ->
                    contractService.approveContract(contract.getId(), 2L, Role.MARKETER));
            status.setStage(ContractStage.CUSTOMER_CHECKING);
            Assertions.assertDoesNotThrow(() ->
                    contractService.approveContract(contract.getId(), 1L, Role.CUSTOMER));
        }

        @Test
        public void testDenyContractInvalidStage() {
            status.setStage(ContractStage.COPY_SAVED);
            Assertions.assertThrows(ContractInvalidStageException.class, () ->
                    contractService.denyContract(contract.getId(), "", 2L, Role.MARKETER));
        }

        @Test
        public void testDenyContractNoAuthority() {
            status.setStage(ContractStage.MARKETER_CHECKING);
            Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                    contractService.denyContract(contract.getId(), "", 4L, Role.MARKETER));
            status.setStage(ContractStage.CUSTOMER_CHECKING);
            Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                    contractService.denyContract(contract.getId(), "", 4L, Role.CUSTOMER));
        }

        @Test
        public void testDenyContractSuccess() {
            status.setStage(ContractStage.MARKETER_CHECKING);
            Assertions.assertDoesNotThrow(() ->
                    contractService.denyContract(contract.getId(), "", 2L, Role.MARKETER));
            status.setStage(ContractStage.CUSTOMER_CHECKING);
            Assertions.assertDoesNotThrow(() ->
                    contractService.denyContract(contract.getId(), "", 1L, Role.CUSTOMER));
        }

        @Test
        public void testUpdateCustomerIdNoAuthority() {
            Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                    contractService.updateCustomerId(contract.getId(), 4L, 3L, Role.TESTER));
        }

        @Test
        public void testUpdateCustomerIdSuccess() {
            Assertions.assertDoesNotThrow(() ->
                    contractService.updateCustomerId(contract.getId(), 4L, 0L, Role.ADMIN));
        }

        @Test
        public void testUpdateMarketerIdNoAuthority() {
            Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                    contractService.updateMarketerId(contract.getId(), 4L, 5L, Role.QA_SUPERVISOR));
        }

        @Test
        public void testUpdateMarketerIdSuccess() {
            Assertions.assertDoesNotThrow(() ->
                    contractService.updateMarketerId(contract.getId(), 4L, 0L, Role.ADMIN));
        }

        @Test
        public void testUpdateStatusNoAuthority() {
            Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                    contractService.updateStatus(contract.getId(), status, 5L, Role.MARKETING_SUPERVISOR));
        }

        @Test
        public void testUpdateStatusSuccess() {
            Assertions.assertDoesNotThrow(() ->
                    contractService.updateStatus(contract.getId(), status, 0L, Role.ADMIN));
        }

        @Nested
        public class ScannedCopyTest {

            MultipartFile multipartFile = mock(MultipartFile.class);

            @BeforeEach
            public void prepareData() {
                when(multipartFile.isEmpty()).thenReturn(false);
                when(multipartFile.getOriginalFilename()).thenReturn("contract.pdf");
            }

            @Test
            public void testSaveScannedCopyEmpty() {
                status.setStage(ContractStage.MARKETER_ACCEPT);
                when(multipartFile.isEmpty()).thenReturn(true);
                Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                        contractService.saveScannedCopy(contract.getId(), multipartFile, 2L, Role.CUSTOMER));
            }

            @Test
            public void testSaveScannedCopyInvalidStage() {
                status.setStage(ContractStage.CUSTOMER_CHECKING);
                Assertions.assertThrows(ContractInvalidStageException.class, () ->
                        contractService.saveScannedCopy(contract.getId(), multipartFile, 2L, Role.MARKETER));
            }

            @Test
            public void testSaveScannedCopyNoAuthority() {
                status.setStage(ContractStage.MARKETER_ACCEPT);
                Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                        contractService.saveScannedCopy(contract.getId(), multipartFile, 1L, Role.CUSTOMER));
                Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                        contractService.saveScannedCopy(contract.getId(), multipartFile, 4L, Role.MARKETER));
            }

            @Test
            public void testGetScannedCopyInvalidStage() {
                status.setStage(ContractStage.FILL_CONTRACT);
                Assertions.assertThrows(ContractInvalidStageException.class, () ->
                        contractService.getScannedCopy(contract.getId(), 1L, Role.CUSTOMER));
            }

            @Test
            public void testGetScannedCopyNoAuthority() {
                status.setStage(ContractStage.COPY_SAVED);
                Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                        contractService.getScannedCopy(contract.getId(), 4L, Role.CUSTOMER));
                Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                        contractService.getScannedCopy(contract.getId(), 4L, Role.MARKETER));
            }

        }

        @Test
        public void testUpdateNonDisclosureInvalidStage() {
            status.setStage(ContractStage.CUSTOMER_CHECKING);
            Assertions.assertThrows(ContractInvalidStageException.class, () ->
                    contractService.updateNonDisclosure(contract.getId(), nonDisclosureAgreement, 2L, Role.MARKETER));
            Assertions.assertThrows(ContractInvalidStageException.class, () ->
                    contractService.updateNonDisclosure(contract.getId(), nonDisclosureAgreement, 1L, Role.CUSTOMER));
        }

        @Test
        public void testUpdateNonDisclosureNoAuthority() {
            status.setStage(ContractStage.MARKETER_DENY);
            Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                    contractService.updateNonDisclosure(contract.getId(), nonDisclosureAgreement, 4L, Role.CUSTOMER));
            status.setStage(ContractStage.CUSTOMER_DENY);
            Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                    contractService.updateNonDisclosure(contract.getId(), nonDisclosureAgreement, 4L, Role.MARKETER));
        }

        @Test
        public void testUpdateNonDisclosureSuccess() {
            status.setStage(ContractStage.FILL_CONTRACT);
            Assertions.assertDoesNotThrow(() ->
                    contractService.updateNonDisclosure(contract.getId(), nonDisclosureAgreement, 2L, Role.MARKETER));
            status.setStage(ContractStage.CUSTOMER_ACCEPT);
            Assertions.assertDoesNotThrow(() ->
                    contractService.updateNonDisclosure(contract.getId(), nonDisclosureAgreement, 1L, Role.CUSTOMER));
        }

        @Test
        public void testRemoveContractNoAuthority() {
            Assertions.assertThrows(ContractPermissionDeniedException.class, () ->
                    contractService.removeContract(contract.getId(), 1L, Role.CUSTOMER));
        }

        @Test
        public void testRemoveContractNotExist() {
            Assertions.assertThrows(ContractNotFoundException.class, () ->
                    contractService.removeContract(getNonExistId(), 0L, Role.ADMIN));
        }

        @Test
        public void testRemoveContractSuccess() {
            Assertions.assertDoesNotThrow(() ->
                    contractService.removeContract(contract.getId(), 0L, Role.ADMIN));
        }

    }

}

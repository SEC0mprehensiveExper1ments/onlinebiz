package com.njustc.onlinebiz.contract;

import com.njustc.onlinebiz.contract.dao.ContractDAO;
import com.njustc.onlinebiz.contract.service.ContractService;
import com.njustc.onlinebiz.contract.service.DefaultContractService;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;

public class DefaultContractServiceTest {

    private final ContractDAO contractDAO = mock(ContractDAO.class);

    private final RestTemplate restTemplate = mock(RestTemplate.class);

    private final ContractService contractService = new DefaultContractService(contractDAO, restTemplate);

    @Test
    public void testCreateContractNoAuthority() {

    }

    @Test
    public void testCreateContractInconsistent() {

    }

    @Test
    public void testCreateContractSuccess() {

    }

    @Test
    public void testFindContractNoAuthority() {

    }

    @Test
    public void testFindContractNotExist() {

    }

    @Test
    public void testFindContractSuccess() {

    }

    @Test
    public void testUpdateContractNoAuthority() {

    }

    @Test
    public void testMarketerUpdateContractInvalidStage() {

    }

    @Test
    public void testMarketerUpdateContractSuccess() {

    }

    @Test
    public void testCustomerUpdateContractInvalidStage() {

    }

    @Test
    public void testCustomerUpdateContractSuccess() {

    }

    @Test
    public void testApproveContractNoAuthority() {

    }

    @Test
    public void testMarketerApproveContractInvalidStage() {

    }

    @Test
    public void testMarketerApproveContractSuccess() {

    }

    @Test
    public void testCustomerApproveContractInvalidStage() {

    }

    @Test
    public void testCustomerApproveContractSuccess() {

    }

    @Test
    public void testDenyContractNoAuthority() {

    }

    @Test
    public void testMarketerDenyContractInvalidStage() {

    }

    @Test
    public void testMarketerDenyContractSuccess() {

    }

    @Test
    public void testCustomerDenyContractInvalidStage() {

    }

    @Test
    public void testCustomerDenyContractSuccess() {

    }

    @Test
    public void testUpdateCustomerIdNoAuthority() {

    }

    @Test
    public void testUpdateCustomerIdSuccess() {

    }

    @Test
    public void testUpdateMarketerIdNoAuthority() {

    }

    @Test
    public void testUpdateMarketerIdSuccess() {

    }

    @Test
    public void testUpdateStatusNoAuthority() {

    }

    @Test
    public void testUpdateStatusSuccess() {

    }

    @Test
    public void testSaveScannedCopyNoAuthority() {

    }

    @Test
    public void testSaveScannedCopyInvalidStage() {

    }

    @Test
    public void testSaveScannedCopySuccess() {

    }

    @Test
    public void testGetScannedCopyNoAuthority() {

    }

    @Test
    public void testGetScannedCopyInvalidStage() {

    }

    @Test
    public void testUpdateNonDisclosureNoAuthority() {

    }

    @Test
    public void testMarketerUpdateNonDisclosureInvalidStage() {

    }

    @Test
    public void testMarketerUpdateNonDisclosureSuccess() {

    }

    @Test
    public void testCustomerUpdateNonDisclosureInvalidStage() {

    }

    @Test
    public void testCustomerUpdateNonDisclosureSuccess() {

    }

    @Test
    public void testRemoveContractNoAuthority() {

    }

    @Test
    public void testRemoveContractNotExist() {

    }

    @Test
    public void testRemoveContractSuccess() {

    }

}

package com.njustc.onlinebiz.contract.service;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.njustc.onlinebiz.common.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class MongoContractService implements ContractService {

    private static final String APPLY_SERVICE_URL = "http://onlinebiz-apply";

    private final MongoTemplate mongoTemplate;

    @Autowired
    private RestTemplate restTemplate;

    public MongoContractService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // 创建一个空的合同
    @Override
    public Contract createContract(Long principalId, Long creatorId, String applyId) {
        Contract contract = new Contract();
        contract.setPrincipalId(principalId);
        contract.setCreatorId(creatorId);
        contract.setApplyId(applyId);
        // 从委托申请服务获取委托信息，并提取相关内容放入 Contract 对象
        if (!initializeFromApply(applyId, contract)) {
            log.warn("将创建一份空的合同");
        }
        try {
            return mongoTemplate.insert(contract, Contract.COLLECTION_NAME);
        } catch (Exception e) {
            log.warn("创建合同失败");
            return null;
        }
    }

    // 从委托申请初始化合同内容
    private boolean initializeFromApply(String applyId, Contract contract) {
        Apply apply;
        try {
            apply = restTemplate.getForObject(APPLY_SERVICE_URL + "/apply/" + applyId, Apply.class);
            if (apply == null) {
                log.warn("获取委托申请信息失败");
                return false;
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
        ContractParty principal = contract.getPrincipal();
        ApplyParty applyParty = apply.getPrincipal();
        principal.setCompany(applyParty.getCompanyCH());
        principal.setAuthorizedRepresentative(applyParty.getContact());
        principal.setContact(applyParty.getContact());
        principal.setAddress(applyParty.getAddress());
        principal.setFax(applyParty.getFax());
        principal.setPhoneNumber(applyParty.getPhoneNumber());
        principal.setZipCode(applyParty.getZipCode());
        return true;
    }

    // 返回 MongoDB 中所有 Contract 对象投影到 Outline 对象的结果
    @Override
    public List<ContractOutline> findAllContracts() {
        Query query = new BasicQuery("{}", ContractOutline.PROJECT_FROM_CONTRACT);
        return mongoTemplate.find(query, ContractOutline.class, Contract.COLLECTION_NAME);
    }

    // 根据合同 id 查询合同的完整信息
    @Override
    public Contract findContractById(String contractId) {
        return mongoTemplate.findById(contractId, Contract.class);
    }

    // 检查访问的用户是否是合同的委托方
    @Override
    public Contract findContractByIdAndPrincipal(String contractId, Long userId) {
        Contract contract = findContractById(contractId);
        if (contract != null && userId.equals(contract.getPrincipalId())) {
            // 的确是客户自己的合同
            return contract;
        }
        return null;
    }

    // 更新一个合同，重新设置所有可编辑的域，除了合同 id 和用户 id
    @Override
    public boolean updateContract(Contract contract) {
        // ID 不合法有可能抛出异常
        try {
            Query query = new BasicQuery("{_id:ObjectId('" + contract.getId() + "')}");
            UpdateDefinition updateDefinition = new Update()
                    .set("serialNumber", contract.getSerialNumber())
                    .set("projectName", contract.getProjectName())
                    .set("principal", contract.getPrincipal())
                    .set("trustee", contract.getTrustee())
                    .set("signedAt", contract.getSignedAt())
                    .set("signedDate", contract.getSignedDate())
                    .set("targetSoftware", contract.getTargetSoftware())
                    .set("price", contract.getPrice())
                    .set("totalWorkingDays", contract.getTotalWorkingDays())
                    .set("rectificationLimit", contract.getRectificationLimit())
                    .set("rectificationDaysEachTime", contract.getRectificationDaysEachTime());
            UpdateResult result = mongoTemplate.updateFirst(query, updateDefinition, Contract.COLLECTION_NAME);
            return result.wasAcknowledged() && result.getMatchedCount() == 1;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean removeContract(String contractId) {
        try {
            Query query = new BasicQuery("{_id:ObjectId('" + contractId + "')}");
            DeleteResult result = mongoTemplate.remove(query, Contract.COLLECTION_NAME);
            return result.wasAcknowledged() && result.getDeletedCount() == 1;
        } catch (Exception e) {
            // in case of invalid contractId
            return false;
        }
    }

    @Override
    public ConditionBuilder search() {
        return new MongoConditionBuilder(mongoTemplate);
    }
}

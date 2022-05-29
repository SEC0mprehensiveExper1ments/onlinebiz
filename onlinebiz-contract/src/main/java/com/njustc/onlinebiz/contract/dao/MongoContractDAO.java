package com.njustc.onlinebiz.contract.dao;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.njustc.onlinebiz.common.model.contract.Contract;
import com.njustc.onlinebiz.common.model.contract.ContractStatus;
import com.njustc.onlinebiz.common.model.contract.NonDisclosureAgreement;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * 使用 MongoDB 的数据访问层实现，这里假设服务层已经校验好参数
 */
@Component
public class MongoContractDAO implements ContractDAO {

    private static final String COLLECTION_NAME = "contract";

    private final MongoTemplate mongoTemplate;

    public MongoContractDAO(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Contract insertContract(Contract contract) {
        return mongoTemplate.insert(contract, COLLECTION_NAME);
    }

    @Override
    public Contract findContractById(String contractId) {
        return mongoTemplate.findById(contractId, Contract.class, COLLECTION_NAME);
    }

    @Override
    public Boolean updateContract(String contractId, Contract contract) {
        Update update = new Update()
                .set("serialNumber", contract.getSerialNumber())
                .set("projectName", contract.getProjectName())
                .set("partyA", contract.getPartyA())
                .set("partyB", contract.getPartyB())
                .set("signedAt", contract.getSignedAt())
                .set("signedDate", contract.getSignedDate())
                .set("targetSoftware", contract.getTargetSoftware())
                .set("price", contract.getPrice())
                .set("totalWorkingDays", contract.getTotalWorkingDays())
                .set("rectificationLimit", contract.getRectificationLimit())
                .set("rectificationDaysEachTime", contract.getRectificationDaysEachTime());
        return updateWithContractId(contractId, update);
    }

    @Override
    public Boolean updateCustomerId(String contractId, Long customerId) {
        Update update = new Update().set("customerId", customerId);
        return updateWithContractId(contractId, update);
    }

    @Override
    public Boolean updateMarketerId(String contractId, Long marketerId) {
        Update update = new Update().set("marketerId", marketerId);
        return updateWithContractId(contractId, update);
    }

    @Override
    public Boolean updateScannedCopyPath(String contractId, String path) {
        Update update = new Update().set("scannedCopyPath", path);
        return updateWithContractId(contractId, update);
    }

    @Override
    public Boolean updateNonDisclosure(String contractId, NonDisclosureAgreement nonDisclosureAgreement) {
        Update update = new Update().set("nonDisclosureAgreement", nonDisclosureAgreement);
        return updateWithContractId(contractId, update);
    }

    @Override
    public Boolean updateStatus(String contractId, ContractStatus status) {
        Update update = new Update().set("status", status);
        return updateWithContractId(contractId, update);
    }

    // 使用配置好的更新选项根据ID更新合同对象
    private Boolean updateWithContractId(String contractId, Update update) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(contractId));
        UpdateResult result = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getMatchedCount() == 1;
    }

    @Override
    public Boolean deleteContractById(String contractId) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(contractId));
        DeleteResult result = mongoTemplate.remove(query, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getDeletedCount() == 1;
    }

}

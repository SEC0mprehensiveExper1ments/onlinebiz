package com.njustc.onlinebiz.contract.service;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.njustc.onlinebiz.contract.model.Contract;
import com.njustc.onlinebiz.contract.model.Outline;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Service;

import java.util.List;

// 为了减少实现的复杂性，这里我们先不写接口。如果要把业务类的接口抽象出来，
// 那还需要一个 ConditionBuilder 的接口，然后为不同的业务类写它的实现。
@Slf4j
@Service
public class MongoContractService implements ContractService {

    private final MongoTemplate mongoTemplate;

    public MongoContractService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // 创建一个空的合同
    @Override
    public Contract createContract(Long principalId, Long creatorId, String entrustId) {
        Contract contract = new Contract();
        contract.setPrincipalId(principalId);
        contract.setCreatorId(creatorId);
        contract.setEntrustId(entrustId);
        try {
            return mongoTemplate.insert(contract);
        } catch (Exception e) {
            log.warn("创建合同失败");
            return null;
        }
    }

    // 返回 MongoDB 中所有 Contract 对象投影到 Outline 对象的结果
    @Override
    public List<Outline> findAllContracts() {
        Query query = new BasicQuery("{}", Outline.PROJECT_FROM_CONTRACT);
        return mongoTemplate.find(query, Outline.class, Contract.COLLECTION_NAME);
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

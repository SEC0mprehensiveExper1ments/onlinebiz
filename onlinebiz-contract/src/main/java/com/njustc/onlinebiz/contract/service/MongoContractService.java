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

@Slf4j
@Service
public class MongoContractService implements ContractService {

    private final MongoTemplate mongoTemplate;

    public MongoContractService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // 创建一个空的合同
    @Override
    public Contract createContract(Long principalId, Long creatorId) {
        Contract contract = new Contract();
        contract.setPrincipalId(principalId);
        contract.setCreatorId(creatorId);
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
    public Contract findContractById(String contractId) {
        return mongoTemplate.findById(contractId, Contract.class);
    }

    // 更新一个合同，重新设置所有可编辑的域，除了合同 id 和用户 id
    @Override
    public boolean updateContract(Contract contract) {
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
    }

    @Override
    public boolean removeContract(String contractId) {
        Query query = new BasicQuery("{_id:ObjectId('" + contractId + "')}");
        DeleteResult result = mongoTemplate.remove(query, Contract.COLLECTION_NAME);
        return result.wasAcknowledged() && result.getDeletedCount() == 1;
    }

    public ConditionBuilder search() {
        return new ConditionBuilder();
    }

    // 用于构造查询条件的内部类，查询条件之间是 and 关系
    public class ConditionBuilder {

        private final StringBuilder condition = new StringBuilder("{$and:[");

        /* 使用正则表达式进行子序列匹配，即输入参数是要查询字段的一个子序列 */

        /**
         * 查询任意一方联系人的名字与 contactName 相匹配的合同
         * @param contactName 要查询的联系人名字
         */
        public ConditionBuilder byContact(String contactName) {
            if (contactName != null) {
                String pattern = convertToSubSeqPattern(contactName);
                condition.append("{$or:[{'principal.contact':{$regex:'").append(pattern).append("'}},{'trustee.contact':{$regex:'").append(pattern).append("'}}]},");
            }
            return this;
        }

        // 查询任意一方单位全称与 companyName 相匹配的合同
        public ConditionBuilder byCompany(String companyName) {
            if (companyName != null) {
                String pattern = convertToSubSeqPattern(companyName);
                condition.append("{$or:[{'principal.company':{$regex:'").append(pattern).append("'}},{'trustee.company':{$regex:'").append(pattern).append("'}}]},");
            }
            return this;
        }

        // 查询任意一方授权代表与 representativeName 相匹配的合同
        public ConditionBuilder byAuthorizedRepresentative(String representativeName) {
            if (representativeName != null) {
                String pattern = convertToSubSeqPattern(representativeName);
                condition.append("{$or:[{'principal.authorizedRepresentative':{$regex:'").append(pattern).append("'}},{'trustee.authorizedRepresentative':{$regex:'").append(pattern).append("'}}]},");
            }
            return this;
        }

        // 查询项目名称与 projectName 相匹配的合同
        public ConditionBuilder byProjectName(String projectName) {
            if (projectName != null) {
                String pattern = convertToSubSeqPattern(projectName);
                condition.append("{projectName:{$regex:'").append(pattern).append("'}},");
            }
            return this;
        }

        // 查询受测软件与 targetSoftware 相匹配的合同
        public ConditionBuilder byTargetSoftware(String targetSoftware) {
            if (targetSoftware != null) {
                String pattern = convertToSubSeqPattern(targetSoftware);
                condition.append("{targetSoftware:{$regex:'").append(pattern).append("'}},");
            }
            return this;
        }

        // 根据委托方用户 id 查询合同
        public ConditionBuilder byPrincipalId(Long principalId) {
            if (principalId != null) {
                condition.append("{principalId:").append(principalId).append("},");
            }
            return this;
        }

        // 获取查询结果
        public List<Outline> getResult() {
            Query query = new BasicQuery(condition.append("]}").toString());
            return mongoTemplate.find(query, Outline.class, Contract.COLLECTION_NAME);
        }

        // 把查询参数转换为正则表达式，主要是在字符前后加 ".*" 实现模糊匹配
        private String convertToSubSeqPattern(String param) {
            StringBuilder pattern = new StringBuilder(".*");
            for (int i = 0; i < param.length(); ++i) {
                pattern.append(param.charAt(i)).append(".*");
            }
            return pattern.toString();
        }
    }
}

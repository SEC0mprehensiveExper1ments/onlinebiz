package com.njustc.onlinebiz.contract.service;

import com.njustc.onlinebiz.common.model.Contract;
import com.njustc.onlinebiz.common.model.ContractOutline;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

// 使用 MongoDB 的正则表达式功能进行子序列匹配，即输入参数是要查询字段的一个子序列
public class MongoConditionBuilder implements ConditionBuilder {

    private final MongoTemplate mongoTemplate;

    private final StringBuilder condition = new StringBuilder("{$and:[");

    public MongoConditionBuilder(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public ConditionBuilder byContact(String contactName) {
        if (contactName != null) {
            String pattern = convertToSubSeqPattern(contactName);
            condition.append("{$or:[{'principal.contact':{$regex:'").append(pattern).append("'}},{'trustee.contact':{$regex:'").append(pattern).append("'}}]},");
        }
        return this;
    }

    public ConditionBuilder byCompany(String companyName) {
        if (companyName != null) {
            String pattern = convertToSubSeqPattern(companyName);
            condition.append("{$or:[{'principal.company':{$regex:'").append(pattern).append("'}},{'trustee.company':{$regex:'").append(pattern).append("'}}]},");
        }
        return this;
    }

    public ConditionBuilder byAuthorizedRepresentative(String representativeName) {
        if (representativeName != null) {
            String pattern = convertToSubSeqPattern(representativeName);
            condition.append("{$or:[{'principal.authorizedRepresentative':{$regex:'").append(pattern).append("'}},{'trustee.authorizedRepresentative':{$regex:'").append(pattern).append("'}}]},");
        }
        return this;
    }

    public ConditionBuilder byProjectName(String projectName) {
        if (projectName != null) {
            String pattern = convertToSubSeqPattern(projectName);
            condition.append("{projectName:{$regex:'").append(pattern).append("'}},");
        }
        return this;
    }

    public ConditionBuilder byTargetSoftware(String targetSoftware) {
        if (targetSoftware != null) {
            String pattern = convertToSubSeqPattern(targetSoftware);
            condition.append("{targetSoftware:{$regex:'").append(pattern).append("'}},");
        }
        return this;
    }

    public ConditionBuilder byPrincipalId(Long principalId) {
        if (principalId != null) {
            condition.append("{principalId:").append(principalId).append("},");
        }
        return this;
    }

    public List<ContractOutline> getResult() {
        Query query = new BasicQuery(condition.append("]}").toString());
        return mongoTemplate.find(query, ContractOutline.class, Contract.COLLECTION_NAME);
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

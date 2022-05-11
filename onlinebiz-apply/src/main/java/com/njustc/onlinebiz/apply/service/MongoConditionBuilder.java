package com.njustc.onlinebiz.apply.service;

import com.njustc.onlinebiz.apply.model.Apply;
import com.njustc.onlinebiz.apply.model.Outline;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class MongoConditionBuilder implements ConditionBuilder {

    private final MongoTemplate mongoTemplate;

    private final StringBuilder condition = new StringBuilder("{$and:[");

    public MongoConditionBuilder(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ConditionBuilder byContact(String contactName) {
        if (contactName != null) {
            String pattern = convertToSubSeqPattern(contactName);
            condition.append("{projectName:{$regex:'").append(pattern).append("'}},");
        }
        return this;
    }

    @Override
    public ConditionBuilder byCompany(String companyName) {
        if (companyName != null) {
            String pattern = convertToSubSeqPattern(companyName);
            condition.append("{projectName:{$regex:'").append(pattern).append("'}},");
        }
        return this;
    }

    @Override
    public ConditionBuilder byTestType(String testType) {
        if (testType != null) {
            String pattern = convertToSubSeqPattern(testType);
            condition.append("{projectName:{$regex:'").append(pattern).append("'}},");
        }
        return this;
    }

    @Override
    public ConditionBuilder byTargetSoftware(String targetSoftware) {
        if (targetSoftware != null) {
            String pattern = convertToSubSeqPattern(targetSoftware);
            condition.append("{targetSoftware:{$regex:'").append(pattern).append("'}},");
        }
        return this;
    }

    @Override
    public ConditionBuilder byPrincipalId(Long principalId) {
        if (principalId != null) {
            condition.append("{principalId:").append(principalId).append("},");
        }
        return this;
    }

    @Override
    public List<Outline> getResult() {
        Query query = new BasicQuery(condition.append("]}").toString());
        return mongoTemplate.find(query, Outline.class, Apply.COLLECTION_NAME);
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

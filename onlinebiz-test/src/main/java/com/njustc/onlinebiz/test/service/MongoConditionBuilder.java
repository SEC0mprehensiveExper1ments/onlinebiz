package com.njustc.onlinebiz.test.service;

import com.njustc.onlinebiz.common.model.Scheme;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class MongoConditionBuilder implements ConditionBuilder{
    private final MongoTemplate mongoTemplate;

    private final StringBuilder condition = new StringBuilder("{$and:[");

    public MongoConditionBuilder(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // 把查询参数转换为正则表达式，主要是在字符前后加 ".*" 实现模糊匹配
    private String convertToSubSeqPattern(String param) {
        StringBuilder pattern = new StringBuilder(".*");
        for (int i = 0; i < param.length(); ++i) {
            pattern.append(param.charAt(i)).append(".*");
        }
        return pattern.toString();
    }

    @Override
    public ConditionBuilder byApplyId(String applyId) {
        if (applyId != null) {
            String pattern = convertToSubSeqPattern(applyId);
            condition.append("{applyId:{$regex:'").append(pattern).append("'}},");
        }
        return this;
    }

    @Override
    public ConditionBuilder byContractId(String contractId) {
        if (contractId != null) {
            String pattern = convertToSubSeqPattern(contractId);
            condition.append("{contractId:{$regex:'").append(pattern).append("'}},");
        }
        return this;
    }

    @Override
    public List<Scheme> getResult() {
        Query query = new BasicQuery(condition.append("]}").toString());
        return mongoTemplate.find(query, Scheme.class, Scheme.COLLECTION_NAME);
    }
}

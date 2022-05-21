//package com.njustc.onlinebiz.test.service.schemeService;
//
//import com.njustc.onlinebiz.common.model.Scheme;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.BasicQuery;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.web.client.RestTemplate;
//
//import javax.validation.constraints.NotNull;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class MongoConditionBuilder implements ConditionBuilder {
//
//    private static final String APPLY_SERVICE_URL = "http://onlinebiz-apply";
//    private final MongoTemplate mongoTemplate;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    private final StringBuilder condition = new StringBuilder("{$and:[");
//
//    public MongoConditionBuilder(MongoTemplate mongoTemplate) {
//        this.mongoTemplate = mongoTemplate;
//    }
//
//    @Override
//    public ConditionBuilder byContact(String contactName) {
//        // 调用apply模块得到对应的委托申请
//        Map<String, String> map = new HashMap<>();
//        map.put("contactName", contactName);
//        return callApplyMethod(map);
//    }
//
//    @Override
//    public ConditionBuilder byCompany(String companyName) {
//        // 调用apply模块得到对应的委托申请
//        Map<String, String> map = new HashMap<>();
//        map.put("companyName", companyName);
//        return callApplyMethod(map);
//    }
//
//    @Override
//    public ConditionBuilder byProjectName(String projectName) {
//        // 调用apply模块得到对应的委托申请
//        Map<String, String> map = new HashMap<>();
//        map.put("projectName", projectName);
//        return callApplyMethod(map);
//    }
//
//    @Override
//    public ConditionBuilder byTargetSoftware(String targetSoftware) {
//        Map<String, String> map = new HashMap<>();
//        map.put("targetSoftware", targetSoftware);
//        return callApplyMethod(map);
//    }
//
//    @Override
//    public List<Scheme> getResult() {
//        Query query = new BasicQuery(condition.append("]}").toString());
//        return mongoTemplate.find(query, Scheme.class, Scheme.COLLECTION_NAME);
//    }
//
//    @Override
//    public ConditionBuilder byCreatorId(Long creatorId) {
//        if (creatorId != null) {
//            condition.append("{creatorId:").append(creatorId).append("},");
//        }
//        return this;
//    }
//
//    @NotNull
//    private ConditionBuilder callApplyMethod(Map<String, String> map) {
//        Apply apply = restTemplate.getForObject(APPLY_SERVICE_URL + "/apply/search", Apply.class, map);
//        String pattern;
//        if (apply == null) {
//            pattern = convertToSubSeqPattern("errorId");
//        } else {
//            pattern = convertToSubSeqPattern(apply.getId());
//        }
//        condition.append("{applyId:{$regex:'").append(pattern).append("'}},");
//        return this;
//    }
//
//    // 把查询参数转换为正则表达式，主要是在字符前后加 ".*" 实现模糊匹配
//    private String convertToSubSeqPattern(String param) {
//        StringBuilder pattern = new StringBuilder(".*");
//        for (int i = 0; i < param.length(); ++i) {
//            pattern.append(param.charAt(i)).append(".*");
//        }
//        return pattern.toString();
//    }
//}

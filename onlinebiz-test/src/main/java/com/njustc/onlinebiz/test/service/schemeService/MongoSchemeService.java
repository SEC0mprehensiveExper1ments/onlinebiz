//package com.njustc.onlinebiz.test.service.schemeService;
//
//import com.mongodb.client.result.DeleteResult;
//import com.mongodb.client.result.UpdateResult;
//import com.njustc.onlinebiz.test.model.Scheme;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.BasicQuery;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.data.mongodb.core.query.UpdateDefinition;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//public class MongoSchemeService implements SchemeService {
//    private final MongoTemplate mongoTemplate;
//
//    public MongoSchemeService(MongoTemplate mongoTemplate) {
//        this.mongoTemplate = mongoTemplate;
//    }
//
//    @Override
//    public Scheme createScheme(Long creatorId, String applyId, Scheme scheme) {
//        scheme.setCreatorId(creatorId);
//        scheme.setApplyId(applyId);
//        try {
//            return mongoTemplate.insert(scheme, Scheme.COLLECTION_NAME);
//        } catch (Exception e) {
//            log.warn("创建测试方案失败");
//            return null;
//        }
//    }
//
//    @Override
//    public Scheme findSchemeById(String schemeId) {
//        return mongoTemplate.findById(schemeId, Scheme.class);
//    }
//
//    @Override
//    public Scheme findSchemeByIdAndCreator(String schemeId, Long userId) {
//        Scheme scheme = findSchemeById(schemeId);
//        if (scheme != null && userId.equals(scheme.getCreatorId())) {
//            // 的确是该后台管理人员创建的测试方案
//            return scheme;
//        }
//        return null;
//    }
//
//    @Override
//    public boolean updateScheme(Scheme scheme) {
//        // ID 不合法有可能抛出异常
//        try {
//            Query query = new BasicQuery("{_id:ObjectId('" + scheme.getId() + "')}");
//            UpdateDefinition updateDefinition = new Update()
//                    .set("version", scheme.getVersion())
//                    .set("modificationList", scheme.getModificationList())
//                    .set("logo", scheme.getLogo())
//                    .set("systemSummary", scheme.getSystemSummary())
//                    .set("documentSummary", scheme.getDocumentSummary())
//                    .set("baseline", scheme.getBaseline())
//                    .set("hardware", scheme.getHardware())
//                    .set("software", scheme.getSoftware())
//                    .set("otherEnvironment", scheme.getOtherEnvironment())
//                    .set("organization", scheme.getOrganization())
//                    .set("participant", scheme.getParticipant())
//                    .set("testLevel", scheme.getTestLevel())
//                    .set("testType", scheme.getTestType())
//                    .set("testCondition", scheme.getTestCondition())
//                    .set("testToBeExecuted", scheme.getTestToBeExecuted())
//                    .set("testSample", scheme.getTestSample())
//                    .set("planSchedule", scheme.getPlanSchedule())
//                    .set("designSchedule", scheme.getDesignSchedule())
//                    .set("executeSchedule", scheme.getExecuteSchedule())
//                    .set("evaluateSchedule", scheme.getEvaluateSchedule())
//                    .set("traceability", scheme.getTraceability());
//            UpdateResult result = mongoTemplate.updateFirst(query, updateDefinition, Scheme.COLLECTION_NAME);
//            return result.wasAcknowledged() && result.getMatchedCount() == 1;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    @Override
//    public boolean removeScheme(String schemeId) {
//        try {
//            Query query = new BasicQuery("{_id:ObjectId('" + schemeId + "')}");
//            DeleteResult result = mongoTemplate.remove(query, Scheme.COLLECTION_NAME);
//            return result.wasAcknowledged() && result.getDeletedCount() == 1;
//        } catch (Exception e) {
//            // in case of invalid schemeId
//            return false;
//        }
//    }
//
//    @Override
//    public ConditionBuilder search() {
//        return new MongoConditionBuilder(mongoTemplate);
//    }
//}

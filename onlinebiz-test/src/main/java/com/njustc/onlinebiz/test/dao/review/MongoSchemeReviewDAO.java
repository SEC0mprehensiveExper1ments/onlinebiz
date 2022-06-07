package com.njustc.onlinebiz.test.dao.review;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.njustc.onlinebiz.common.model.test.review.SchemeReview;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class MongoSchemeReviewDAO implements SchemeReviewDAO{
    public static final String COLLECTION_NAME = "schemeReview";
    private final MongoTemplate mongoTemplate;

    public MongoSchemeReviewDAO(MongoTemplate mongoTemplate) { this.mongoTemplate = mongoTemplate; }

    @Override
    public SchemeReview insertSchemeReview(SchemeReview schemeReview) {
        return mongoTemplate.insert(schemeReview, COLLECTION_NAME);
    }

    @Override
    public SchemeReview findSchemeReviewById(String schemeReviewId) {
        return mongoTemplate.findById(schemeReviewId, SchemeReview.class);
    }

    @Override
    public Boolean updateSchemeReview(String schemeReviewId, SchemeReview schemeReview) {
        Update update = new Update()
                .set("softwareName", schemeReview.getSoftwareName())
                .set("version", schemeReview.getVersion())
                .set("projectId", schemeReview.getProjectId())
                .set("testType", schemeReview.getTestType())
                .set("conclusions", schemeReview.getConclusions());
        return updateFirstWithId(schemeReviewId, update);
    }

    @Override
    public Boolean updateScannedCopyPath(String schemeReviewId, String path) {
        Update update = new Update().set("scannedCopyPath", path);
        return updateFirstWithId(schemeReviewId, update);
    }

    @Override
    public Boolean deleteSchemeAuditById(String schemeReviewId) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(schemeReviewId)));
        DeleteResult result = mongoTemplate.remove(query, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getDeletedCount() == 1;
    }

    // 根据方案评审 id 更新方案评审表，由传入的 Update 对象设置要更新的部分
    private Boolean updateFirstWithId(String schemeReviewId, Update update) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(schemeReviewId)));
        UpdateResult result = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getMatchedCount() == 1;
    }
}

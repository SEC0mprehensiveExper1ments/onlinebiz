package com.njustc.onlinebiz.test.dao.review;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.njustc.onlinebiz.common.model.test.review.ReportReview;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class MongoReportReviewDAO implements ReportReviewDAO {
    public static final String COLLECTION_NAME = "reportReview";
    private final MongoTemplate mongoTemplate;

    public MongoReportReviewDAO(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ReportReview insertReportReview(ReportReview reportReview) {
        return mongoTemplate.insert(reportReview, COLLECTION_NAME);
    }

    @Override
    public ReportReview findReportReviewById(String reportReviewId) {
        return mongoTemplate.findById(reportReviewId, ReportReview.class, COLLECTION_NAME);
    }

    @Override
    public Boolean updateReportReview(String reportReviewId, ReportReview reportReview) {
        Update update = new Update()
                .set("softwareName", reportReview.getSoftwareName())
                .set("principal", reportReview.getPrincipal())
                .set("conclusions", reportReview.getConclusions());
        return updateFirstWithId(reportReviewId, update);
    }

    @Override
    public Boolean updateScannedCopyPath(String reportReviewId, String path) {
        Update update = new Update().set("scannedCopyPath", path);
        return updateFirstWithId(reportReviewId, update);
    }

    @Override
    public Boolean deleteReportAuditById(String reportReviewId) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(reportReviewId)));
        DeleteResult result = mongoTemplate.remove(query, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getDeletedCount() == 1;
    }

    // 根据方案评审 id 更新方案评审表，由传入的 Update 对象设置要更新的部分
    private Boolean updateFirstWithId(String reportReviewId, Update update) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(reportReviewId)));
        UpdateResult result = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getMatchedCount() == 1;
    }
}

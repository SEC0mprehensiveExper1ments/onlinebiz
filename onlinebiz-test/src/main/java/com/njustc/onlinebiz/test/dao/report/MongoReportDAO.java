package com.njustc.onlinebiz.test.dao.report;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.njustc.onlinebiz.common.model.test.report.Report;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class MongoReportDAO implements ReportDAO {
  public static final String COLLECTION_NAME = "testReport";
  private final MongoTemplate mongoTemplate;

  public MongoReportDAO(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Report insertReport(Report report) {
    return mongoTemplate.insert(report, COLLECTION_NAME);
  }

  @Override
  public Report findReportById(String id) {
    return mongoTemplate.findById(id, Report.class, COLLECTION_NAME);
  }

  @Override
  public boolean updateContent(String id, Report.ReportContent content) {
    Update update = new Update().set("content", content);
    return updateFirstWithId(id, update);
  }

  @Override
  public boolean deleteReport(String id) {
    Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(id)));
    DeleteResult result = mongoTemplate.remove(query, COLLECTION_NAME);
    return result.wasAcknowledged() && result.getDeletedCount() == 1;
  }

  private boolean updateFirstWithId(String id, Update update) {
    Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(id)));
    UpdateResult result = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
    return result.wasAcknowledged() && result.getMatchedCount() == 1;
  }
}

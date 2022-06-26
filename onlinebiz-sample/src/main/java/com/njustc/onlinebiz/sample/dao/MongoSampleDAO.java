package com.njustc.onlinebiz.sample.dao;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.njustc.onlinebiz.common.model.sample.SampleCollection;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongoSampleDAO implements SampleDAO {

  private static final String COLLECTION_NAME = "sampleCollection";
  private final MongoTemplate mongoTemplate;

  public MongoSampleDAO(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public SampleCollection insertSampleCollection(SampleCollection sampleCollection) {
    return mongoTemplate.insert(sampleCollection, COLLECTION_NAME);
  }

  @Override
  public long countAll() {
    return mongoTemplate.count(new Query(), COLLECTION_NAME);
  }

  @Override
  public List<SampleCollection> findAllCollections(Integer page, Integer pageSize) {
    Query query = new Query();
    return findWithProjection(query, page, pageSize);
  }


  @Override
  public SampleCollection findSampleCollectionById(String sampleCollectionId) {
    return mongoTemplate.findById(sampleCollectionId, SampleCollection.class, COLLECTION_NAME);
  }

  @Override
  public Boolean updateSampleCollection(String sampleCollectionId, SampleCollection sampleCollection) {
    Update update =
        new Update()
            .set("name", sampleCollection.getName())
            .set("samples", sampleCollection.getSamples())
            .set("stage", sampleCollection.getStage());
    return updateSampleCollectionById(sampleCollectionId, update);
  }

  @Override
  public Boolean deleteSampleCollection(String id) {
    Query query = new Query().addCriteria(Criteria.where("_id").is(id));
    DeleteResult result = mongoTemplate.remove(query, COLLECTION_NAME);
    return result.wasAcknowledged() && result.getDeletedCount() == 1;
  }

  private List<SampleCollection> findWithProjection(Query query, Integer page, Integer pageSize) {
    // 设置要取回的字段
    query.fields().include("_id");
    query.fields().include("entrustId");
    query.fields().include("marketerId");
    query.fields().include("name");
    query.fields().include("samples");
    query.fields().include("stage");
    return findWithPagination(query, page, pageSize);
  }

  private List<SampleCollection> findWithPagination(Query query, Integer page, Integer pageSize) {
    // 计算起始位置，页码从1开始
    int offset = (page - 1) * pageSize;
    // 先不优化分页查询
    query.skip(offset).limit(pageSize);
    return mongoTemplate.find(query, SampleCollection.class, COLLECTION_NAME);
  }

  private Boolean updateSampleCollectionById(String id, Update update) {
    Query query = new Query().addCriteria(Criteria.where("_id").is(id));
    UpdateResult result = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
    return result.wasAcknowledged() && result.getMatchedCount() == 1;
  }
}

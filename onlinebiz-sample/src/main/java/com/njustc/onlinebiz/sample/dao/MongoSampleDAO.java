package com.njustc.onlinebiz.sample.dao;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.njustc.onlinebiz.common.model.sample.SampleCollection;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

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
  public SampleCollection findSampleCollectionById(String id) {
    return mongoTemplate.findById(id, SampleCollection.class, COLLECTION_NAME);
  }

  @Override
  public Boolean updateSampleCollection(String id, SampleCollection sampleCollection) {
    Update update =
        new Update()
            .set("name", sampleCollection.getName())
            .set("samples", sampleCollection.getSamples())
            .set("stage", sampleCollection.getStage());
    return updateSampleCollectionById(id, update);
  }

  @Override
  public Boolean deleteSampleCollection(String id) {
    Query query = new Query().addCriteria(Criteria.where("_id").is(id));
    DeleteResult result = mongoTemplate.remove(query, COLLECTION_NAME);
    return result.wasAcknowledged() && result.getDeletedCount() == 1;
  }

  private Boolean updateSampleCollectionById(String id, Update update) {
    Query query = new Query().addCriteria(Criteria.where("_id").is(id));
    UpdateResult result = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
    return result.wasAcknowledged() && result.getMatchedCount() == 1;
  }
}

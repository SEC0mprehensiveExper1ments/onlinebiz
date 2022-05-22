package com.njustc.onlinebiz.test.dao.scheme;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.njustc.onlinebiz.test.model.scheme.Scheme;
import com.njustc.onlinebiz.test.model.scheme.SchemeContent;
import com.njustc.onlinebiz.test.model.scheme.SchemeStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class MongoSchemeDAO implements SchemeDAO {
    public static final String COLLECTION_NAME = "testScheme";

    private final MongoTemplate mongoTemplate;

    public MongoSchemeDAO(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Scheme findSchemeById(String schemeId) {
        return mongoTemplate.findById(schemeId, Scheme.class);
    }

    @Override
    public Scheme insertScheme(Scheme scheme) {
        return mongoTemplate.insert(scheme, COLLECTION_NAME);
    }

    @Override
    public boolean updateContent(String schemeId, SchemeContent content) {
        Update update = new Update().set("content", content);
        return updateFirstWithId(schemeId, update);
    }

    @Override
    public boolean updateStatus(String schemeId, SchemeStatus status) {
        Update update = new Update().set("status", status);
        return updateFirstWithId(schemeId, update);
    }

    @Override
    public boolean deleteScheme(String schemeId) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(schemeId)));
        DeleteResult result = mongoTemplate.remove(query, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getDeletedCount() == 1;
    }

    // 根据测试方案ID更新测试方案，由传入的 Update 对象设置要更新的部分
    private Boolean updateFirstWithId(String schemeId, Update update) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(schemeId)));
        UpdateResult result = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getMatchedCount() == 1;
    }
}

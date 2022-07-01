package com.njustc.onlinebiz.test.dao.testrecord;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.njustc.onlinebiz.common.model.test.testrecord.TestRecordList;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongoTestRecordDAO implements TestRecordDAO {
    public static final String COLLECTION_NAME = "testRecord";

    private final MongoTemplate mongoTemplate;

    public MongoTestRecordDAO(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public TestRecordList insertTestRecordList(TestRecordList testRecordList) {
        return mongoTemplate.insert(testRecordList, COLLECTION_NAME);
    }

    @Override
    public TestRecordList findTestRecordListById(String testRecordListId) {
        return mongoTemplate.findById(testRecordListId, TestRecordList.class, COLLECTION_NAME);
    }

    @Override
    public boolean updateContent(String testRecordListId, List<TestRecordList.TestRecord> content) {
        Update update = new Update().set("testRecords", content);
        return updateFirstWithId(testRecordListId, update);
    }


    @Override
    public boolean deleteTestRecordList(String testRecordListId) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(testRecordListId)));
        DeleteResult result = mongoTemplate.remove(query, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getDeletedCount() == 1;
    }

    // 根据测试用例表ID更新测试记录表，由传入的 Update 对象设置要更新的部分
    private Boolean updateFirstWithId(String testRecordListId, Update update) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(testRecordListId)));
        UpdateResult result = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getMatchedCount() == 1;
    }
}

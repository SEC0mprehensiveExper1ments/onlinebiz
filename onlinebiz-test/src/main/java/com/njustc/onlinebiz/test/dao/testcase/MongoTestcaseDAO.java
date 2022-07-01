package com.njustc.onlinebiz.test.dao.testcase;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.njustc.onlinebiz.common.model.test.testcase.Testcase;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongoTestcaseDAO implements TestcaseDAO {
    public static final String COLLECTION_NAME = "testcase";

    private final MongoTemplate mongoTemplate;

    public MongoTestcaseDAO(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Testcase insertTestcaseList(Testcase testcaseList) {
        return mongoTemplate.insert(testcaseList, COLLECTION_NAME);
    }

    @Override
    public Testcase findTestcaseListById(String testcaseListId) {
        return mongoTemplate.findById(testcaseListId, Testcase.class, COLLECTION_NAME);
    }

    @Override
    public boolean updateContent(String testcaseListId, List<Testcase.TestcaseList> content) {
        Update update = new Update().set("testcases", content);
        return updateFirstWithId(testcaseListId, update);
    }

    @Override
    public boolean deleteTestcaseList(String testcaseListId) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(testcaseListId)));
        DeleteResult result = mongoTemplate.remove(query, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getDeletedCount() == 1;
    }

    // 根据测试用例表ID更新测试用例表，由传入的 Update 对象设置要更新的部分
    private Boolean updateFirstWithId(String testcaseListId, Update update) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(testcaseListId)));
        UpdateResult result = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getMatchedCount() == 1;
    }
}

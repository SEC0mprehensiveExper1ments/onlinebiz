package com.njustc.onlinebiz.test.dao.testissue;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.njustc.onlinebiz.common.model.test.testissue.TestIssueList;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongoTestIssueDAO implements TestIssueDAO {
    public static final String COLLECTION_NAME = "testIssue";

    private final MongoTemplate mongoTemplate;

    public MongoTestIssueDAO(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public TestIssueList insertTestIssueList(TestIssueList testIssueList) {
        return mongoTemplate.insert(testIssueList, COLLECTION_NAME);
    }

    @Override
    public TestIssueList findTestIssueListById(String testIssueListId) {
        return mongoTemplate.findById(testIssueListId, TestIssueList.class, COLLECTION_NAME);
    }

    @Override
    public boolean updateContent(String testIssueListId, List<TestIssueList.TestIssue> content) {
        Update update = new Update().set("testIssues", content);
        return updateFirstWithId(testIssueListId, update);
    }


    @Override
    public boolean deleteTestIssueList(String testIssueListId) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(testIssueListId)));
        DeleteResult result = mongoTemplate.remove(query, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getDeletedCount() == 1;
    }

    // 根据测试用例表ID更新测试记录表，由传入的 Update 对象设置要更新的部分
    private Boolean updateFirstWithId(String testIssueListId, Update update) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(testIssueListId)));
        UpdateResult result = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getMatchedCount() == 1;
    }
}

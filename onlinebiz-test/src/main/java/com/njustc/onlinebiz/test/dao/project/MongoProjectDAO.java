package com.njustc.onlinebiz.test.dao.project;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.njustc.onlinebiz.test.model.project.Project;
import com.njustc.onlinebiz.test.model.project.ProjectStatus;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * 使用MongoDB的数据访问层实现。这里假设服务层已校验了参数的合法性。
 * */
@Component
public class MongoProjectDAO implements ProjectDAO {

    public static final String COLLECTION_NAME = "project";

    private final MongoTemplate mongoTemplate;

    public MongoProjectDAO(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Project insertProject(Project project) {
        return mongoTemplate.insert(project, COLLECTION_NAME);
    }

    @Override
    public Project findProjectById(String projectId) {
        return mongoTemplate.findById(projectId, Project.class);
    }

    @Override
    public Boolean updateQaId(String projectId, Long qaId) {
        Update update = new Update().set("qaId", qaId);
        return updateFirstWithId(projectId, update);
    }

    @Override
    public Boolean deleteProject(String projectId) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(projectId)));
        DeleteResult result = mongoTemplate.remove(query, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getDeletedCount() == 1;
    }

    @Override
    public boolean updateStatus(String projectId, ProjectStatus status) {
        Update update = new Update().set("status", status);
        return updateFirstWithId(projectId, update);
    }

    // 根据测试项目ID更新测试项目，由传入的 Update 对象设置要更新的部分
    private Boolean updateFirstWithId(String projectId, Update update) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId()));
        UpdateResult result = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getMatchedCount() == 1;
    }
}

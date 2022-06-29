package com.njustc.onlinebiz.test.dao.project;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.njustc.onlinebiz.common.model.test.project.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
        Project project = findProjectById(projectId);
        if (project == null) {
            return false;
        }
        ProjectBaseInfo projectBaseInfo = project.getProjectBaseInfo();
        projectBaseInfo.setQaId(qaId);
        Update update = new Update().set("projectBaseInfo", projectBaseInfo);
        return updateFirstWithId(projectId, update);
    }

    @Override
    public Boolean updateFormIds(String projectId, ProjectFormIds projectFormIds) {
        Project project = findProjectById(projectId);
        Update update = new Update().set("projectFormIds", projectFormIds);
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

    @Override
    public long countAll() {
        return mongoTemplate.count(new Query(), COLLECTION_NAME);
    }

    @Override
    public List<ProjectOutline> findAllProjects(Integer page, Integer pageSize) {
        Query query = new Query();
        return findWithProjection(query, page, pageSize);
    }

    @Override
    public long countByMarketerId(Long marketerId) {
        Query query = new Query().addCriteria(Criteria.where("projectBaseInfo.marketerId").is(marketerId));
        return mongoTemplate.count(query, COLLECTION_NAME);
    }

    @Override
    public List<ProjectOutline> findProjectByMarketerId(Long marketerId, Integer page, Integer pageSize) {
        Query query = new Query().addCriteria(Criteria.where("projectBaseInfo.marketerId").is(marketerId));
        return findWithProjection(query, page, pageSize);
    }

    @Override
    public long countByTesterId(Long testerId) {
        Query query = new Query().addCriteria(Criteria.where("projectBaseInfo.testerId").is(testerId));
        return mongoTemplate.count(query, COLLECTION_NAME);
    }

    @Override
    public List<ProjectOutline> findProjectByTesterId(Long testerId, Integer page, Integer pageSize) {
        Query query = new Query().addCriteria(Criteria.where("projectBaseInfo.testerId").is(testerId));
        return findWithProjection(query, page, pageSize);
    }

    @Override
    public long countByQaId(Long qaId) {
        Query query = new Query().addCriteria(Criteria.where("projectBaseInfo.qaId").is(qaId));
        return mongoTemplate.count(query, COLLECTION_NAME);
    }

    @Override
    public List<ProjectOutline> findProjectByQaId(Long qaId, Integer page, Integer pageSize) {
        Query query = new Query().addCriteria(Criteria.where("projectBaseInfo.qaId").is(qaId));
        return findWithProjection(query, page, pageSize);
    }

    // 提取出查询的项目基本信息
    private List<ProjectOutline> findWithProjection(Query query, Integer page, Integer pageSize) {
        // 设置要取回的字段
        query.fields().include("_id");
        query.fields().include("projectBaseInfo");
        query.fields().include("status");
        List<Project> results = findWithPagination(query, page, pageSize);
        // 提取出查询的项目信息
        return results.stream().map(ProjectOutline::new).collect(Collectors.toList());
    }

    // 根据查询条件进行分页查询
    private List<Project> findWithPagination(Query query, Integer page, Integer pageSize) {
        // 计算其实位置，页码从1开始
        int offset = (page - 1) * pageSize;
        // 先不优化分页查询
        query.skip(offset).limit(pageSize);
        return mongoTemplate.find(query, Project.class, COLLECTION_NAME);
    }

    // 根据测试项目ID更新测试项目，由传入的 Update 对象设置要更新的部分
    private Boolean updateFirstWithId(String projectId, Update update) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(projectId)));
        UpdateResult result = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getMatchedCount() == 1;
    }
}

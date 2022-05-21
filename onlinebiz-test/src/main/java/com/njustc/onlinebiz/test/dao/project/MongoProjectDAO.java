package com.njustc.onlinebiz.test.dao.project;

import com.njustc.onlinebiz.test.model.Project;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoProjectDAO implements ProjectDAO {

    public static final String COLLECTION_NAME = "testProject";

    private final MongoTemplate mongoTemplate;

    public MongoProjectDAO(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 保存一份新的测试项目到数据库
     * @param project 测试项目实体
     * @return 保存后的测试项目实体
     */
    @Override
    public Project insertProject(Project project) {
        return mongoTemplate.insert(project, COLLECTION_NAME);
    }

    /**
     * 根据测试项目 ID 查询测试项目实体
     * @param projectId 要查询的测试项目 ID
     * @return 查询得到的测试项目实体
     */
    @Override
    public Project findProjectById(String projectId) {
        return mongoTemplate.findById(projectId, Project.class);
    }
}

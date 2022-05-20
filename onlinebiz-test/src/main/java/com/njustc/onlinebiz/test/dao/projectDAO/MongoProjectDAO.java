package com.njustc.onlinebiz.test.dao.projectDAO;

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

    @Override
    public Project insertProject(Project project) {
        return mongoTemplate.insert(project, COLLECTION_NAME);
    }

    @Override
    public Project findProjectById(String projectId) {
        return mongoTemplate.findById(projectId, Project.class);
    }
}

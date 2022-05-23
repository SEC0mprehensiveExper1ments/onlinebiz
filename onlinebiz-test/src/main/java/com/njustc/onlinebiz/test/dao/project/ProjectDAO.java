package com.njustc.onlinebiz.test.dao.project;

import com.njustc.onlinebiz.test.model.Project;

public interface ProjectDAO {

    Project insertProject(Project project);

    Project findProjectById(String projectId);
}

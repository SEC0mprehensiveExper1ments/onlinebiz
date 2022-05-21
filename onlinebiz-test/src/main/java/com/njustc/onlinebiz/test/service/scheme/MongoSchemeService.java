package com.njustc.onlinebiz.test.service.scheme;

import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.test.dao.scheme.SchemeDAO;
import com.njustc.onlinebiz.test.exception.scheme.SchemeNotFoundException;
import com.njustc.onlinebiz.test.exception.scheme.SchemePermissionDeniedException;
import com.njustc.onlinebiz.test.model.Scheme;
import com.njustc.onlinebiz.test.model.SchemeContent;
import com.njustc.onlinebiz.test.model.SchemeStage;
import com.njustc.onlinebiz.test.model.SchemeStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class MongoSchemeService implements SchemeService {

    private static final String ENTRUST_SERVICE_URL = "http://onlinebiz-entrust";
    private final RestTemplate restTemplate;
    private final SchemeDAO schemeDAO;

    public MongoSchemeService(RestTemplate restTemplate, MongoTemplate mongoTemplate, SchemeDAO schemeDAO) {
        this.restTemplate = restTemplate;
        this.schemeDAO = schemeDAO;
    }

    @Override
    public boolean updateScheme(Scheme scheme, Long userId, Role userRole) {
        return false;
    }

    @Override
    public boolean removeScheme(String schemeId, Long userId, Role userRole) {
        return false;
    }

    @Override
    public String createScheme(String entrustId, SchemeContent schemeContent, Long userId, Role userRole) {
        if (!hasAuthorityToCreate(userRole)) {
            throw new SchemePermissionDeniedException("无权新建测试方案");
        }
        Scheme scheme = new Scheme();
        scheme.setCreatorId(userId);
        scheme.setSchemeContent(schemeContent);
        scheme.setSchemeStatus(new SchemeStatus(SchemeStage.UNFILLED, "等待填写测试方案"));
        return schemeDAO.insertScheme(scheme).getId();
    }

    @Override
    public Scheme findScheme(String schemeId, Long userId, Role userRole) {
        Scheme scheme = schemeDAO.findSchemeById(schemeId);
        if (scheme == null) {
            throw new SchemeNotFoundException("该测试项目不存在");
        }
        if (!hasAuthorityToCheck(userId, userRole, scheme)) {
            throw new SchemePermissionDeniedException("无权查看该测试项目");
        }
        return scheme;
    }

    private boolean hasAuthorityToCheck(Long userId, Role userRole, Scheme scheme) {
        String entrustId = scheme.getEntrustId();
        ResponseEntity<Long> responseEntity = restTemplate.getForEntity(ENTRUST_SERVICE_URL + "/api/entrust/{entrustId}/get_testerId", Long.class, entrustId);
        if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED) {
            Long testerId = responseEntity.getBody();
            /*根据调研情况，分配的测试部人员、测试部主管、所有质量部人员、质量部主管均有权限查阅*/
            return (userRole == Role.TESTER && userId.equals(testerId)) || userRole == Role.TESTING_SUPERVISOR || userRole == Role.QA || userRole == Role.QA_SUPERVISOR;
        }
        return false;
    }

    private boolean hasAuthorityToCreate(Role userRole) {
        /*根据调研情况，只有市场部人员、市场部主管具有新建测试方案权限*/
        return userRole == Role.MARKETER || userRole == Role.MARKETING_SUPERVISOR;
    }
}

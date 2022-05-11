package com.njustc.onlinebiz.apply.service;


import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.njustc.onlinebiz.apply.model.Apply;
import com.njustc.onlinebiz.apply.model.Outline;
import com.njustc.onlinebiz.apply.model.Party;
import com.njustc.onlinebiz.apply.model.Software;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MongoApplyService implements ApplyService {

    private final MongoTemplate mongoTemplate;

    public MongoApplyService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // 创建一个空的委托申请
    @Override
    public Apply createApply(Long principalId, Long auditorId) {
        Apply apply = new Apply();
        apply.setPrincipalId(principalId);
        apply.setAuditorId(auditorId);
        try {
            System.out.println("1");
            return mongoTemplate.insert(apply);
        } catch (Exception e) {
            log.warn("创建合同失败");
            return null;
        }
    }

    // 返回所有委托申请信息的概要列表
    @Override
    public List<Outline> findAllApplys() {
        Query query = new BasicQuery("{}", Outline.PROJECT_FROM_CONTRACT);
        return mongoTemplate.find(query, Outline.class, Apply.COLLECTION_NAME);
    }

    // 根据委托申请 id 查询申请的完整信息
    @Override
    public Apply findApplyById(String applyId) {
        return mongoTemplate.findById(applyId, Apply.class);
    }

    // 检查访问的用户是否是申请的委托方
    @Override
    public Apply findApplyByIdAndPrincipal(String applyId, Long userId) {
        Apply apply = findApplyById(applyId);
        if (apply != null && userId.equals(apply.getPrincipalId())) {
            return apply;
        }
        return null;
    }

    // 更新一个申请，重新设置所有可编辑的域，除了申请 id 和用户 id
    @Override
    public boolean updateApply(Apply apply) {
        // ID 不合法有可能抛出异常
        try {
            Query query = new BasicQuery("{_id:ObjectId('" + apply.getId() + "')}");
            UpdateDefinition updateDefinition = new Update()
                    .set("testType", apply.getTestType())
                    .set("principal", apply.getPrincipal())
                    .set("testedSoftware", apply.getTestedSoftware())
                    .set("testStandard", apply.getTestStandard())
                    .set("techIndex", apply.getTechIndex())
                    .set("softwareMedium", apply.getSoftwareMedium())
                    .set("document", apply.getDocument())
                    .set("sampleHandling", apply.getSampleHandling())
                    .set("expectedTime", apply.getExpectedTime())
                    .set("securityLevel", apply.getSecurityLevel())
                    .set("checkVirus", apply.getCheckVirus())
                    .set("checkMaterious", apply.getCheckMaterious())
                    .set("confirmation", apply.getConfirmation())
                    .set("acceptance", apply.getAcceptance())
                    .set("testSerialNumber", apply.getTestSerialNumber())
                    .set("auditorStage", apply.getAuditorStage());
            UpdateResult result = mongoTemplate.updateFirst(query, updateDefinition, Apply.COLLECTION_NAME);
            return result.wasAcknowledged() && result.getMatchedCount() == 1;
        } catch (Exception e) {
            return false;
        }
    }

    // 根据委托申请ID删除委托申请，返回是否成功
    @Override
    public boolean removeApply(String applyId) {
        try {
            Query query = new BasicQuery("{_id:ObjectId('" + applyId + "')}");
            DeleteResult result = mongoTemplate.remove(query, Apply.COLLECTION_NAME);
            return result.wasAcknowledged() && result.getDeletedCount() == 1;
        } catch (Exception e) {
            // in case of invalid applyId
            return false;
        }
    }

    // 根据条件查询委托申请
    @Override
    public ConditionBuilder search() {
        return new MongoConditionBuilder(mongoTemplate);
    }
}

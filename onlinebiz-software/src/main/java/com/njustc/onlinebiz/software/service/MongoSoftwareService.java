package com.njustc.onlinebiz.software.service;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.njustc.onlinebiz.common.model.SoftwareFunctionList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MongoSoftwareService implements SoftwareService {

    private final MongoTemplate mongoTemplate;

    public MongoSoftwareService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // 创建一个空的软件功能列表
    @Override
    public SoftwareFunctionList createSoftwareFunctionList(Long principalId, SoftwareFunctionList softwareFunctionList) {
        softwareFunctionList.setPrincipalId(principalId);
        // 从委托申请服务获取委托信息，并提取相关内容放入 Contract 对象
        try {
            return mongoTemplate.insert(softwareFunctionList, softwareFunctionList.COLLECTION_NAME);
        } catch (Exception e) {
            log.warn("创建软件功能列表失败");
            return null;
        }
    }

    // 根据软件功能列表 id 查询合同的完整信息
    @Override
    public SoftwareFunctionList findSoftwareFunctionListById(String softwareFunctionListId) {
        return mongoTemplate.findById(softwareFunctionListId, SoftwareFunctionList.class);
    }

    // 检查访问的用户是否是软件功能列表的委托方
    @Override
    public SoftwareFunctionList findSoftwareFunctionListByIdAndPrincipal(String softwareFunctionListId, Long userId) {
        SoftwareFunctionList software = findSoftwareFunctionListById(softwareFunctionListId);
        if (software != null && userId.equals(software.getPrincipalId())) {
            // 的确是客户自己的合同
            return software;
        }
        return null;
    }

    // 更新一个软件功能列表，重新设置所有可编辑的域，除了软件功能列表 id 和用户 id
    @Override
    public boolean updateSoftwareFunctionList(SoftwareFunctionList softwareFunctionList) {
        // ID 不合法有可能抛出异常
        try {
            Query query = new BasicQuery("{_id:ObjectId('" + softwareFunctionList.getId() + "')}");
            UpdateDefinition updateDefinition = new Update()
                    .set("softwareFunctionProject", softwareFunctionList.getSoftwareFunctionProject())
                    .set("softwareName", softwareFunctionList.getSoftwareName())
                    .set("version", softwareFunctionList.getVersion());
            UpdateResult result = mongoTemplate.updateFirst(query, updateDefinition, SoftwareFunctionList.COLLECTION_NAME);
            return result.wasAcknowledged() && result.getMatchedCount() == 1;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean removeSoftwareFunctionList(String softwareFunctionListId) {
        try {
            Query query = new BasicQuery("{_id:ObjectId('" + softwareFunctionListId + "')}");
            DeleteResult result = mongoTemplate.remove(query, SoftwareFunctionList.COLLECTION_NAME);
            return result.wasAcknowledged() && result.getDeletedCount() == 1;
        } catch (Exception e) {
            // in case of invalid contractId
            return false;
        }
    }

    @Override
    public ConditionBuilder search() {
        return new MongoConditionBuilder(mongoTemplate);
    }
}

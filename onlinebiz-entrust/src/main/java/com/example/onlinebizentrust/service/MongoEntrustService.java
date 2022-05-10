package com.example.onlinebizentrust.service;

import com.example.onlinebizentrust.model.Entrust;
import com.example.onlinebizentrust.model.Outline;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Service;

import java.util.List;

// 为了减少实现的复杂性，这里我们先不写接口。如果要把业务类的接口抽象出来，
// 那还需要一个 ConditionBuilder 的接口，然后为不同的业务类写它的实现。
@Slf4j
@Service
public class MongoEntrustService implements EntrustService {

    private final MongoTemplate mongoTemplate;

    public MongoEntrustService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // 创建一个空的委托
    @Override
    public Entrust createEntrust(Long principalId, Long creatorId, String entrustId) {
        Entrust entrust = new Entrust();
        entrust.setPrincipalId(principalId);
        entrust.setCreatorId(creatorId);
        entrust.setEntrustId(entrustId);
        try {
            return mongoTemplate.insert(entrust);
        } catch (Exception e) {
            log.warn("创建委托失败");
            return null;
        }
    }

    // 返回 MongoDB 中所有 Entrust 对象投影到 Outline 对象的结果
    @Override
    public List<Outline> findAllEntrusts() {
        Query query = new BasicQuery("{}", Outline.PROJECT_FROM_ENTRUST);
        return mongoTemplate.find(query, Outline.class, Entrust.COLLECTION_NAME);
    }

    // 根据委托 id 查询委托的完整信息
    @Override
    public Entrust findEntrustById(String entrustId) {
        return mongoTemplate.findById(entrustId, Entrust.class);
    }

    // 检查访问的用户是否是委托的委托方
    @Override
    public Entrust findEntrustByIdAndPrincipal(String entrustId, Long userId) {
        Entrust entrust = findEntrustById(entrustId);
        if (entrust != null && userId.equals(entrust.getPrincipalId())) {
            // 的确是客户自己的委托
            return entrust;
        }
        return null;
    }

    // 更新一个委托，重新设置所有可编辑的域，除了委托 id 和用户 id
    @Override
    public boolean updateEntrust(Entrust entrust) {
        // ID 不合法有可能抛出异常
        try {
            Query query = new BasicQuery("{_id:ObjectId('" + entrust.getId() + "')}");
            UpdateDefinition updateDefinition = new Update()
                    .set("serialNumber", entrust.getSerialNumber())
                    .set("projectName", entrust.getProjectName())
                    .set("principal", entrust.getPrincipal())
                    .set("principalRepresentative", entrust.getPrincipalRepresentative())
                    .set("trusteeRepresentative", entrust.getTrusteeRepresentative())
                    .set("signedDate", entrust.getSignedDate());
            UpdateResult result = mongoTemplate.updateFirst(query, updateDefinition, Entrust.COLLECTION_NAME);
            return result.wasAcknowledged() && result.getMatchedCount() == 1;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean removeEntrust(String entrustId) {
        try {
            Query query = new BasicQuery("{_id:ObjectId('" + entrustId + "')}");
            DeleteResult result = mongoTemplate.remove(query, Entrust.COLLECTION_NAME);
            return result.wasAcknowledged() && result.getDeletedCount() == 1;
        } catch (Exception e) {
            // in case of invalid entrustId
            return false;
        }
    }

    @Override
    public ConditionBuilder search() {
        return new MongoConditionBuilder(mongoTemplate);
    }
}

package com.njustc.onlinebiz.entrust.dao;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.njustc.onlinebiz.common.model.entrust.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 使用MongoDB的数据访问层实现。这里假设服务层已经校验了参数的合法性。
 */
@Component
public class MongoEntrustDAO implements EntrustDAO {

    public static final String COLLECTION_NAME = "entrust";

    private final MongoTemplate mongoTemplate;

    public MongoEntrustDAO(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Entrust insertEntrust(Entrust entrust) {
        return mongoTemplate.insert(entrust, COLLECTION_NAME);
    }

    @Override
    public Entrust findEntrustById(String entrustId) {
        return mongoTemplate.findById(entrustId, Entrust.class);
    }

    @Override
    public List<EntrustOutline> findAllEntrusts(Integer page, Integer pageSize) {
        Query query = new Query();
        return findWithProjection(query, page, pageSize);
    }

    @Override
    public long countAll() {
        return mongoTemplate.count(new Query(), COLLECTION_NAME);
    }

    @Override
    public List<EntrustOutline> findEntrustsByCustomerId(Long customerId, Integer page, Integer pageSize) {
        Query query = new Query().addCriteria(Criteria.where("customerId").is(customerId));
        return findWithProjection(query, page, pageSize);
    }

    @Override
    public long countByCustomerId(Long customerId) {
        Query query = new Query().addCriteria(Criteria.where("customerId").is(customerId));
        return mongoTemplate.count(query, COLLECTION_NAME);
    }

    @Override
    public List<EntrustOutline> findEntrustsByMarketerId(Long marketerId, Integer page, Integer pageSize) {
        Query query = new Query().addCriteria(Criteria.where("marketerId").is(marketerId));
        return findWithProjection(query, page, pageSize);
    }

    @Override
    public long countByMarketerId(Long marketerId) {
        Query query = new Query().addCriteria(Criteria.where("marketerId").is(marketerId));
        return mongoTemplate.count(query, COLLECTION_NAME);
    }

    @Override
    public List<EntrustOutline> findEntrustsByTesterId(Long testerId, Integer page, Integer pageSize) {
        Query query = new Query().addCriteria(Criteria.where("testerId").is(testerId));
        return findWithProjection(query, page, pageSize);
    }

    @Override
    public long countByTesterId(Long testerId) {
        Query query = new Query().addCriteria(Criteria.where("testerId").is(testerId));
        return mongoTemplate.count(query, COLLECTION_NAME);
    }

    @Override
    public Boolean updateCustomerId(String entrustId, Long customerId) {
        Update update = new Update().set("customerId", customerId);
        return updateFirstWithId(entrustId, update);
    }

    @Override
    public Boolean updateMarketerId(String entrustId, Long marketerId) {
        Update update = new Update().set("marketerId", marketerId);
        return updateFirstWithId(entrustId, update);
    }

    @Override
    public Boolean updateTesterId(String entrustId, Long testerId) {
        Update update = new Update().set("testerId", testerId);
        return updateFirstWithId(entrustId, update);
    }

    @Override
    public Boolean updateContractId(String entrustId, String contractId) {
        Update update = new Update().set("contractId", contractId);
        return updateFirstWithId(entrustId, update);
    }

    @Override
    public Boolean updateProjectId(String entrustId, String projectId) {
        Update update = new Update().set("projectId", projectId);
        return updateFirstWithId(entrustId, update);
    }

    @Override
    public Boolean updateContent(String entrustId, EntrustContent content) {
        Update update = new Update().set("content", content);
        return updateFirstWithId(entrustId, update);
    }

    @Override
    public Boolean updateSoftwareDocReview(String entrustId, SoftwareDocReview softwareDocReview) {
        Update update = new Update().set("softwareDocReview", softwareDocReview);
        return updateFirstWithId(entrustId, update);
    }

    @Override
    public Boolean updateReview(String entrustId, EntrustReview review) {
        Update update = new Update().set("review", review);
        return updateFirstWithId(entrustId, update);
    }

    @Override
    public Boolean updateQuote(String entrustId, EntrustQuote quote) {
        Update update = new Update().set("quote", quote);
        return updateFirstWithId(entrustId, update);
    }

    @Override
    public Boolean updateStatus(String entrustId, EntrustStatus status) {
        Update update = new Update().set("status", status);
        return updateFirstWithId(entrustId, update);
    }

    @Override
    public Boolean deleteEntrust(String entrustId) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(entrustId)));
        DeleteResult result = mongoTemplate.remove(query, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getDeletedCount() == 1;
    }

    private List<EntrustOutline> findWithProjection(Query query, Integer page, Integer pageSize) {
        // 设置要取回的字段
        query.fields().include("_id");
        query.fields().include("customerId");
        query.fields().include("marketerId");
        query.fields().include("testerId");
        query.fields().include("content.software.name");
        query.fields().include("content.software.version");
        query.fields().include("status");
        List<Entrust> result = findWithPagination(query, page, pageSize);
        // 转换为委托概要
        return result.stream().map(EntrustOutline::new).collect(Collectors.toList());
    }

    // 根据查询条件进行分页查询
    private List<Entrust> findWithPagination(Query query, Integer page, Integer pageSize) {
        // 计算起始位置，页码从1开始
        int offset = (page - 1) * pageSize;
        // 先不优化分页查询
        query.skip(offset).limit(pageSize);
        return mongoTemplate.find(query, Entrust.class, COLLECTION_NAME);
    }

    // 根据委托ID更新委托，由传入的 Update 对象设置要更新的部分
    private Boolean updateFirstWithId(String entrustId, Update update) {
        Query query = new Query().addCriteria(Criteria.where("_id").is(new ObjectId(entrustId)));
        UpdateResult result = mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
        return result.wasAcknowledged() && result.getMatchedCount() == 1;
    }

}

package com.njustc.onlinebiz.test.dao.scheme;

import com.njustc.onlinebiz.test.model.Scheme;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoSchemeDAO implements SchemeDAO {
    public static final String COLLECTION_NAME = "testScheme";

    private final MongoTemplate mongoTemplate;

    public MongoSchemeDAO(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Scheme findSchemeById(String schemeId) {
        return mongoTemplate.findById(schemeId, Scheme.class);
    }

    @Override
    public Scheme insertScheme(Scheme scheme) {
        return mongoTemplate.insert(scheme, COLLECTION_NAME);
    }
}

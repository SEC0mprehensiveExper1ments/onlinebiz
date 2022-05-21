package com.njustc.onlinebiz.test.dao.scheme;

import com.njustc.onlinebiz.test.model.Scheme;

public interface SchemeDAO {
    Scheme insertScheme(Scheme scheme);

    Scheme findSchemeById(String schemeId);
}

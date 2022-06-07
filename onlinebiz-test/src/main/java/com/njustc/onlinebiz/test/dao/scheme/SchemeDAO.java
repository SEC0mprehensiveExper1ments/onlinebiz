package com.njustc.onlinebiz.test.dao.scheme;

import com.njustc.onlinebiz.common.model.test.scheme.Scheme;
import com.njustc.onlinebiz.common.model.test.scheme.SchemeContent;

public interface SchemeDAO {
    Scheme insertScheme(Scheme scheme);

    Scheme findSchemeById(String schemeId);

    boolean updateContent(String schemeId, SchemeContent content);

    boolean deleteScheme(String schemeId);
}

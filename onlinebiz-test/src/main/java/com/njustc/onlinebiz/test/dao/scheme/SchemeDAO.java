package com.njustc.onlinebiz.test.dao.scheme;

import com.njustc.onlinebiz.test.model.scheme.Scheme;
import com.njustc.onlinebiz.test.model.scheme.SchemeContent;
import com.njustc.onlinebiz.test.model.scheme.SchemeStatus;

public interface SchemeDAO {
    Scheme insertScheme(Scheme scheme);

    Scheme findSchemeById(String schemeId);

    boolean updateContent(String schemeId, SchemeContent content);

    boolean updateStatus(String schemeId, SchemeStatus status);

    boolean deleteScheme(String schemeId);
}

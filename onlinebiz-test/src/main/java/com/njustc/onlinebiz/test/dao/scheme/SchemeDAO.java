package com.njustc.onlinebiz.test.dao.scheme;

import com.njustc.onlinebiz.test.model.Scheme;
import com.njustc.onlinebiz.test.model.SchemeContent;
import com.njustc.onlinebiz.test.model.SchemeStatus;

public interface SchemeDAO {
    Scheme insertScheme(Scheme scheme);

    Scheme findSchemeById(String schemeId);

    boolean updateContent(String schemeId, SchemeContent content);

    boolean updateStatus(String schemeId, SchemeStatus status);

    boolean deleteScheme(String schemeId);
}

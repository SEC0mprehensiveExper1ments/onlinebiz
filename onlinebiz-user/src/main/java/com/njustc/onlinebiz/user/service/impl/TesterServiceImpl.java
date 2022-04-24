package com.njustc.onlinebiz.user.service.impl;

import com.njustc.onlinebiz.user.mapper.TesterMapper;
import com.njustc.onlinebiz.user.pojo.Tester;
import com.njustc.onlinebiz.user.service.TesterService;
import org.springframework.beans.factory.annotation.Autowired;

public class TesterServiceImpl implements TesterService {
    @Autowired
    TesterMapper testerMapper;

    @Override
    public Tester login(String testerName, String testerPassword) {
        return testerMapper.queryTesterByTesterNameAndTesterPassword(testerName, testerPassword);
    }
}

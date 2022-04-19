package com.njustc.onlinebiz.user.service.impl;

import com.njustc.onlinebiz.user.mapper.AdministratorMapper;
import com.njustc.onlinebiz.user.pojo.Administrator;
import com.njustc.onlinebiz.user.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;

public class AdministratorServiceImpl implements AdministratorService {
    @Autowired
    AdministratorMapper administratorMapper;

    @Override
    public Administrator login(String administratorName, String administratorPassword) {
        return administratorMapper.queryAdministratorByAdministratorNameAndAdministratorPassword(administratorName, administratorPassword);
    }
}

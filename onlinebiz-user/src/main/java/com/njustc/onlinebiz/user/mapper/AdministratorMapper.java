package com.njustc.onlinebiz.user.mapper;

import com.njustc.onlinebiz.user.pojo.Administrator;
import org.apache.ibatis.annotations.Select;

public interface AdministratorMapper {
    /**
     * 通过后台管理人员用户名与用户密码查询后台管理人员
     *
     * @param administratorName 待登录后台管理人员的用户名
     * @param administratorPassword 待登录后台管理人员的用户密码
     * @return 若查询成功，则返回Administrator对象；若查询失败，则返回null
     */
    @Select("select * from administrator where administrator.administratorName=#{administratorName} and administrator.administratorPassword=#{administratorPassword}")
    Administrator queryAdministratorByAdministratorNameAndAdministratorPassword(String administratorName, String administratorPassword);
}

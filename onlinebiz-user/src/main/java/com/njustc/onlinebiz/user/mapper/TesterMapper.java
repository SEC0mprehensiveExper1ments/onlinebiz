package com.njustc.onlinebiz.user.mapper;

import com.njustc.onlinebiz.user.pojo.Tester;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TesterMapper {
    /**
     * 通过测试人员用户名与用户密码查询测试人员
     *
     * @param testerName 待登录测试人员的用户名
     * @param testerPassword 待登录测试人员的用户密码
     * @return 若查询成功，则返回Tester对象；若查询失败，则返回null
     */
    @Select("select * from tester where tester.testerName=#{testerName} and tester.testerPassword=#{testerPassword}")
    Tester queryTesterByTesterNameAndTesterPassword(String testerName, String testerPassword);
}

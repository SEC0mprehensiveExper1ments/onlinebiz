package com.njustc.onlinebiz.entrust.dao;

import com.njustc.onlinebiz.common.model.entrust.*;
import java.util.List;

/**
 * 委托管理的数据访问层接口
 */
public interface EntrustDAO {

    /**
     * 保存一份新的委托到数据库，根据具体实现不同，可能会设置对象的某些字段。
     * @param entrust 要保存的委托对象
     * @return 返回保存后的委托对象
     */
    Entrust insertEntrust(Entrust entrust);

    /**
     * 根据委托ID查询委托的详细信息
     * @param entrustId 要查询的委托ID
     * @return 如果存在则返回查找到的委托对象，不存在返回 null
     */
    Entrust findEntrustById(String entrustId);

    /**
     * 查询所有委托的概要列表，允许设置分页页码和每页记录条数，页码从1开始
     * @param page 要查询的页码
     * @param pageSize 每页有多少条记录
     * @return 返回该页上的委托概要列表
     */
    List<EntrustOutline> findAllEntrusts(Integer page, Integer pageSize);

    /**
     * 获取总委托数，主要配合分页查询使用
     * @return 总的委托数目
     */
    long countAll();

    /**
     * 根据客户ID查询概要列表，允许设置分页页码和每页记录条数，页码从1开始
     * @param customerId 要查询的客户ID
     * @param page 要查询的页码
     * @param pageSize 每页有多少条记录
     */
    List<EntrustOutline> findEntrustsByCustomerId(Long customerId, Integer page, Integer pageSize);

    /**
     * 获取某个客户的总委托数目
     * @param customerId 客户ID
     * @return 该客户的委托数
     */
    long countByCustomerId(Long customerId);

    /**
     * 根据市场部人员ID查询概要列表，允许设置分页页码和每页记录条数，页码从1开始
     * @param marketingId 要查询的市场部人员ID
     * @param page 要查询的页码
     * @param pageSize 每页有多少条记录
     * @return 返回该页上的委托概要列表
     */
    List<EntrustOutline> findEntrustsByMarketerId(Long marketingId, Integer page, Integer pageSize);

    /**
     * 获取某个市场部人员的总委托数目
     * @param marketerId 市场部人员ID
     * @return 该市场部人员的委托数
     */
    long countByMarketerId(Long marketerId);

    /**
     * 根据测试部人员ID查询概要列表，允许设置分页页码和每页记录条数，页码从1开始
     * @param testerId 要查询的测试部人员ID
     * @param page 要查询的页码
     * @param pageSize 每页有多少条记录
     * @return 返回该页上的委托概要列表
     */
    List<EntrustOutline> findEntrustsByTesterId(Long testerId, Integer page, Integer pageSize);

    /**
     * 获取某个测试人员的总委托数
     * @param testerId 测试人员ID
     * @return 该测试人员的委托数目
     */
    long countByTesterId(Long testerId);

    /**
     * 更新委托的客户ID
     * @param entrustId 要更新的委托ID
     * @param customerId 更改后的客户ID
     * @return 成功返回 true，失败返回 false
     */
    Boolean updateCustomerId(String entrustId, Long customerId);

    /**
     * 更新委托的市场部人员ID
     * @param entrustId 要更新的委托ID
     * @param marketerId 更改后的市场部人员ID
     * @return 成功返回 true，失败返回 false
     */
    Boolean updateMarketerId(String entrustId, Long marketerId);

    /**
     * 更新委托的测试部人员ID
     * @param entrustId 要更新的委托ID
     * @param testerId 更改后的测试部人员ID
     * @return 成功返回 true，失败返回 false
     */
    Boolean updateTesterId(String entrustId, Long testerId);

    /**
     * 更新委托对应的合同ID
     * @param entrustId 委托ID
     * @param contractId 对应的合同ID
     * @return 成功返回 true，失败返回 false
     */
    Boolean updateContractId(String entrustId, String contractId);

    /**
     * 更新委托对应的测试项目ID
     * @param entrustId 委托ID
     * @param projectId 对应的测试项目ID
     * @return 成功返回 true，失败返回 false
     */
    Boolean updateProjectId(String entrustId, String projectId);

    /**
     * 更新委托申请
     * @param entrustId 委托ID
     * @param content 更新后的申请内容
     * @return 成功返回 true，失败返回 false
     */
    Boolean updateContent(String entrustId, EntrustContent content);

    /**
     * 更新软件文档评审信息。
     * @param entrustId 委托ID
     * @param softwareDocReview 软件文档评审对象
     * @return 成功返回 true，失败返回 false
     */
    Boolean updateSoftwareDocReview(String entrustId, SoftwareDocReview softwareDocReview);

    /**
     * 更新委托评审结果
     * @param entrustId 委托ID
     * @param review 更新后的评审结果
     * @return 成功返回 true，失败返回 false
     */
    Boolean updateReview(String entrustId, EntrustReview review);

    /**
     * 更新委托的报价
     * @param entrustId 委托ID
     * @param quote 更新后的报价
     * @return 成功返回 true，失败返回 false
     */
    Boolean updateQuote(String entrustId, EntrustQuote quote);

    /**
     * 修改委托的状态
     * @param entrustId 委托ID
     * @param status 修改后的状态
     * @return 成功返回 true，失败返回 false
     */
    Boolean updateStatus(String entrustId, EntrustStatus status);

    /**
     * 删除指定的委托
     * @param entrustId 要删除的委托ID
     * @return 成功返回 true，失败返回 false
     */
    Boolean deleteEntrust(String entrustId);

}

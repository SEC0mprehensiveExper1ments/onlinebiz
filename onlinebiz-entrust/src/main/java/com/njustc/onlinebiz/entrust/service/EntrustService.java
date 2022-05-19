package com.njustc.onlinebiz.entrust.service;

import com.njustc.onlinebiz.common.model.*;
import com.njustc.onlinebiz.entrust.model.*;

import java.util.List;

/**
 * 委托管理的服务层接口
 */
public interface EntrustService {

    /**
     * 提交一份委托申请。只有客户和管理员可以执行此操作，此操作会设置委托的创建者ID，
     * 委托内容以及委托的初始状态。默认的初始状态是等待分配市场部人员。
     * @param content 委托申请的内容
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * @return 成功返回委托ID，失败返回 null
     */
    String createEntrust(EntrustContent content, Long userId, Role userRole);

    /**
     * 查看一份委托的完整信息。只有与此委托相关的客户、测试部、市场部主管及人员和管理员
     * 可以执行此操作。
     * @param entrustId 要查看的委托ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * @return 如果存在返回该委托对象，否则返回 null
     */
    Entrust findEntrust(String entrustId, Long userId, Role userRole);

    /**
     * 查看与某个用户相关的所有委托列表。客户、市场部人员和测试部人员都只能看到他们
     * 自己的委托；管理员和主管看到的是所有委托。
     * @param page 查询结果的页码
     * @param pageSize 每页有多少条记录
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     * @return 返回该用户可以看到的委托列表
     */
    List<EntrustOutline> findEntrustOutlines(Integer page, Integer pageSize, Long userId, Role userRole);

    /**
     * 修改委托申请的内容。该委托所属的客户和管理员可以执行此操作。其中客户只能在委托被拒绝
     * 之后才能修改；管理员任何时候都可以修改。
     * @param entrustId 要修改的委托ID
     * @param content 修改后的委托内容
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void updateContent(String entrustId, EntrustContent content, Long userId, Role userRole);

    /**
     * 更新委托对应的市场部人员，只有管理员和市场部主管可以执行此操作。其中市场部主管只能在
     * 指定阶段执行此操作，管理员任何时候都可以执行。
     * @param entrustId 要更新的委托ID
     * @param marketingId 对应的市场部人员ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void updateMarketer(String entrustId, Long marketingId, Long userId, Role userRole);

    /**
     * 更新委托对应的测试人员，只有管理员和测试部主管可以执行此操作。其中测试部主管只能在
     * 指定阶段执行此操作，管理员任何时候都可以执行。
     * @param entrustId 要更新的委托ID
     * @param testerId 对应的测试人员ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void updateTester(String entrustId, Long testerId, Long userId, Role userRole);

    /**
     * 设置委托状态为审核不通过，只有市场部人员、测试部人员和管理员可以执行此操作。其中，
     * 市场部人员只可以在市场部审核阶段执行此操作；测试部人员只可以在测试部审核阶段执行
     * 此操作；管理员可以在任何时候执行此操作。
     * @param entrustId 要设置的委托ID
     * @param message 不通过的原因
     * @param userId 设置此状态的用户ID，必须符合权限要求
     * @param userRole 执行此操作的用户角色
     */
    void denyContent(String entrustId, String message, Long userId, Role userRole);

    /**
     * 设置委托状态为审核通过，只有市场部人员、测试部人员和管理员可以执行此操作。其中，
     * 市场部人员只可以在市场部审核阶段执行此操作；测试部人员只可以在测试部审核阶段执行
     * 此操作；管理员可以在任何时候执行此操作。
     * @param entrustId 要设置的委托ID
     * @param userId 设置此状态的用户ID，必须符合委托当前阶段
     * @param userRole 执行此操作的用户角色
     */
    void approveContent(String entrustId, Long userId, Role userRole);

    /**
     * 更新委托申请的评审信息。只有管理员和与该委托相关的测试部、市场部人员可以执行此操作。
     * 其中除了管理员外只能在各自的评审阶段才可以执行此操作。
     * @param entrustId 要更新的委托ID
     * @param review 更新后的评审内容
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void updateReview(String entrustId, EntrustReview review, Long userId, Role userRole);

    /**
     * 更新委托的报价。只有市场部人员和管理员可以执行此操作。其中对于市场部人员，只有分配
     * 给此委托的市场部人员可以执行此操作，并且需要符合当前委托阶段。
     * @param entrustId 要更新的委托ID
     * @param quote 更新后的报价
     * @param userId 更新此报价的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void updateQuote(String entrustId, EntrustQuote quote, Long userId, Role userRole);

    /**
     * 拒绝委托的报价。只有客户和管理员可以执行此操作。其中对于客户身份，只有此委托所属的
     * 客户可以拒绝此报价，并且需要符合当前委托所处的阶段。
     * @param entrustId 报价所属的委托ID
     * @param message 拒绝的原因
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void denyQuote(String entrustId, String message, Long userId, Role userRole);

    /**
     * 接受委托的报价。只有客户和管理员可以执行此操作。其中对于客户身份，只有此委托所属的
     * 客户可以接受此报价，并且需要符合当前委托所处的阶段。
     * @param entrustId 报价所属的委托ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void approveQuote(String entrustId, Long userId, Role userRole);

    /**
     * 更改委托所属的客户，只有管理员可以进行此操作。
     * @param entrustId 要更改的委托ID
     * @param customerId 更改后的客户ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void updateCustomer(String entrustId, Long customerId, Long userId, Role userRole);

    /**
     * 更改委托的状态，包括设置委托当前所处阶段和附加的说明信息。只有管理员可以执行此操作。
     * @param entrustId 要更改的委托ID
     * @param status 更改后的新状态
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void updateStatus(String entrustId, EntrustStatus status, Long userId, Role userRole);

    /**
     * 删除一份委托，只有管理员可以执行此操作。这个操作会删除该委托下的所有子条目，包括
     * 委托对应的评审数据、合同信息等等。
     * @param entrustId 要删除的委托ID
     * @param userId 执行此操作的用户ID
     * @param userRole 执行此操作的用户角色
     */
    void removeEntrust(String entrustId, Long userId, Role userRole);

}
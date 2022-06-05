package com.njustc.onlinebiz.entrust.service;

import com.njustc.onlinebiz.common.model.*;
import com.njustc.onlinebiz.common.model.entrust.*;
import com.njustc.onlinebiz.entrust.dao.EntrustDAO;
import com.njustc.onlinebiz.entrust.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 委托管理的默认服务层实现类
 */
@Slf4j
@Service
public class DefaultEntrustService implements EntrustService {

    private final EntrustDAO entrustDAO;

    public DefaultEntrustService(EntrustDAO entrustDAO) {
        this.entrustDAO = entrustDAO;
    }

    @Override
    public String createEntrust(EntrustContent content, Long userId, Role userRole) {
        if (userRole != Role.ADMIN && userRole != Role.CUSTOMER) {
            throw new EntrustPermissionDeniedException("无权提交委托申请");
        }
        Entrust entrust = new Entrust();
        entrust.setCustomerId(userId);
        entrust.setContent(content);
        entrust.setStatus(new EntrustStatus(EntrustStage.WAIT_FOR_MARKETER, "等待市场部主管分配市场部人员"));
        return entrustDAO.insertEntrust(entrust).getId();
    }

    @Override
    public Entrust findEntrust(String entrustId, Long userId, Role userRole) {
        // 对应委托必须存在
        Entrust entrust = entrustDAO.findEntrustById(entrustId);
        if (entrust == null) {
            throw new EntrustNotFoundException("该委托不存在");
        }
        // 检查是否有权限查看
        if (hasFindAuthority(entrust, userId, userRole)) {
            return entrust;
        }
        throw new EntrustPermissionDeniedException("无权查看该委托");
    }

    private Boolean hasFindAuthority(Entrust entrust, Long userId, Role userRole) {
        if (userRole == Role.ADMIN || userRole == Role.MARKETING_SUPERVISOR || userRole == Role.TESTING_SUPERVISOR) {
            return true;
        } else if (userRole == Role.CUSTOMER) {
            return userId.equals(entrust.getCustomerId());
        } else if (userRole == Role.MARKETER) {
            return userId.equals(entrust.getMarketerId());
        } else if (userRole == Role.TESTER) {
            return userId.equals(entrust.getTesterId());
        }
        return false;
    }

    @Override
    public PageResult<EntrustOutline> findEntrustOutlines(Integer page, Integer pageSize, Long userId, Role userRole) {
        if (page <= 0 || pageSize <= 0) {
            throw new EntrustInvalidArgumentException("页号或每页大小必须为正整数");
        }
        long total;
        List<EntrustOutline> list;
        // 根据用户角色不同，返回不同的结果
        if (userRole == Role.ADMIN || userRole == Role.MARKETING_SUPERVISOR || userRole == Role.TESTING_SUPERVISOR) {
            total = entrustDAO.countAll();
            list = entrustDAO.findAllEntrusts(page, pageSize);
        } else if (userRole == Role.CUSTOMER) {
            total = entrustDAO.countByCustomerId(userId);
            list = entrustDAO.findEntrustsByCustomerId(userId, page, pageSize);
        } else if (userRole == Role.MARKETER) {
            total = entrustDAO.countByMarketerId(userId);
            list = entrustDAO.findEntrustsByMarketerId(userId, page, pageSize);
        } else if (userRole == Role.TESTER) {
            total = entrustDAO.countByTesterId(userId);
            list = entrustDAO.findEntrustsByTesterId(userId, page, pageSize);
        } else {
            throw new EntrustPermissionDeniedException("无权查看委托列表");
        }
        return new PageResult<>(page, pageSize, total, list);
    }

    @Override
    public void updateContent(String entrustId, EntrustContent content, Long userId, Role userRole) {
        Entrust entrust = findEntrust(entrustId, userId, userRole);
        // 检查委托阶段
        EntrustStage currStage = entrust.getStatus().getStage();
        EntrustStage nextStage;
        if (currStage == EntrustStage.MARKETER_DENIED) {
            nextStage = EntrustStage.MARKETER_AUDITING;
        } else if (currStage == EntrustStage.TESTER_DENIED) {
            nextStage = EntrustStage.TESTER_AUDITING;
        } else {
            throw new EntrustInvalidStageException("此阶段不能修改委托申请");
        }
        // 检查用户权限
        if (!hasUpdateAuthority(entrust, userId, userRole)) {
            throw new EntrustPermissionDeniedException("无权修改该委托申请");
        }
        // 检查数据库操作是否成功
        if (!entrustDAO.updateContent(entrustId, content) || !entrustDAO.updateStatus(entrustId, new EntrustStatus(nextStage, null))) {
            throw new EntrustDAOFailureException("更新委托申请失败");
        }
    }

    @Override
    public void updateMarketer(String entrustId, Long marketerId, Long userId, Role userRole) {
        Entrust entrust = findEntrust(entrustId, userId, userRole);
        // 检查委托阶段
        EntrustStage currStage = entrust.getStatus().getStage();
        EntrustStage nextStage = EntrustStage.MARKETER_AUDITING;
        if (currStage != EntrustStage.WAIT_FOR_MARKETER) {
            throw new EntrustInvalidStageException(("此阶段不能分配市场部人员"));
        }
        // 检查用户权限
        if (!hasUpdateAuthority(entrust, userId, userRole)) {
            throw new EntrustPermissionDeniedException("无权分配市场部人员");
        }
        // 检查数据库操作是否成功
        if (!entrustDAO.updateMarketerId(entrustId, marketerId) || !entrustDAO.updateStatus(entrustId, new EntrustStatus(nextStage, null))) {
            throw new EntrustDAOFailureException("更新市场部人员失败");
        }
    }

    @Override
    public void updateTester(String entrustId, Long testerId, Long userId, Role userRole) {
        Entrust entrust = findEntrust(entrustId, userId, userRole);
        // 检查委托阶段
        EntrustStage currStage = entrust.getStatus().getStage();
        EntrustStage nextStage = EntrustStage.TESTER_AUDITING;
        if (currStage != EntrustStage.WAIT_FOR_TESTER) {
            throw new EntrustInvalidStageException(("此阶段不能分配测试部人员"));
        }
        // 检查用户权限
        if (!hasUpdateAuthority(entrust, userId, userRole)) {
            throw new EntrustPermissionDeniedException("无权分配测试部人员");
        }
        // 检查数据库操作是否成功
        if (!entrustDAO.updateTesterId(entrustId, testerId) || !entrustDAO.updateStatus(entrustId, new EntrustStatus(nextStage, null))) {
            throw new EntrustDAOFailureException("更新测试部人员失败");
        }
    }

    @Override
    public void denyContent(String entrustId, String message, Long userId, Role userRole) {
        Entrust entrust = findEntrust(entrustId, userId, userRole);
        // 检查委托阶段
        EntrustStage currStage = entrust.getStatus().getStage();
        EntrustStage nextStage;
        if (currStage == EntrustStage.MARKETER_AUDITING) {
            nextStage = EntrustStage.MARKETER_DENIED;
        } else if (currStage == EntrustStage.TESTER_AUDITING) {
            nextStage = EntrustStage.TESTER_DENIED;
        } else {
            throw new EntrustInvalidStageException("此阶段不能设置委托申请审核结果");
        }
        // 检查用户权限
        if (!hasUpdateAuthority(entrust, userId, userRole)) {
            throw new EntrustPermissionDeniedException("当前阶段无权审核委托申请");
        }
        // 检查数据库操作是否成功
        if (!entrustDAO.updateStatus(entrustId, new EntrustStatus(nextStage, message))) {
            throw new EntrustDAOFailureException("拒绝申请失败");
        }
    }

    @Override
    public void approveContent(String entrustId, Long userId, Role userRole) {
        Entrust entrust = findEntrust(entrustId, userId, userRole);
        // 检查委托阶段
        EntrustStage currStage = entrust.getStatus().getStage();
        EntrustStage nextStage;
        if (currStage == EntrustStage.MARKETER_AUDITING) {
            nextStage = EntrustStage.WAIT_FOR_TESTER;
        } else if (currStage == EntrustStage.TESTER_AUDITING) {
            nextStage = EntrustStage.AUDITING_PASSED;
        } else {
            throw new EntrustInvalidStageException("此阶段不能设置委托申请审核结果");
        }
        // 检查用户权限
        if (!hasUpdateAuthority(entrust, userId, userRole)) {
            throw new EntrustPermissionDeniedException("当前阶段无权审核委托申请");
        }
        if (!entrustDAO.updateStatus(entrustId, new EntrustStatus(nextStage, null))) {
            throw new EntrustDAOFailureException("同意申请失败");
        }
    }

    @Override
    public void updateSoftwareDocReview(String entrustId, SoftwareDocReview softwareDocReview, Long userId, Role userRole) {
        Entrust entrust = findEntrust(entrustId, userId, userRole);
        if (entrust.getStatus().getStage() != EntrustStage.TESTER_AUDITING) {
            throw new EntrustInvalidStageException("此阶段不能修改软件文档评审信息");
        }
        if (userRole != Role.ADMIN && (userRole != Role.TESTER || !userId.equals(entrust.getTesterId()))) {
            throw new EntrustPermissionDeniedException("无权修改此软件文档评审信息");
        }
        if (entrustDAO.updateSoftwareDocReview(entrustId, softwareDocReview)) {
            return;
        }
        throw new EntrustDAOFailureException("更新软件文档评审数据失败");
    }

    @Override
    public void updateReview(String entrustId, EntrustReview review, Long userId, Role userRole) {
        Entrust entrust = findEntrust(entrustId, userId, userRole);
        boolean hasAuth = false;
        if (userRole == Role.ADMIN) {
            hasAuth = true;
        } else if (userRole == Role.MARKETER) {
            hasAuth = userId.equals(entrust.getMarketerId());
        } else if (userRole == Role.TESTER) {
            hasAuth = userId.equals(entrust.getTesterId());
        }
        if (!hasAuth) {
            throw new EntrustPermissionDeniedException("无权修改委托审核结果");
        }
        if (!entrustDAO.updateReview(entrustId, review)) {
            throw new EntrustDAOFailureException("更新评审结果失败");
        }
    }

    @Override
    public void updateQuote(String entrustId, EntrustQuote quote, Long userId, Role userRole) {
        Entrust entrust = findEntrust(entrustId, userId, userRole);
        EntrustStage currStage = entrust.getStatus().getStage();
        EntrustStage nextStage = EntrustStage.CUSTOMER_CHECK_QUOTE;
        // 检查委托阶段
        if (currStage != EntrustStage.AUDITING_PASSED && currStage != EntrustStage.CUSTOMER_DENY_QUOTE) {
            throw new EntrustInvalidStageException("此阶段不能修改报价");
        }
        // 检查用户权限
        if (!hasUpdateAuthority(entrust, userId, userRole)) {
            throw new EntrustPermissionDeniedException("无权修改委托报价");
        }
        if (!entrustDAO.updateQuote(entrustId, quote) || !entrustDAO.updateStatus(entrustId, new EntrustStatus(nextStage, null))) {
            throw new EntrustDAOFailureException("更新报价失败");
        }
    }

    @Override
    public void denyQuote(String entrustId, String message, Long userId, Role userRole) {
        Entrust entrust = findEntrust(entrustId, userId, userRole);
        EntrustStage currStage = entrust.getStatus().getStage();
        EntrustStage nextStage = EntrustStage.CUSTOMER_DENY_QUOTE;
        // 检查委托阶段
        if (currStage != EntrustStage.CUSTOMER_CHECK_QUOTE) {
            throw new EntrustInvalidStageException("此阶段不能拒绝报价");
        }
        // 检查用户权限
        if (!hasUpdateAuthority(entrust, userId, userRole)) {
            throw new EntrustPermissionDeniedException("无权操作该报价");
        }
        if (!entrustDAO.updateStatus(entrustId, new EntrustStatus(nextStage, message))) {
            throw new EntrustDAOFailureException("拒绝报价失败");
        }
    }

    @Override
    public void approveQuote(String entrustId, Long userId, Role userRole) {
        Entrust entrust = findEntrust(entrustId, userId, userRole);
        EntrustStage currStage = entrust.getStatus().getStage();
        EntrustStage nextStage = EntrustStage.CUSTOMER_ACCEPT_QUOTE;
        // 检查委托阶段
        if (currStage != EntrustStage.CUSTOMER_CHECK_QUOTE) {
            throw new EntrustInvalidStageException("此阶段不能接受报价");
        }
        // 检查用户权限
        if (!hasUpdateAuthority(entrust, userId, userRole)) {
            throw new EntrustPermissionDeniedException("无权操作该报价");
        }
        if (!entrustDAO.updateStatus(entrustId, new EntrustStatus(nextStage, null))) {
            throw new EntrustDAOFailureException("同意报价失败");
        }
    }

    @Override
    public void terminateEntrust(String entrustId, Long userId, Role userRole) {
        Entrust entrust = findEntrust(entrustId, userId, userRole);
        EntrustStage currStage = entrust.getStatus().getStage();
        EntrustStage nextStage = EntrustStage.TERMINATED;
        // 检查阶段
        if (currStage != EntrustStage.CUSTOMER_CHECK_QUOTE) {
            throw new EntrustInvalidStageException("此阶段无法终止委托");
        }
        // 检查用户权限
        if (!hasUpdateAuthority(entrust, userId, userRole)) {
            throw new EntrustPermissionDeniedException("无权终止该委托");
        }
        if (!entrustDAO.updateStatus(entrustId, new EntrustStatus(nextStage, null))) {
            throw new EntrustDAOFailureException("终止委托失败");
        }
    }

    // 检查用户是否有权限修改委托相关的内容
    private Boolean hasUpdateAuthority(Entrust entrust, Long userId, Role userRole) {
        EntrustStage currStage = entrust.getStatus().getStage();
        if (userRole == Role.ADMIN) {
            return true;
        } else if (userRole == Role.MARKETING_SUPERVISOR) {
            return currStage == EntrustStage.WAIT_FOR_MARKETER;
        } else if (userRole == Role.TESTING_SUPERVISOR) {
            return currStage == EntrustStage.WAIT_FOR_TESTER;
        } else if (userRole == Role.CUSTOMER) {
            if (currStage == EntrustStage.TESTER_DENIED || currStage == EntrustStage.MARKETER_DENIED
                    || currStage == EntrustStage.CUSTOMER_CHECK_QUOTE) {
                return userId.equals(entrust.getCustomerId());
            }
        } else if (userRole == Role.MARKETER) {
            if (currStage == EntrustStage.MARKETER_AUDITING || currStage == EntrustStage.AUDITING_PASSED
                    || currStage == EntrustStage.CUSTOMER_DENY_QUOTE) {
                return userId.equals(entrust.getMarketerId());
            }
        } else if (userRole == Role.TESTER) {
            if (currStage == EntrustStage.TESTER_AUDITING) {
                return userId.equals(entrust.getTesterId());
            }
        }
        return false;
    }

    @Override
    public void updateCustomer(String entrustId, Long customerId, Long userId, Role userRole) {
        if (userRole != Role.ADMIN) {
            throw new EntrustPermissionDeniedException("无权更改委托客户");
        }
        Entrust entrust = findEntrust(entrustId, userId, userRole);
        if (!entrustDAO.updateCustomerId(entrust.getId(), customerId)) {
            throw new EntrustDAOFailureException("更新客户失败");
        }
    }

    @Override
    public void updateStatus(String entrustId, EntrustStatus status, Long userId, Role userRole) {
        if (userRole != Role.ADMIN) {
            throw new EntrustPermissionDeniedException("无权修改委托状态");
        }
        Entrust entrust = findEntrust(entrustId, userId, userRole);
        if (!entrustDAO.updateStatus(entrust.getId(), status)) {
            throw new EntrustDAOFailureException("更改委托阶段失败");
        }
    }

    @Override
    public void removeEntrust(String entrustId, Long userId, Role userRole) {
        if (userRole != Role.ADMIN) {
            throw new EntrustPermissionDeniedException("无权删除委托");
        }
        Entrust entrust = findEntrust(entrustId, userId, userRole);
        if (!entrustDAO.deleteEntrust(entrust.getId())) {
            throw new EntrustDAOFailureException("删除委托失败");
        }
    }

    @Override
    public Long checkConsistencyWithContract(String entrustId, Long userId, Role userRole) {
        Entrust entrust = findEntrust(entrustId, userId, userRole);
        // 检查是否已经有合同了
        if (entrust.getContractId() != null) {
            throw new EntrustInvalidStageException("该委托已经创建了一份合同");
        }
        // 检查委托阶段
        if (entrust.getStatus().getStage() != EntrustStage.CUSTOMER_ACCEPT_QUOTE) {
            throw new EntrustInvalidStageException("用户没有确认委托报价，不能创建合同");
        }
        // 检查角色权限
        if (userRole != Role.ADMIN && (userRole != Role.MARKETER || !userId.equals(entrust.getMarketerId()))) {
            throw new EntrustPermissionDeniedException("无权创建此委托对应的合同");
        }
        return entrust.getCustomerId();
    }

    // 假设此接口调用是正确的，因为这是一个内部接口
    @Override
    public void registerContract(String entrustId, String contractId) {
        if (!entrustDAO.updateContractId(entrustId, contractId)) {
            throw new EntrustDAOFailureException("注册合同ID失败");
        }
    }

    @Override
    public Long getTesterId(String entrustId) {
        Entrust entrust = entrustDAO.findEntrustById(entrustId);
        if (entrust == null) {
            throw new EntrustNotFoundException("该委托不存在");
        }
        return entrust.getTesterId();
    }

    @Override
    public EntrustDto getEntrustDto(String entrustId) {
        Entrust entrust = entrustDAO.findEntrustById(entrustId);
        if (entrust == null) {
            throw new EntrustNotFoundException("该委托不存在");
        }
        return entrust.toEntrustDto();
    }

    @Override
    public void registerProject(String entrustId, String projectId) {
        if (!entrustDAO.updateProjectId(entrustId, projectId)) {
            throw new EntrustDAOFailureException("注册测试项目ID失败");
        }
    }

}

package com.njustc.onlinebiz.sample.service;

import com.njustc.onlinebiz.common.model.PageResult;
import com.njustc.onlinebiz.common.model.Role;
import com.njustc.onlinebiz.common.model.sample.SampleCollection;

public interface SampleService {

  /**
   * 创建一个样品集（样品集状态默认为已接收）
   * @param entrustId 该样品集所属的委托ID
   * @param userId 执行此操作的用户ID
   * @param userRole 执行此操作的用户角色
   * */
  String createSampleCollection(String entrustId, Long userId, Role userRole);

  /**
   * 根据样品集编号查找样品集
   * @param sampleCollectionId 样品集ID
   * @param userId 执行此操作的用户ID
   * @param userRole 执行此操作的用户角色
   * */
  SampleCollection findSpecificCollection(String sampleCollectionId, Long userId, Role userRole);

  /**
   * 查看当前所有样品集的列表。
   * @param page     查询结果的页码
   * @param pageSize 每页有多少条记录
   * @param userId   执行此操作的用户ID
   * @param userRole 执行此操作的用户角色
   * @return 返回所有的样品集列表，是一个分页结果。
   * */
  PageResult<SampleCollection> findAllCollections(Integer page, Integer pageSize, Long userId, Role userRole);

  /**
   * 更新样品集信息；
   * 相关的市场部员工：当样品集状态为 RECEIVED 时，可更新；其他状态不可更新；
   * 市场部主管/admin：什么状态都可更新，从而解决员工误操作的问题；
   * @param sampleCollectionId 待更新样品集ID
   * @param sampleCollection 样品集更新内容
   * @param userId 执行此操作的用户ID
   * @param userRole 执行此操作的用户角色
   * */
  void updateSampleCollection(String sampleCollectionId, SampleCollection sampleCollection, Long userId, Role userRole);




}

package com.njustc.onlinebiz.sample.dao;

import com.njustc.onlinebiz.common.model.sample.SampleCollection;

import java.util.List;

public interface SampleDAO {

  /**
   * 添加样品集到数据库
   * @param sampleCollection 要保存的样品集对象
   * @return 返回保存后的样品集对象
   * */
  SampleCollection insertSampleCollection(SampleCollection sampleCollection);

  /**
   * 获取总样品集的数目，主要配合分页查询使用
   * @return  总的样品集数目
   * */
  long countAll();

  /**
   * 查询所有样品集的列表，允许设置分页页码和每页记录条数，页码从1开始
   * @param page 要查询的页码
   * @param pageSize 每页有多少条记录
   * @return 返回该页上的样品集列表
   * */
  List<SampleCollection> findAllCollections(Integer page, Integer pageSize);

  /**
   * 根据样品集ID查询样品集
   * @param sampleCollectionId 要查询的样品集ID
   * @return 如果存在则返回查找到的样品集对象，不存在返回 null
   * */
  SampleCollection findSampleCollectionById(String sampleCollectionId);

  /**
   * 更新指定id的样品集，只更新name， samples， stage
   * @param sampleCollectionId 样品集ID
   * @param sampleCollection 新的样品集对象
   * @return 成功返回 true，失败返回 false
   * */
  Boolean updateSampleCollection(String sampleCollectionId, SampleCollection sampleCollection);

  /**
   * 删除指定id的样品集，返回是否成功
   * */
  Boolean deleteSampleCollection(String id);
}

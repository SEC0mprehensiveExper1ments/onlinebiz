package com.njustc.onlinebiz.sample.dao;

import com.njustc.onlinebiz.common.model.sample.SampleCollection;

public interface SampleDAO {
  // 添加样品集到数据库
  SampleCollection insertSampleCollection(SampleCollection sampleCollection);

  // 根据样品集ID查询样品集
  SampleCollection findSampleCollectionById(String id);

  // 更新指定id的样品集，返回是否成功
  Boolean updateSampleCollection(String id, SampleCollection sampleCollection);

  // 删除指定id的样品集，返回是否成功
  Boolean deleteSampleCollection(String id);
}

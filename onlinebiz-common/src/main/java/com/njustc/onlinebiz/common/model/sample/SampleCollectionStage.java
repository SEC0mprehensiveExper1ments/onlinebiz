package com.njustc.onlinebiz.common.model.sample;

public enum SampleCollectionStage {

  // 样品集状态：样品已接受 ---> 归档/销毁/归还（最终态）
  // 样品已接收
  RECEIVED,
  // 归档
  SEALED,
  // 销毁
  DESTROYED,
  // 归还
  RETURNED
}

package com.njustc.onlinebiz.common.model.sample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SampleCollection {
  // 样品集Id
  private String id;
  // 委托Id
  private String entrustId;
  // 市场部人员ID
  private Long marketerId;
  // 集合的名称
  private String name;
  // 集合内的样品
  private List<Sample> samples;
  // 集合的状态
  private SampleCollectionStage stage;
}

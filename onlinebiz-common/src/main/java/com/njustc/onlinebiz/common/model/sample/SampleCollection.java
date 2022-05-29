package com.njustc.onlinebiz.common.model.sample;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SampleCollection {
  // 样品集Id
  private String id;
  // 委托Id
  private Long entrustId;
  // 集合的名称
  private String name;
  // 集合内的样品
  private List<Sample> samples;
}

package com.clairvoyant.weather.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gufran Khan
 * @version 1.0
 * @date 17-08-2021 16:20
 */

@Data
@NoArgsConstructor
public class WeatherDto {

  private Long id;
  private String name;
  private Double temp;
  private Double feels_like;
}

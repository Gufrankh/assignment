package com.clairvoyant.weather.handler;

import static com.clairvoyant.weather.contants.WeatherConstants.WEATHER_FUNCTIONAL_END_POINT;

import com.clairvoyant.weather.document.WeatherDetails;
import com.clairvoyant.weather.repository.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Gufran Khan
 * @version 1.0
 * @date 23-08-2021 14:11
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
class WeatherHandlerTest {

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private WeatherRepository weatherRepository;

  @Test
  @WithMockUser(roles = "ADMIN")
  void getAllWeatherDetails() {
    WeatherDetails weatherDetails = new WeatherDetails();
    weatherDetails.setId(null);
    weatherDetails.setName("Mumbai");
    weatherDetails.setTemp(299.12);
    weatherDetails.setFeelsLike(300.12);
    Mockito.when(weatherRepository.findAll()).thenReturn(Flux.just(weatherDetails));
    webTestClient.get().uri(WEATHER_FUNCTIONAL_END_POINT).exchange().expectStatus().isOk()
        .expectBody()
        .jsonPath("[0].name").isEqualTo("Mumbai");
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void createWeatherDetails() {
    WeatherDetails weatherDetails = new WeatherDetails();
    weatherDetails.setId(null);
    weatherDetails.setName("Mumbai");
    weatherDetails.setTemp(299.12);
    weatherDetails.setFeelsLike(300.12);

    Mockito.when(weatherRepository.save(weatherDetails)).thenReturn(Mono.just(weatherDetails));
    webTestClient.post().uri(WEATHER_FUNCTIONAL_END_POINT).bodyValue(weatherDetails).exchange()
        .expectStatus()
        .isOk().expectBody(WeatherDetails.class);
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void updateWeather() {
    WeatherDetails weatherDetails = new WeatherDetails();
    weatherDetails.setId(8131499L);
    weatherDetails.setName("Mumbai");
    weatherDetails.setTemp(299.12);
    weatherDetails.setFeelsLike(300.12);
    Mockito.when(weatherRepository.findById(8131499L)).thenReturn(Mono.just(weatherDetails));
    Mockito.when(weatherRepository.save(weatherDetails)).thenReturn(Mono.just(weatherDetails));
    webTestClient.put().uri(WEATHER_FUNCTIONAL_END_POINT.concat("/{id}"), 8131499L)
        .bodyValue(weatherDetails)
        .exchange().expectStatus().isOk().expectBody(WeatherDetails.class);

  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void getWeatherDetailsByCity() {
    WeatherDetails weatherDetails = new WeatherDetails();
    weatherDetails.setId(123L);
    weatherDetails.setName("Mumbai");
    weatherDetails.setTemp(299.12);
    weatherDetails.setFeelsLike(300.12);
    Mockito.when(weatherRepository.findByName("Mumbai")).thenReturn(Mono.just(weatherDetails));
    webTestClient.get().uri(WEATHER_FUNCTIONAL_END_POINT.concat("/city/{city}"), "Mumbai")
        .exchange().expectStatus()
        .isOk().expectBody().jsonPath("$.name").isEqualTo("Mumbai");

  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void deleteWeatherDetails() {
    Mockito.when(weatherRepository.deleteById(8131499L)).thenReturn(Mono.empty());
    webTestClient.delete().uri(WEATHER_FUNCTIONAL_END_POINT.concat("/{id}"), 8131499L)
        .accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk().expectBody(Void.class);
  }
}
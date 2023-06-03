package com.idf.currency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Document(collection = "currency")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Currency implements Serializable {

  private static final long serialVersionUID = 4900493878723528744L;

  @Id
  @JsonProperty("id")
  private Long id;

  @JsonProperty("symbol")
  private String symbol;

  @JsonProperty("price_usd")
  private double priceUsd;

  private LocalDateTime time;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Currency currency = (Currency) o;
    return Objects.equals(id, currency.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}

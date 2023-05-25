package com.idf.currency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CURRENCY")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Currency implements Serializable {

  private static final long serialVersionUID = 4900493878723528744L;

  @javax.persistence.Id
  @Column(name = "ID")
  @JsonProperty("id")
  private Long id;

  @Column(name = "SYMBOL")
  @JsonProperty("symbol")
  private String symbol;

  @Column(name = "PRICE_USD")
  @JsonProperty("price_usd")
  private double priceUsd;

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

package com.neemiasgabriel.sicredi.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfo {
  private String agencia;
  private String conta;
  private Double saldo;
  private String status;
  private String result;

  public AccountInfo(String agencia, String conta, Double saldo, String status) {
    this.agencia = agencia;
    this.conta = conta;
    this.saldo = saldo;
    this.status = status;
  }
}

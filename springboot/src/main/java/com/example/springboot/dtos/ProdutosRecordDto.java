package com.example.springboot.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutosRecordDto(@NotBlank String nome, @NotNull BigDecimal valor, String imagem) {

}

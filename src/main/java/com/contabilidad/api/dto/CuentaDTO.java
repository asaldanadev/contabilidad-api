package com.contabilidad.api.dto;

import com.contabilidad.api.model.Cuenta;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

public class CuentaDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "El código es obligatorio")
        @Size(max = 20, message = "El código no puede superar 20 caracteres")
        private String codigo;

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        private String nombre;

        @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
        private String descripcion;

        @NotNull(message = "El tipo de cuenta es obligatorio")
        private Cuenta.TipoCuenta tipo;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String codigo;
        private String nombre;
        private String descripcion;
        private Cuenta.TipoCuenta tipo;
        private BigDecimal saldo;
        private Boolean activa;

        public static Response fromEntity(Cuenta cuenta) {
            return Response.builder()
                    .id(cuenta.getId())
                    .codigo(cuenta.getCodigo())
                    .nombre(cuenta.getNombre())
                    .descripcion(cuenta.getDescripcion())
                    .tipo(cuenta.getTipo())
                    .saldo(cuenta.getSaldo())
                    .activa(cuenta.getActiva())
                    .build();
        }
    }
}

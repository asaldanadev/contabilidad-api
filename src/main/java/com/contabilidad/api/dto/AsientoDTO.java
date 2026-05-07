package com.contabilidad.api.dto;

import com.contabilidad.api.model.Asiento;
import com.contabilidad.api.model.LineaAsiento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AsientoDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotNull(message = "La fecha es obligatoria")
        private LocalDate fecha;

        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
        private String descripcion;

        @Size(max = 50, message = "La referencia no puede superar 50 caracteres")
        private String referencia;

        @NotEmpty(message = "El asiento debe tener al menos una línea")
        @Valid
        private List<LineaRequest> lineas;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LineaRequest {

        @NotNull(message = "El ID de la cuenta es obligatorio")
        private Long cuentaId;

        @NotNull(message = "El tipo de movimiento es obligatorio")
        private LineaAsiento.TipoMovimiento tipo;

        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
        private BigDecimal monto;

        @Size(max = 255)
        private String descripcion;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private LocalDate fecha;
        private String descripcion;
        private String referencia;
        private Asiento.EstadoAsiento estado;
        private List<LineaResponse> lineas;

        public static Response fromEntity(Asiento asiento) {
            List<LineaResponse> lineasResp = asiento.getLineas() == null ? List.of() :
                    asiento.getLineas().stream().map(LineaResponse::fromEntity).toList();
            return Response.builder()
                    .id(asiento.getId())
                    .fecha(asiento.getFecha())
                    .descripcion(asiento.getDescripcion())
                    .referencia(asiento.getReferencia())
                    .estado(asiento.getEstado())
                    .lineas(lineasResp)
                    .build();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LineaResponse {
        private Long id;
        private Long cuentaId;
        private String cuentaCodigo;
        private String cuentaNombre;
        private LineaAsiento.TipoMovimiento tipo;
        private BigDecimal monto;
        private String descripcion;

        public static LineaResponse fromEntity(LineaAsiento linea) {
            return LineaResponse.builder()
                    .id(linea.getId())
                    .cuentaId(linea.getCuenta().getId())
                    .cuentaCodigo(linea.getCuenta().getCodigo())
                    .cuentaNombre(linea.getCuenta().getNombre())
                    .tipo(linea.getTipo())
                    .monto(linea.getMonto())
                    .descripcion(linea.getDescripcion())
                    .build();
        }
    }
}

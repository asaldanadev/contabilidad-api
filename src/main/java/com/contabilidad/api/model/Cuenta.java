package com.contabilidad.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "cuentas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCuenta tipo;

    @Column(nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal saldo = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activa = true;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
    private List<LineaAsiento> lineas;

    public enum TipoCuenta {
        ACTIVO, PASIVO, PATRIMONIO, INGRESO, GASTO
    }
}

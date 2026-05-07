package com.contabilidad.api.service;

import com.contabilidad.api.dto.AsientoDTO;
import com.contabilidad.api.exception.ResourceNotFoundException;
import com.contabilidad.api.model.*;
import com.contabilidad.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsientoService {

    private final AsientoRepository asientoRepository;
    private final CuentaRepository cuentaRepository;

    public List<AsientoDTO.Response> listarTodos() {
        return asientoRepository.findAll().stream()
                .map(AsientoDTO.Response::fromEntity)
                .toList();
    }

    public AsientoDTO.Response buscarPorId(Long id) {
        return AsientoDTO.Response.fromEntity(
                asientoRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Asiento", id))
        );
    }

    @Transactional
    public AsientoDTO.Response crear(AsientoDTO.Request request) {
        validarPartidaDoble(request.getLineas());

        Asiento asiento = Asiento.builder()
                .fecha(request.getFecha())
                .descripcion(request.getDescripcion())
                .referencia(request.getReferencia())
                .build();

        List<LineaAsiento> lineas = request.getLineas().stream().map(lr -> {
            Cuenta cuenta = cuentaRepository.findById(lr.getCuentaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cuenta", lr.getCuentaId()));
            return LineaAsiento.builder()
                    .asiento(asiento)
                    .cuenta(cuenta)
                    .tipo(lr.getTipo())
                    .monto(lr.getMonto())
                    .descripcion(lr.getDescripcion())
                    .build();
        }).toList();

        asiento.setLineas(lineas);
        return AsientoDTO.Response.fromEntity(asientoRepository.save(asiento));
    }

    @Transactional
    public AsientoDTO.Response confirmar(Long id) {
        Asiento asiento = asientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asiento", id));

        if (asiento.getEstado() != Asiento.EstadoAsiento.BORRADOR) {
            throw new IllegalArgumentException("Solo se pueden confirmar asientos en estado BORRADOR");
        }

        // Actualizar saldos de cuentas
        asiento.getLineas().forEach(linea -> {
            Cuenta cuenta = linea.getCuenta();
            if (linea.getTipo() == LineaAsiento.TipoMovimiento.DEBITO) {
                cuenta.setSaldo(cuenta.getSaldo().add(linea.getMonto()));
            } else {
                cuenta.setSaldo(cuenta.getSaldo().subtract(linea.getMonto()));
            }
            cuentaRepository.save(cuenta);
        });

        asiento.setEstado(Asiento.EstadoAsiento.CONFIRMADO);
        return AsientoDTO.Response.fromEntity(asientoRepository.save(asiento));
    }

    @Transactional
    public AsientoDTO.Response anular(Long id) {
        Asiento asiento = asientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asiento", id));

        if (asiento.getEstado() == Asiento.EstadoAsiento.ANULADO) {
            throw new IllegalArgumentException("El asiento ya está anulado");
        }
        asiento.setEstado(Asiento.EstadoAsiento.ANULADO);
        return AsientoDTO.Response.fromEntity(asientoRepository.save(asiento));
    }

    private void validarPartidaDoble(List<AsientoDTO.LineaRequest> lineas) {
        BigDecimal totalDebitos = lineas.stream()
                .filter(l -> l.getTipo() == LineaAsiento.TipoMovimiento.DEBITO)
                .map(AsientoDTO.LineaRequest::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCreditos = lineas.stream()
                .filter(l -> l.getTipo() == LineaAsiento.TipoMovimiento.CREDITO)
                .map(AsientoDTO.LineaRequest::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalDebitos.compareTo(totalCreditos) != 0) {
            throw new IllegalArgumentException(
                    "El asiento no está balanceado. Débitos: " + totalDebitos + " | Créditos: " + totalCreditos);
        }
    }
}

package com.contabilidad.api.service;

import com.contabilidad.api.dto.CuentaDTO;
import com.contabilidad.api.exception.ResourceNotFoundException;
import com.contabilidad.api.model.Cuenta;
import com.contabilidad.api.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    public List<CuentaDTO.Response> listarTodas() {
        return cuentaRepository.findAll().stream()
                .map(CuentaDTO.Response::fromEntity)
                .toList();
    }

    public List<CuentaDTO.Response> listarActivas() {
        return cuentaRepository.findByActivaTrue().stream()
                .map(CuentaDTO.Response::fromEntity)
                .toList();
    }

    public List<CuentaDTO.Response> listarPorTipo(Cuenta.TipoCuenta tipo) {
        return cuentaRepository.findByTipo(tipo).stream()
                .map(CuentaDTO.Response::fromEntity)
                .toList();
    }

    public CuentaDTO.Response buscarPorId(Long id) {
        return CuentaDTO.Response.fromEntity(
                cuentaRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Cuenta", id))
        );
    }

    @Transactional
    public CuentaDTO.Response crear(CuentaDTO.Request request) {
        if (cuentaRepository.existsByCodigo(request.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una cuenta con el código: " + request.getCodigo());
        }
        Cuenta cuenta = Cuenta.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .tipo(request.getTipo())
                .build();
        return CuentaDTO.Response.fromEntity(cuentaRepository.save(cuenta));
    }

    @Transactional
    public CuentaDTO.Response actualizar(Long id, CuentaDTO.Request request) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta", id));

        if (!cuenta.getCodigo().equals(request.getCodigo())
                && cuentaRepository.existsByCodigo(request.getCodigo())) {
            throw new IllegalArgumentException("Ya existe una cuenta con el código: " + request.getCodigo());
        }

        cuenta.setCodigo(request.getCodigo());
        cuenta.setNombre(request.getNombre());
        cuenta.setDescripcion(request.getDescripcion());
        cuenta.setTipo(request.getTipo());
        return CuentaDTO.Response.fromEntity(cuentaRepository.save(cuenta));
    }

    @Transactional
    public void desactivar(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta", id));
        cuenta.setActiva(false);
        cuentaRepository.save(cuenta);
    }
}

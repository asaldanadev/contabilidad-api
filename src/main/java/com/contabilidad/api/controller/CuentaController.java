package com.contabilidad.api.controller;

import com.contabilidad.api.dto.CuentaDTO;
import com.contabilidad.api.model.Cuenta;
import com.contabilidad.api.service.CuentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
@Tag(name = "Cuentas Contables", description = "Gestión del plan de cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    @GetMapping
    @Operation(summary = "Listar todas las cuentas")
    public ResponseEntity<List<CuentaDTO.Response>> listar() {
        return ResponseEntity.ok(cuentaService.listarTodas());
    }

    @GetMapping("/activas")
    @Operation(summary = "Listar cuentas activas")
    public ResponseEntity<List<CuentaDTO.Response>> listarActivas() {
        return ResponseEntity.ok(cuentaService.listarActivas());
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar cuentas por tipo (ACTIVO, PASIVO, PATRIMONIO, INGRESO, GASTO)")
    public ResponseEntity<List<CuentaDTO.Response>> listarPorTipo(@PathVariable Cuenta.TipoCuenta tipo) {
        return ResponseEntity.ok(cuentaService.listarPorTipo(tipo));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cuenta por ID")
    public ResponseEntity<CuentaDTO.Response> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear nueva cuenta contable")
    public ResponseEntity<CuentaDTO.Response> crear(@Valid @RequestBody CuentaDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaService.crear(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cuenta contable")
    public ResponseEntity<CuentaDTO.Response> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CuentaDTO.Request request) {
        return ResponseEntity.ok(cuentaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar cuenta contable")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        cuentaService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}

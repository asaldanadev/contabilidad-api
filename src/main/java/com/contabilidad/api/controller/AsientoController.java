package com.contabilidad.api.controller;

import com.contabilidad.api.dto.AsientoDTO;
import com.contabilidad.api.service.AsientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/asientos")
@RequiredArgsConstructor
@Tag(name = "Asientos Contables", description = "Gestión de asientos contables con partida doble")
public class AsientoController {

    private final AsientoService asientoService;

    @GetMapping
    @Operation(summary = "Listar todos los asientos")
    public ResponseEntity<List<AsientoDTO.Response>> listar() {
        return ResponseEntity.ok(asientoService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener asiento por ID")
    public ResponseEntity<AsientoDTO.Response> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(asientoService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear asiento contable (valida partida doble)")
    public ResponseEntity<AsientoDTO.Response> crear(@Valid @RequestBody AsientoDTO.Request request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(asientoService.crear(request));
    }

    @PatchMapping("/{id}/confirmar")
    @Operation(summary = "Confirmar asiento y actualizar saldos")
    public ResponseEntity<AsientoDTO.Response> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(asientoService.confirmar(id));
    }

    @PatchMapping("/{id}/anular")
    @Operation(summary = "Anular asiento contable")
    public ResponseEntity<AsientoDTO.Response> anular(@PathVariable Long id) {
        return ResponseEntity.ok(asientoService.anular(id));
    }
}

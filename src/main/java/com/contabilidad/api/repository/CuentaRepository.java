package com.contabilidad.api.repository;

import com.contabilidad.api.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
    List<Cuenta> findByTipo(Cuenta.TipoCuenta tipo);
    List<Cuenta> findByActivaTrue();
}

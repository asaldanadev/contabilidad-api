package com.contabilidad.api.repository;

import com.contabilidad.api.model.Asiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AsientoRepository extends JpaRepository<Asiento, Long> {
    List<Asiento> findByEstado(Asiento.EstadoAsiento estado);
    List<Asiento> findByFechaBetween(LocalDate desde, LocalDate hasta);
}

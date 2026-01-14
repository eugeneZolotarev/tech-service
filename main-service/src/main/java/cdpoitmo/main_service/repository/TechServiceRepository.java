package cdpoitmo.main_service.repository;

import cdpoitmo.main_service.entity.TechServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechServiceRepository extends JpaRepository<TechServiceEntity, Long> {
}

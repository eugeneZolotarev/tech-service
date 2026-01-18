package cdpoitmo.main_service.repository;

import cdpoitmo.main_service.entity.TechServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechServiceRepository extends JpaRepository<TechServiceEntity, Long> {
}

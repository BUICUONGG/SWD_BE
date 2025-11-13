package swd.fpt.exegroupingmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import swd.fpt.exegroupingmanagement.entity.TeamEntity;

@Repository
public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
}

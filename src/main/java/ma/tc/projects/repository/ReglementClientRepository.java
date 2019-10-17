package ma.tc.projects.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.tc.projects.entity.ReglementClient;

@Repository
public interface ReglementClientRepository extends JpaRepository<ReglementClient, Long> {

}

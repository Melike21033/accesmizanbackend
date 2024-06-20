
package com.mizanlabs.mr.repository;
import com.mizanlabs.mr.entities.Priorite;
import com.mizanlabs.mr.entities.Situation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SituationRepository extends JpaRepository<Situation, Long> {
    List<Situation> findByLabel(String label);

}

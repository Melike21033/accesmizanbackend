
package com.mizanlabs.mr.repository;

import com.mizanlabs.mr.entities.Priorite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrioriteRepository extends JpaRepository<Priorite, Long>{
    List<Priorite> findByLabel(String label);

}

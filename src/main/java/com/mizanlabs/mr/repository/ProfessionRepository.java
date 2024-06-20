
package com.mizanlabs.mr.repository;

import com.mizanlabs.mr.entities.Profession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProfessionRepository extends JpaRepository<Profession, Long>{
    List<Profession> findByLabel(String label);

}

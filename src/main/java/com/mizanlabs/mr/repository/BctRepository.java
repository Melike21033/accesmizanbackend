
package com.mizanlabs.mr.repository;

import com.mizanlabs.mr.entities.BCT;
import com.mizanlabs.mr.entities.Profession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BctRepository extends JpaRepository<BCT, Long> {
    List<BCT> findByLabel(String label);

}

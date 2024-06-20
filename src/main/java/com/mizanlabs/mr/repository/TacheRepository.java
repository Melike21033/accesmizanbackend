
package com.mizanlabs.mr.repository;

import com.mizanlabs.mr.entities.Tache;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TacheRepository extends JpaRepository<Tache, Long> {
}

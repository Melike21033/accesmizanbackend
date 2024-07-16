package com.mizanlabs.mr.repository;
import com.mizanlabs.mr.entities.Priorite;
import com.mizanlabs.mr.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusRepository extends JpaRepository<Status, Long> {
    List<Status> findByLabel(String label);

	Status findByLabelAndTableref(String string, String string2);
    List<Status> findByTablerefAndLabel(String tableref, String label);

}

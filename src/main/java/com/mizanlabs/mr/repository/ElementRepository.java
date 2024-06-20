package com.mizanlabs.mr.repository;

import com.mizanlabs.mr.entities.Element;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ElementRepository extends JpaRepository<Element, Long> {
    @Modifying
    @Transactional
    @Query("delete from Element e where e.type.id_type = :typeId")
    void deleteByTypeId(@Param("typeId") Long typeId);
    boolean existsByName(String name);

}



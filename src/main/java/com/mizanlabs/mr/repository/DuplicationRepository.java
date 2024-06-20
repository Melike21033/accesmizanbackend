package com.mizanlabs.mr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mizanlabs.mr.entities.Duplication;

@Repository
public interface DuplicationRepository extends JpaRepository<Duplication, Long> {

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Duplication d WHERE d.d = :d AND d.p = :p")
    boolean existsByDAndP(@Param("d") Long d, @Param("p") Long p);
}
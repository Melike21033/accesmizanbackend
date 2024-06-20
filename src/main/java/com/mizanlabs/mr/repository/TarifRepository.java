package com.mizanlabs.mr.repository;
import com.mizanlabs.mr.entities.Tarif;
import java.util.Optional;

import com.mizanlabs.mr.entities.Unite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface TarifRepository extends JpaRepository<Tarif, Long> {
    void deleteByUnite(Unite unite);
    void deleteByElementId(Long elementId);
    List<Tarif> findByElementId(Long elementId);
    List<Tarif> findByUniteId(Long uniteId);
    boolean existsByIsPrincipal(Boolean isPrincipal);
    List<Tarif> findByIsPrincipal(Boolean isPrincipal);
    List<Tarif> findByIsPrincipalAndElementId(Boolean isPrincipal, Long elementId);
    boolean existsByElementIdAndIsPrincipal(Long elementId, Boolean isPrincipal);
//    @Query("SELECT t FROM Tarif t WHERE t.element.id = :elementId AND t.unite.id = :uniteId")
//    Optional<Tarif> findTarifByElementIdAndUniteId(@Param("elementId") Long elementId, @Param("uniteId") Long uniteId);
//   
    @Query("SELECT t FROM Tarif t WHERE t.element.id = :elementId AND t.unite.unite = :uniteNom")
    Optional<Tarif> findTarifByElementIdAndUniteNom(@Param("elementId") Long elementId, @Param("uniteNom") String uniteNom);


    @Query("SELECT t.unite FROM Tarif t WHERE t.element.id = :elementId")
    List<Unite> findUnitesByElementId(@Param("elementId") Long elementId);
     @Query("SELECT u FROM Unite u WHERE u.id NOT IN (SELECT t.unite.id FROM Tarif t WHERE t.element.id = :elementId)")

    List<Unite> findUnitesNotLinkedToElement(@Param("elementId") Long elementId);

}
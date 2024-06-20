package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.*;
import com.mizanlabs.mr.repository.ElementRepository;
import com.mizanlabs.mr.repository.TarifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ElementService {

    private final ElementRepository elementRepository;
    private final TarifRepository tarifRepository;
    @Autowired
    public ElementService(ElementRepository elementRepository, TarifRepository tarifRepository) {
        this.elementRepository = elementRepository;
        this.tarifRepository = tarifRepository;
    }

    public Element saveOrUpdateElement(Element element) {
        // Vérifiez si l'élément existe déjà en fonction de son nom
        boolean exists = elementRepository.existsByName(element.getName());
        if (exists) {
            throw new DataIntegrityViolationException("Un élément avec le nom '" + element.getName() + "' existe déjà.");
        }
        return elementRepository.save(element);
    }
    public List<Element> getAllElements() {
        return elementRepository.findAll();
    }

    public Optional<Element> getElementById(Long id) {
        return elementRepository.findById(id);
    }

    @Transactional
    public void deleteElementById(Long id) {
//        tarifRepository.deleteByElementId(id); // Supprime d'abord les tarifs
        elementRepository.deleteById(id); // Ensuite, supprime l'élément
    }
    
    
    public Optional<Type> findElementTypeById(Long elementId) {
        Optional<Element> elementOptional = elementRepository.findById(elementId);
        return elementOptional.map(Element::getType);
    }

    public UnitesResponse getUnitesByElementId(Long elementId) {
        List<Unite> unites = tarifRepository.findUnitesByElementId(elementId);
        Unite unitePrincipale = null;
        List<Tarif> tarifsPrincipaux = tarifRepository.findByIsPrincipalAndElementId(true, elementId);

        if (!tarifsPrincipaux.isEmpty()) {
            // Supposant qu'il n'y a qu'une seule unité principale par élément
            unitePrincipale = tarifsPrincipaux.get(0).getUnite();
            return new UnitesResponse(unites, unitePrincipale);
        } else {
            // Si aucun tarif principal n'est trouvé, retourner uniquement la liste des unités
            return new UnitesResponse(unites, null);
        }
    }
}
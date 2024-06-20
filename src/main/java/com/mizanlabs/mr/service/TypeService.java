package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.Element;
import com.mizanlabs.mr.entities.Type;
import com.mizanlabs.mr.repository.ElementRepository;
import com.mizanlabs.mr.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TypeService {

    private final TypeRepository typeRepository;
private final ElementRepository elementRepository;
    @Autowired
    public TypeService(TypeRepository typeRepository, ElementRepository elementRepository) {
        this.typeRepository = typeRepository;
        this.elementRepository = elementRepository;
    }

    public Type saveOrUpdateType(Type type) {
        return typeRepository.save(type);
    }

    public List<Type> getAllTypes() {
        return typeRepository.findAll();
    }

    public Optional<Type> getTypeById(Long id) {
        return typeRepository.findById(id);
    }
    @Transactional
    public void deleteTypeById(Long id) {
        Optional<Type> typeOptional = typeRepository.findById(id);
        if (typeOptional.isPresent()) {
            elementRepository.deleteByTypeId(id);
            typeRepository.deleteById(id);
        }
    }
}
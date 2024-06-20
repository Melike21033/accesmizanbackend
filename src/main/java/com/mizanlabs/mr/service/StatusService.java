package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.Status;
import com.mizanlabs.mr.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class StatusService {
    private final StatusRepository StatusRepository;
    @Autowired
    public StatusService(StatusRepository StatusRepository) {
        this.StatusRepository = StatusRepository;
    }

    public Status saveOrUpdateStatus(Status Status) {
        return StatusRepository.save(Status);
    }

    public List<Status> getAllStatuss() {
        return StatusRepository.findAll();
    }

    public Optional<Status> getStatusById(Long id) {
        return StatusRepository.findById(id);
    }

    @Transactional
    public void deleteStatus(Long id) {
        // Additional checks can be performed here (like checking for dependencies before deleting)
        StatusRepository.deleteById(id);
    }
}

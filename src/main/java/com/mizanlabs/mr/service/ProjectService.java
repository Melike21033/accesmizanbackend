package com.mizanlabs.mr.service;

import com.mizanlabs.mr.entities.*;
import com.mizanlabs.mr.service.ClientService;
import com.mizanlabs.mr.service.TaskService;
import com.mizanlabs.mr.repository.ContactsClientsRepository;
import com.mizanlabs.mr.repository.ProjectRepository;
import com.mizanlabs.mr.repository.TaskRepository;
import com.mizanlabs.mr.repository.DevisRepository;
import  com.mizanlabs.mr.repository.ElementDevisRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.*;
import java.util.List;

@Service
public class ProjectService {

private final ElementDevisRepository elementDevisRepository;

    private final TaskService taskService;
    private final ProjectRepository ProjectRepository;
    private final ClientService ClientService;
    private final TaskRepository taskRepository;
    private final DevisRepository devisRepository;
    private final   ContactsClientsRepository contactsClientsRepository;
    @Lazy

    @Autowired
    public ProjectService(ElementDevisRepository elementDevisRepository, TaskService taskService, ProjectRepository ProjectRepository, com.mizanlabs.mr.service.ClientService clientService, TaskRepository taskRepository, DevisRepository devisRepository, ContactsClientsRepository contactsClientsRepository) {
        this.elementDevisRepository = elementDevisRepository;
        this.taskService = taskService;
        this.ProjectRepository = ProjectRepository;
        ClientService = clientService;
        this.taskRepository = taskRepository;
        this.devisRepository = devisRepository;
        this.contactsClientsRepository = contactsClientsRepository;

    }

    public List<Project> getAllProject() {
        return ProjectRepository.findAll();
    }

    public Optional<Project> getProjectById(Long id) {
        return ProjectRepository.findById(id);
    }

    public Project save(Project project) {

        return ProjectRepository.save(project);
    }

    public  Project createProject(Project Project) {
        return ProjectRepository.save(Project);
    }
    @Transactional
    public Project updateProject(Long id, Project projectDetails) {
        Project project = ProjectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Project not found with id: " + id));

        project.setProjectMO(projectDetails.getProjectMO());
        project.setProjectMOE(projectDetails.getProjectMOE());
        project.setProjectBCT(projectDetails.getProjectBCT());
        project.setProjectLocation(projectDetails.getProjectLocation());
        project.setTitle(projectDetails.getTitle());
        project.setAnnee(projectDetails.getAnnee());
        project.setCreationDate(projectDetails.getCreationDate());
        project.setSituation(projectDetails.getSituation());
        project.setStatus(projectDetails.getStatus());
        // Check if a new client is being set and fetch it from the database
        if (projectDetails.getClient() != null && projectDetails.getClient().getId() != null) {
            Client client = ClientService.getClientById(projectDetails.getClient().getId())
                    .orElseThrow(() -> new NoSuchElementException("Client not found with id: " + projectDetails.getClient().getId()));
            project.setClient(client);
        }

        return ProjectRepository.save(project);
    }



    public boolean deleteClient(Long id) {
        boolean exists = ProjectRepository.existsById(id);
        if (!exists) {
            return false;
        }
        ProjectRepository.deleteById(id);
        return true;
    }
    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
    public boolean deleteProject(Long id) {
        Optional<Project> projectOptional = ProjectRepository.findById(id);
        if (!projectOptional.isPresent()) {
            return false;
        }

        Project project = projectOptional.get();
//
//        // Parcourir chaque Devis associé au Projet
//        List<Devis> devisList = devisRepository.findByProject(project);
//        for (Devis devis : devisList) {
//            // Parcourir chaque Task associée au Devis
//            List<Task> tasks = taskRepository.findByDevisId(devis.getDevisId());
//            for (Task task : tasks) {
//                // Supprimer tous les ElementDevis associés à la Task
//                List<ElementDevis> elements = elementDevisRepository.findByTask(task);
//                if (!elements.isEmpty()) {
//                    elementDevisRepository.deleteAll(elements);
//                    elementDevisRepository.flush();
//                }
//                // Maintenant, il est sûr de supprimer la Task
//                taskRepository.delete(task);
//                taskRepository.flush();
//            }
//            // Après la suppression des Tasks et ElementDevis, supprimer le Devis
//            devisRepository.delete(devis);
//        }
//        devisRepository.flush();

        // Enfin, supprimer le Projet
        ProjectRepository.delete(project);
        return true;
    }

//    public String generateProjectReference(Project project) {
//        // Votre logique pour générer refProjet, peut-être quelque chose comme ci-dessous
//        String yearShort = project.getAnnee().substring(2);
//        String maxRefProjet = ProjectRepository.findMaxRefProjetByYear(project.getAnnee());
//        if (maxRefProjet == null) {
//            maxRefProjet = "001";
//
//        }
//        String numSeq = maxRefProjet.substring(2, 5);
//        int numSeqInt = Integer.parseInt(numSeq) + 1;
//        String refProjet = "AF" + String.format("%03d", numSeqInt) + '-' + yearShort;
//        project.setRefProjet(refProjet);
//        return refProjet;
    //}
    
    public String generateProjectReference(String annee) {
        // Votre logique pour générer refProjet, peut-être quelque chose comme ci-dessous
        String yearShort =annee.substring(2);
        String maxRefProjet = ProjectRepository.findMaxRefProjetByYear(annee);
        if (maxRefProjet == null || maxRefProjet.length()==0) {
            maxRefProjet = "AF000"+yearShort;

        }
        String numSeq = maxRefProjet.substring(2, 5);
        int numSeqInt = Integer.parseInt(numSeq) + 1;
        String refProjet = "AF" + String.format("%03d", numSeqInt) + '-' + yearShort;
       // project.setRefProjet(refProjet);
        return refProjet;
    }

    public String findMaxRefProjetByYear(String year) {
        return ProjectRepository.findMaxRefProjetByYear(year);
    }

    public String getRefProjetByProjectId(Long projectId) {
        return ProjectRepository.findRefProjetByProjectId(projectId);
    }
    public Project findById(Long projectId) {
        return ProjectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found with id: " + projectId));
    }
    

    public Client getClientByProjectId(Long projectId) {
        return ProjectRepository.findClientByProjectId(projectId);
    }

    
    public boolean isProjectTitleAvailable(String title) {
        return !ProjectRepository.findByTitle(title).isPresent();
    }
    
    public boolean isRefProjetAvailable(String refProjet) {
        return ProjectRepository.findByRefProjet(refProjet).isEmpty();
    }
    

    public List<Project> findProjectsByContactId(Long contactId) {
        List<Project> projects = new ArrayList<>();
        
        // Trouver tous les ContactsClients pour un contactId donné
        List<ContactsClients> contactsClientsList = contactsClientsRepository.findByContactId(contactId);
        
        // Pour chaque ContactsClients, utiliser projectId pour trouver le projet associé
        for (ContactsClients cc : contactsClientsList) {
            if (cc.getProjectId() != null) {
            	ProjectRepository.findById(cc.getProjectId()).ifPresent(projects::add);
            }
        }
        
        return projects;
    }
    public List<Project> getProjectsByClientId(Long clientId) {
        return ProjectRepository.findByClientId(clientId);
    }
    public Map<String, Long> getProjetStatusDistribution() {
        List<Object[]> results = ProjectRepository.getProjetStatusDistribution();
        Map<String, Long> distributionMap = new HashMap<>();

        for (Object[] result : results) {
            String statusName = (String) result[0];
            Long count = (Long) result[1];
            distributionMap.put(statusName, count);
        }

        return distributionMap;
    }
}


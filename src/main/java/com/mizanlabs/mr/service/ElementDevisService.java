//ElementDevisService
package com.mizanlabs.mr.service;

import com.mizanlabs.mr.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.mizanlabs.mr.entities.ElementDevis;
import com.mizanlabs.mr.entities.Task;
import com.mizanlabs.mr.repository.ElementDevisRepository;
import com.mizanlabs.mr.repository.ElementRepository;
import com.mizanlabs.mr.repository.TaskRepository;

import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.mizanlabs.mr.entities.Element;
import com.mizanlabs.mr.entities.Type;

@Service
public class ElementDevisService {

    private final ElementDevisRepository elementDevisRepository;
    private final TaskRepository taskRepository; // Déclarer TaskRepository
    private final TaskService taskService; // Déclarer TaskRepository
    private final ElementRepository elementRepository; // Déclarer TaskRepository
    private final TypeRepository typeRepository; // Déclarez TypeRepository


    @Lazy

    @Autowired
    public ElementDevisService(ElementDevisRepository elementDevisRepository, TaskRepository taskRepository, TaskService taskService, ElementRepository elementRepository, TypeRepository typeRepository) {
        this.elementDevisRepository = elementDevisRepository;
        this.taskRepository = taskRepository; // Initialiser TaskRepository
        this.taskService = taskService; // Initialiser TaskRepository
        this.elementRepository = elementRepository; // Initialiser TaskRepository


        this.typeRepository = typeRepository;
    }
    public List<ElementDevis> getAllElementDevis() {
        return elementDevisRepository.findAll();
    }
    public Optional<ElementDevis> getElementDevisById(Long id) {
        return elementDevisRepository.findById(id);
    }

    public ElementDevis addElementDevis(ElementDevis elementDevis) {
        return elementDevisRepository.save(elementDevis);
    }

    public Optional<ElementDevis> updateElementDevis(Long id, ElementDevis elementDevis) {
        return elementDevisRepository.findById(id).map(existingElementDevis -> {
            existingElementDevis.setElementQty(elementDevis.getElementQty());
            existingElementDevis.setElementNote(elementDevis.getElementNote());
            existingElementDevis.setStatus(elementDevis.getStatus());
            existingElementDevis.setMontant(elementDevis.getMontant());
            existingElementDevis.setNbreLots(elementDevis.getNbreLots());
            existingElementDevis.setType(elementDevis.getType());
            existingElementDevis.setUnite(elementDevis.getUnite());
            existingElementDevis.setqteLots(String.valueOf(elementDevis.getNbreLots()+"*"+elementDevis.getElementQty()));
            existingElementDevis.setName(elementDevis.getName());
            existingElementDevis.setPrix_unitaire(elementDevis.getPrix_unitaire());
            // setId n'a pas besoin d'être mis à jour car c'est l'identifiant de l'entité et il ne devrait normalement pas être modifié
            // Return saved entity
            existingElementDevis.setNbreLots(elementDevis.getNbreLots());

            return elementDevisRepository.save(existingElementDevis);
        });
    }
    public boolean deleteElementDevis(Long id) {
        if (elementDevisRepository.existsById(id)) {
            // Retrieve the element to be deleted
            ElementDevis elementDevis = elementDevisRepository.findById(id).orElseThrow(() -> new RuntimeException("ElementDevis not found with id: " + id));

            // Retrieve the task associated with the element
            Task task = taskRepository.findById(elementDevis.getTask().getTaskId()).orElseThrow(() -> new RuntimeException("Task not found with id: " + elementDevis.getTask().getTaskId()));

            // Update the total task amount before deleting the element
            task.setTotalTask(task.getTotalTask() - elementDevis.getMontant());

            // Save the updated task
            taskRepository.save(task);

            // Delete the element
            elementDevisRepository.deleteById(id);

            return true;
        }
        return false;
    }

    public List<ElementDevis> getElementDevisByTaskId(Long taskId) {
        return elementDevisRepository.findByTask_TaskId(taskId);
    }
    public List<ElementDevis> getElementDevisByTask(Task task) {
        return elementDevisRepository.findByTask(task);
    }
    public String getMaxRefEdevisForTask(Long taskId) {
        String maxRefEdevis= elementDevisRepository.findMaxRefEdevisByTaskId(taskId);
        String refprojtask;
        String  numSeqed;
        String yearShort;
        int numSeqedInt;
        if(maxRefEdevis==null) {
            String taskref= taskService.findRefTaskById(taskId);
            refprojtask=taskref.substring(2, 7);
            numSeqed="01";
            numSeqedInt = Integer.parseInt(numSeqed);
            yearShort=taskref.substring(7);
        }
        else {
            // Appeler la méthode du repository pour obtenir le max de refEdevis pour une tâche donnée
            yearShort=maxRefEdevis.substring(9,12);
            refprojtask=maxRefEdevis.substring(2,7);

            numSeqed = maxRefEdevis.substring(7,9);
            numSeqedInt = Integer.parseInt(numSeqed) + 1;}
        String refEdevis = "ED"   + refprojtask+  String.format("%02d", numSeqedInt) +yearShort;
        return refEdevis;}

    public String getRefEdevisById(Long id) {
        return elementDevisRepository.findRefEdevisById(id);
    }
    public List<ElementDevis> getElementDevisByTaskNameAndType(String taskName, Type type) {
        return elementDevisRepository.findByTaskNameAndType(taskName, type);
    }
    @Transactional
    public ElementDevis createAndAssignElementDevisToTask(ElementDevis elementDevis, Long taskId,Long elementId) {
        Integer nbreLots = elementDevis.getNbreLots();
        Integer elementQty = elementDevis.getElementQty();

        if (nbreLots == null || nbreLots <= 1 || elementQty<0) {
            // If nbreLots is null, 0, or 1, set qteLots to elementQty
            elementDevis.setqteLots(String.valueOf(elementQty*nbreLots));
        } else {
            // If nbreLots is greater than 1, set qteLots to "nbreLots*elementQty"
            String qteLotsValue = nbreLots + "*" + elementQty;
            elementDevis.setqteLots(qteLotsValue);
        }
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        Element element=elementRepository.findById(elementId).orElseThrow(() -> new RuntimeException("element not found with id: " + elementId));
        elementDevis.setTask(task);
        task.setTotalTask(this.getSumMontantByTaskId(taskId));
        task.setTotalTask(task.getTotalTask() + elementDevis.getMontant());
        elementDevis.setElement(element);

        return elementDevisRepository.save(elementDevis);
    }

    //test
    public List<ElementDevis> getElementDevisByTaskNameSortedByType(String taskName) {
        return elementDevisRepository.findByTaskNameSortedByType(taskName);
    }

    public int nbre_Edevis_par_type(String typeLabel) {
        return elementDevisRepository.countByType(typeLabel);
    }


    public List<ElementDevis> getDevisDetailsByType(String typeLabel) {
        return elementDevisRepository.findByTypeLabel(typeLabel);
    }
    public List<Type> getTypeByElementDevisId(Long elementDevisId) {
        // Récupérer l'élément de devis par son ID
        ElementDevis elementDevis = elementDevisRepository.findById(elementDevisId)
                .orElseThrow(() -> new RuntimeException("ElementDevis not found with id: " + elementDevisId));

        // Récupérer le type associé à cet élément de devis
        String typeLabel = elementDevis.getType(); // Assurez-vous que getType() retourne l'identifiant du type

        // Utiliser le TypeRepository pour obtenir le type correspondant
        return typeRepository.findByLabel(typeLabel);
    }



    @Transactional
    public Integer getSumMontantByTaskId(Long taskId) {
        return elementDevisRepository.sumMontantByTaskId(taskId);
    }
    public Map<String, Long> getElementDevisStatusDistribution() {
        List<Object[]> results = elementDevisRepository.getElementDevisStatusDistribution();
        Map<String, Long> distributionMap = new HashMap<>();

        for (Object[] result : results) {
            String statusName = (String) result[0];
            Long count = (Long) result[1];
            distributionMap.put(statusName, count);
        }

        return distributionMap;
    }
}
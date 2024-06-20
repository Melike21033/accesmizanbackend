package com.mizanlabs.mr.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.mizanlabs.mr.entities.Client; // Replace with your actual package and class names
import com.mizanlabs.mr.entities.Contact;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {


}

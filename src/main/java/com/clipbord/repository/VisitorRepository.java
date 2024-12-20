package com.clipbord.repository;


import com.clipbord.beans.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
    List<Visitor> findByVisitDate(LocalDate date);
}

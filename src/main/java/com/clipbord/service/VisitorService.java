package com.clipbord.service;


import com.clipbord.beans.Visitor;
import com.clipbord.repository.VisitorRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VisitorService {
    @Autowired
    private VisitorRepository visitorRepository;

    public void saveVisitor(String ip, String url) {
        Visitor visitor = new Visitor();
        visitor.setVisitDate(LocalDate.now());
        visitor.setVisitorIp(ip);
        visitor.setPageUrl(url);
        visitorRepository.save(visitor);
    }

    public long countVisitorsByDate(LocalDate date) {
        return visitorRepository.findByVisitDate(date).size();
    }

    public List<Visitor> findtotalVisitorCount() {
        return visitorRepository.findAll();
    }

    public void saveVisitor(HttpServletRequest request){
        String visitorIp = request.getRemoteAddr();
        String pageUrl = request.getRequestURI();
        saveVisitor(visitorIp, pageUrl);
    }
}
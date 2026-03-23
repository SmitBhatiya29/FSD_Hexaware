package com.service;

import com.model.Fund;
import com.model.Manager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManagerService {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void addManager(Manager manager) {
        em.persist(manager);
    }

    public Manager checkId(int id) {
        Manager manager = em.find(Manager.class, id);
        if (manager == null) {
            throw new RuntimeException("Manager not found");
        }
        return manager;
    }
    @Transactional
    public void addFund(Fund fund) {
        em.persist(fund);
    }

    public List<Fund> findFundByManagerID(int id1) {
        String sql = "SELECT fund FROM Fund fund WHERE fund.manager.id = :id1";
        TypedQuery<Fund> query = em.createQuery(sql, Fund.class);
        query.setParameter("id1", id1);
        return query.getResultList();
    }
}

package com.example.membership_api.dao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.membership_api.model.UserTierMetrics;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class UserTierMetricsDAO {

    @PersistenceContext
    private EntityManager em;

    public UserTierMetrics getForUserMonth(String userId, String monthYear) {
        List<UserTierMetrics> results = em.createQuery(
                "FROM UserTierMetrics u WHERE u.userId = :userId AND u.monthYear = :monthYear", UserTierMetrics.class)
                .setParameter("userId", userId)
                .setParameter("monthYear", monthYear)
                .getResultList();

        return results.isEmpty() ? null : results.get(0);
    }

    public UserTierMetrics createOrUpdate(String userId, int orderCount, double totalSpent) {
        String monthYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));

        UserTierMetrics utm = getForUserMonth(userId, monthYear);

        if (utm != null) {
            utm.setOrderCount(utm.getOrderCount() + orderCount);
            utm.setTotalSpent(utm.getTotalSpent() + totalSpent);
        } else {
            utm = new UserTierMetrics();
            utm.setUserId(userId);
            utm.setMonthYear(monthYear);
            utm.setOrderCount(orderCount);
            utm.setTotalSpent(totalSpent);
            em.persist(utm);
        }

        return utm;
    }

    public List<UserTierMetrics> getAllMetrics() {
        return em.createQuery("FROM UserTierMetrics u ORDER BY u.monthYear DESC", UserTierMetrics.class)
                 .getResultList();
    }

    public List<UserTierMetrics> getTopSpenders(String monthYear, int topN) {
        return em.createQuery("FROM UserTierMetrics u WHERE u.monthYear = :monthYear ORDER BY u.totalSpent DESC", UserTierMetrics.class)
                 .setParameter("monthYear", monthYear)
                 .setMaxResults(topN)
                 .getResultList();
    }
}

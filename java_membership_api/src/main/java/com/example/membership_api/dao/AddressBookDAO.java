package com.example.membership_api.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.membership_api.model.AddressBook;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class AddressBookDAO {

    @PersistenceContext
    private EntityManager em;

    public AddressBook create(AddressBook address) {
        em.persist(address);
        return address;
    }

    public List<AddressBook> getByUser(String userId) {
        return em.createQuery("FROM AddressBook WHERE userId = :userId", AddressBook.class)
                 .setParameter("userId", userId)
                 .getResultList();
    }

    public AddressBook getById(String addressId) {
        return em.find(AddressBook.class, addressId);
    }

    public AddressBook getByIdUserId(String addressId, String userId) {
        try {
            return em.createQuery(
                    "SELECT a FROM AddressBook a WHERE a.id = :addressId AND a.userId = :userId",
                    AddressBook.class
                )
                .setParameter("addressId", addressId)
                .setParameter("userId", userId)
                .getSingleResult();
        } catch (NoResultException e) {
            return null; 
        }
    }

    public AddressBook update(AddressBook existing, AddressBook updates) {
        existing.setName(updates.getName());
        existing.setAddress(updates.getAddress());
        existing.setPincode(updates.getPincode());
        existing.setCity(updates.getCity());
        existing.setState(updates.getState());
        existing.setCountry(updates.getCountry());
        existing.setIsDefault(updates.getIsDefault());
        return existing;
    }

    public void delete(AddressBook address) {
        em.remove(address);
    }
}

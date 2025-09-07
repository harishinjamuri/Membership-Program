package com.example.membership_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.membership_api.dao.AddressBookDAO;
import com.example.membership_api.exception.NotFoundException;
import com.example.membership_api.model.AddressBook;

@Service
public class AddressBookService {

    @Autowired
    private AddressBookDAO addressBookDAO;

    public AddressBook createAddress(String userId, AddressBook data) {
        data.setUserId(userId);
        return addressBookDAO.create(data);
    }

    public List<AddressBook> getAddresses(String userId) {
        return addressBookDAO.getByUser(userId);
    }

    public AddressBook getAddressById(String addressId) {
        AddressBook address = addressBookDAO.getById(addressId);
        if (address == null) {
            throw new NotFoundException("Address not found or unauthorized");
        }
        return address;
    }

    public AddressBook updateAddress(String addressId, String userId, AddressBook updates) {
        AddressBook address =  addressBookDAO.getByIdUserId(addressId, userId);
        return addressBookDAO.update(address, updates);
    }

    public void deleteAddress(String addressId, String userId) {
        AddressBook address = addressBookDAO.getByIdUserId(addressId, userId);
        addressBookDAO.delete(address);
    }
}

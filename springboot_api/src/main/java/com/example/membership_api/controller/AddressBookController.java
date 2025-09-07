package com.example.membership_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.membership_api.exception.NotFoundException;
import com.example.membership_api.model.AddressBook;
import com.example.membership_api.service.AddressBookService;
import com.example.membership_api.utils.JwtUtil;
import com.example.membership_api.utils.ResponseUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/address")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @PostMapping
    public Object createAddress(
            HttpServletRequest request,
            @RequestBody AddressBook data
    ) {
        String userId = JwtUtil.extractUserIdFromRequest(request);
        if (userId == null) {
            return ResponseUtil.error("Unauthorized: Invalid or missing token.", 401);
        }

        try {
            AddressBook created = addressBookService.createAddress(userId, data);
            return ResponseUtil.success("Address created successfully", created);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }

    @GetMapping
    public Object getAddresses(HttpServletRequest request) {
        String userId = JwtUtil.extractUserIdFromRequest(request);
        if (userId == null) {
            return ResponseUtil.error("Unauthorized: Invalid or missing token.", 401);
        }

        try {
            List<AddressBook> addresses = addressBookService.getAddresses(userId);
            return ResponseUtil.success("Addresses fetched successfully", addresses);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }

    @GetMapping("/{addressId}")
    public Object getAddressById(
            HttpServletRequest request,
            @PathVariable("addressId") String addressId
    ) {
        try {
            AddressBook address = addressBookService.getAddressById(addressId);
            return ResponseUtil.success("Address fetched successfully", address);
        } catch (NotFoundException e) {
            return ResponseUtil.error(e.getMessage(), 404);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }

    @PutMapping("/{addressId}")
    public Object updateAddress(
            HttpServletRequest request,
            @PathVariable("addressId") String addressId,
            @RequestBody AddressBook updates
    ) {
        String userId = JwtUtil.extractUserIdFromRequest(request);
        if (userId == null) {
            return ResponseUtil.error("Unauthorized: Invalid or missing token.", 401);
        }

        try {
            AddressBook updated = addressBookService.updateAddress(addressId, userId, updates);
            return ResponseUtil.success("Address updated successfully", updated);
        } catch (NotFoundException e) {
            return ResponseUtil.error(e.getMessage(), 404);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }

    @DeleteMapping("/{addressId}")
    public Object deleteAddress(
            HttpServletRequest request,
            @PathVariable("addressId") String addressId
    ) {
        String userId = JwtUtil.extractUserIdFromRequest(request);
        if (userId == null) {
            return ResponseUtil.error("Unauthorized: Invalid or missing token.", 401);
        }

        try {
            addressBookService.deleteAddress(addressId, userId);
            return ResponseUtil.success("Address deleted successfully");
        } catch (NotFoundException e) {
            return ResponseUtil.error(e.getMessage(), 404);
        } catch (Exception e) {
            return ResponseUtil.error(e.getMessage());
        }
    }
}

package com.example.membership_api.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "address_book")
public class AddressBook extends BaseModel {
    private String name;

    @Column(name = "user_id", nullable = false)
    private String userId;

    private String pincode;

    @Column(columnDefinition="TEXT")
    private String address;

    private String city;

    private String state;

    private String country;

    @Column(name = "is_default")
    private Boolean isDefault = true;
}

package org.example.service;


import org.example.data.model.Address;
import org.example.data.model.Customers;
import org.example.dto.request.SetUpAddressRequest;

public interface AddressService {

    Address setUpAddress(SetUpAddressRequest request, Customers customers);
}

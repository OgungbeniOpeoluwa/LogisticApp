package org.example.service;

import lombok.AllArgsConstructor;
import org.example.data.model.Address;
import org.example.data.model.Customers;
import org.example.data.repository.AddressRepository;
import org.example.dto.request.SetUpAddressRequest;
import org.example.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LogisticAddressService implements AddressService {
    private AddressRepository repository;
    private ModelMapper modelMapper;
    @Override
    public Address setUpAddress(SetUpAddressRequest request, Customers customers) {
        Address address = modelMapper.map(request,Address.class);
        repository.save(address);
        return address;
    }
}

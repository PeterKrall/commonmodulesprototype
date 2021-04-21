package de.noventi.cm.example.api;

import de.noventi.cm.example.db.Address;
import de.noventi.cm.example.db.Customer;
import de.noventi.cm.example.db.Phone;
import de.noventi.cm.example.model.AddressDTO;
import de.noventi.cm.example.model.CustomerDTO;
import de.noventi.cm.example.model.PhoneDTO;

public class CustomerMapper {

  public CustomerDTO toCustomerDTO (final Customer customer) {
    CustomerDTO customerDTO = new CustomerDTO();
    customerDTO.setId(customer.getId());
    customerDTO.setFirstname(customer.getFirstname());
    customerDTO.setName(customer.getName());
    customerDTO.setTitle(customer.getTitle());
    customerDTO.setAddress(toAddressDTO(customer.getAddress()));
    if (customer.getPhones() != null) {
      for (Phone next : customer.getPhones()) {
        customerDTO.addPhonesItem(toPhoneDTO(next));
      }
    }

    return customerDTO;
  }

  public AddressDTO toAddressDTO (final Address address) {
    AddressDTO addressDTO = new AddressDTO();
    addressDTO.setId(address.getId());
    addressDTO.setStreet(address.getStreet());
    addressDTO.setCity(address.getCity());
    addressDTO.setHousenumber(address.getHousenumber());
    addressDTO.setPostcode(address.getPostcode());
    return addressDTO;
  }

  public PhoneDTO toPhoneDTO (final Phone phone) {
    PhoneDTO phoneDTO = new PhoneDTO();
    phoneDTO.setId(phone.getId());
    phoneDTO.setNumber(phone.getNumber());
    return phoneDTO;
  }
}
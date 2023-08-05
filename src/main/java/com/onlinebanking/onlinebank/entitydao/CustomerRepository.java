package com.onlinebanking.onlinebank.entitydao;

import com.onlinebanking.onlinebank.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Boolean existsByEmail(String email);
    Customer findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);
}

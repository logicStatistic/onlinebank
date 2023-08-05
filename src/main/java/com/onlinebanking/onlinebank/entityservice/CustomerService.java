package com.onlinebanking.onlinebank.entityservice;

import com.onlinebanking.onlinebank.entitydto.*;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    BankResponse createAccount(CustomerRequest customerRequest);

    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

    String nameEnquiry(EnquiryRequest enquiryRequest);
    BankResponse creditAccount(TransectionRequest transectionRequest);

    BankResponse debitAccount(TransectionRequest transectionRequest);

    BankResponse transfer(TransferRequest transferRequest);
}

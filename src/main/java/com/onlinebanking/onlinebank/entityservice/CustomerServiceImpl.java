package com.onlinebanking.onlinebank.entityservice;

import com.onlinebanking.onlinebank.entity.Customer;
import com.onlinebanking.onlinebank.entitydao.CustomerRepository;
import com.onlinebanking.onlinebank.entitydto.*;
import com.onlinebanking.onlinebank.entityutils.AccountUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    EmailService emailService;

    @Override
    public BankResponse createAccount(CustomerRequest customerRequest) {
        //checking if a customer exist in db
        if (customerRepository.existsByEmail(customerRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtility.ACCOUNT_EXISTS_CODE)
                    .ResponseMSG(AccountUtility.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        Customer newCustomer = Customer.builder()
                .firstName(customerRequest.getFirstName())
                .lastName(customerRequest.getLastName())
                .address(customerRequest.getAddress())
                .stateOfOrigin(customerRequest.getStateOfOrigin())
                .accountNumber(AccountUtility.generateAccountNumber())
                .phoneNumber(customerRequest.getPhoneNumber())
                .altPhoneNumber(customerRequest.getAltPhoneNumber())
                .email(customerRequest.getEmail())
                .accountBalance(BigDecimal.ZERO)
                .status("ACTIVE")
                .build();
        Customer savedCustomer = customerRepository.save(newCustomer);
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedCustomer.getEmail())
                .emailSubject("ACCOUNT CREATED")
                .messageBody("Congratulations!" + " " + savedCustomer.getFirstName() + " \n" +
                        "Thank you for creating an account with us." + "\n " +
                        "Account Details below: " + " " + "FullName:" + " " + savedCustomer
                        .getFirstName() + " " + savedCustomer.getLastName()
                        + "\n " + "Account Number: " + " " + savedCustomer.getAccountNumber())
                .build();
        emailService.sendSuccessEmail(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtility.ACCOUNT_CREATION_SUCCESS)
                .ResponseMSG(AccountUtility.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(savedCustomer.getAccountNumber())
                        .accountBalance(savedCustomer.getAccountBalance())
                        .accountName(savedCustomer.getFirstName() + " " +
                                savedCustomer.getLastName())
                        .build())
                .build();


    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        //firstly check if account exist and then print balance.
        boolean isAccountExist = customerRepository
                .existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtility.ACCOUNTNUMBER_EXISTS_CODE)
                    .ResponseMSG(AccountUtility.ACCOUNTNUMBER_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        Customer foundCustomer = customerRepository
                .findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtility.ACCOUNTNUMBER_EXISTS_SUCCESS)
                .ResponseMSG(AccountUtility.ACCOUNTNUMBER_EXISTS_MESSAGESUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountName(foundCustomer.getFirstName() + " " +
                                foundCustomer.getLastName())
                        .accountNumber(foundCustomer.getAccountNumber())
                        .accountBalance(foundCustomer.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExist = customerRepository
                .existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isAccountExist) {
            return AccountUtility.ACCOUNTNUMBER_EXISTS_MESSAGE;
        }
        Customer foundCustomer = customerRepository
                .findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundCustomer.getFirstName() + " " + foundCustomer.getLastName();
    }

    @Override
    public BankResponse creditAccount(TransectionRequest transectionRequest) {
        //check if the account exist before crediting the account
        boolean isAccountExist = customerRepository
                .existsByAccountNumber(transectionRequest.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtility.ACCOUNTNUMBER_EXISTS_CODE)
                    .ResponseMSG(AccountUtility.ACCOUNTNUMBER_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        Customer creditCustomer = customerRepository
                .findByAccountNumber(transectionRequest.getAccountNumber());
        creditCustomer.setAccountBalance(creditCustomer.getAccountBalance()
                .add(transectionRequest.getAmount()));
        customerRepository.save(creditCustomer);
        return BankResponse.builder()
                .responseCode(AccountUtility.ACCOUNT_CREDIT_CODE)
                .ResponseMSG(AccountUtility.ACCOUNT_CREDIT_SUCCESSMSG)
                .accountInfo(AccountInfo.builder()
                        .accountName(creditCustomer.getFirstName() + " " + creditCustomer.getLastName())
                        .accountNumber(transectionRequest.getAccountNumber())
                        .accountBalance(creditCustomer.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(TransectionRequest transectionRequest) {
        //check if account exist before debiting account
        boolean isAccountExist = customerRepository
                .existsByAccountNumber(transectionRequest.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtility.ACCOUNTNUMBER_EXISTS_CODE)
                    .ResponseMSG(AccountUtility.ACCOUNTNUMBER_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        Customer debitCustomer = customerRepository
                .findByAccountNumber(transectionRequest.getAccountNumber());
       BigDecimal availableBalance = debitCustomer.getAccountBalance();
       BigDecimal debitedAmount = transectionRequest.getAmount();
              if(availableBalance.compareTo(debitedAmount)<0){
                  //give an error message
                  return BankResponse.builder()
                          .responseCode(AccountUtility.ACCOUNT_DEBIT_CODE)
                          .ResponseMSG(AccountUtility.ACCOUNT_DEBIT_FAILURE)
                          .accountInfo(null)
                          .build();
              }else {
                  debitCustomer.setAccountBalance(availableBalance.subtract(debitedAmount));
                  customerRepository.save(debitCustomer);
                  return BankResponse.builder()
                          .responseCode(AccountUtility.ACCOUNT_DEBIT_CODE)
                          .ResponseMSG(AccountUtility.ACCOUNT_DEBIT_SUCCESS)
                          .accountInfo(AccountInfo.builder()
                                  .accountName(debitCustomer.getFirstName() + " " + debitCustomer.getLastName())
                                  .accountBalance(availableBalance.subtract(debitedAmount))
                                  .accountNumber(transectionRequest.getAccountNumber())
                                  .build())
                          .build();
              }
        }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {

        boolean isRecipientAccountExist = customerRepository.existsByAccountNumber(transferRequest.getRecipientAccount());
        if(!isRecipientAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtility.ACCOUNTNUMBER_EXISTS_CODE)
                    .ResponseMSG(AccountUtility.ACCOUNTNUMBER_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        Customer debitSourceAccount = customerRepository.findByAccountNumber(transferRequest.getSourceAccount());
        BigDecimal availableBalance1 = debitSourceAccount.getAccountBalance();
        BigDecimal debitedAmount2 = transferRequest.getTransferAmount();
        if(availableBalance1.compareTo(debitedAmount2)<0){
            //give an error message
            return BankResponse.builder()
                    .responseCode(AccountUtility.ACCOUNT_DEBIT_CODE)
                    .ResponseMSG(AccountUtility.ACCOUNT_DEBIT_FAILURE)
                    .accountInfo(null)
                    .build();
        } else {
            debitSourceAccount.setAccountBalance(availableBalance1.subtract(debitedAmount2));
            customerRepository.save(debitSourceAccount);

            Customer creditRecipientAccount = customerRepository.findByAccountNumber(transferRequest.getRecipientAccount());
            creditRecipientAccount.setAccountBalance(debitedAmount2.add(creditRecipientAccount.getAccountBalance()));
            Customer savedCreditAmount = customerRepository.save(creditRecipientAccount);
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(savedCreditAmount.getEmail())
                    .emailSubject("ACCOUNT CREDIT")
                    .messageBody("Chai My Love!" + " " + savedCreditAmount.getFirstName() + " \n" +
                                 "You received money from" +" "+ debitSourceAccount.getFirstName()  + "\n " +
                                 "FullName:" + " " + savedCreditAmount
                                 .getFirstName() + " " + savedCreditAmount.getLastName()
                                 + "\n " + "Account Number: " + " " + savedCreditAmount.getAccountBalance())
                    .build();
            emailService.sendSuccessEmail(emailDetails);
            return BankResponse.builder()
                    .responseCode(AccountUtility.ACCOUNT_TRANSFER_CODE)
                    .ResponseMSG(AccountUtility.ACCOUNT_TRANSFER_SUCCESS)
                    .accountInfo(AccountInfo.builder()
                            .accountName(debitSourceAccount.getFirstName() + " " + debitSourceAccount.getLastName())
                            .accountBalance(availableBalance1.subtract(debitedAmount2))
                            .accountNumber(transferRequest.getSourceAccount())
                            .build())
                    .build();
        }
    }
}

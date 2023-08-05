package com.onlinebanking.onlinebank.entitycontroller;

import com.onlinebanking.onlinebank.entitydto.*;
import com.onlinebanking.onlinebank.entityservice.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/controller")
@Tag( name = "User management APIs")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Operation(
            summary = "CREATE NEW USER ACCOUNT",
            description = "Creating a new user and assigning the user an ID"
    )
    @ApiResponse(
            responseCode = "001",
            description = "Htp 001 status created"
    )
    @PostMapping
    public BankResponse createAccount(@RequestBody CustomerRequest customerRequest) {
        return customerService.createAccount(customerRequest);
    }
    @Operation(
            summary = "BALANCE ENQUIRY",
            description = "if Account exit, check balance"
    )
    @ApiResponse(
            responseCode = "002",
            description = "Htp 002 status success"
    )
    @GetMapping("/balanceEnquiry")
   public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return customerService.balanceEnquiry(enquiryRequest);
    }
    @GetMapping("/nameEnquiry")
   public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest){
        return customerService.nameEnquiry(enquiryRequest);
   }
   @GetMapping("/credit")
   public BankResponse creditAccount(@RequestBody TransectionRequest transectionRequest){
        return customerService.creditAccount(transectionRequest);
   }
   @GetMapping("/debit")
   public BankResponse debitAccount(@RequestBody TransectionRequest transectionRequest){
        return customerService.debitAccount(transectionRequest);
   }
   @PostMapping("/transfer")
   public BankResponse transfer(@RequestBody TransferRequest transferRequest){
        return customerService.transfer(transferRequest);
   }
}

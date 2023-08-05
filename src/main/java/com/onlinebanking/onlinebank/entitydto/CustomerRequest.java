package com.onlinebanking.onlinebank.entitydto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {
    private String firstName;
    private String lastName;
    private String address;
    private String stateOfOrigin;
    private String accountNumber;
    private String email;
    private String phoneNumber;
    private String altPhoneNumber;
}

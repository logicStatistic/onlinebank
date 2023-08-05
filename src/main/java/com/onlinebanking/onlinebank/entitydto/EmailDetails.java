package com.onlinebanking.onlinebank.entitydto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmailDetails {
    private String recipient;
    private String senderMail;
    private String emailSubject;
    private String messageBody;
    private String attachment;

}

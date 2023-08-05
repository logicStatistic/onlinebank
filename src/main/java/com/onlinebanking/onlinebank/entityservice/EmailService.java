package com.onlinebanking.onlinebank.entityservice;

import com.onlinebanking.onlinebank.entitydto.EmailDetails;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendSuccessEmail(EmailDetails emailDetails);

}

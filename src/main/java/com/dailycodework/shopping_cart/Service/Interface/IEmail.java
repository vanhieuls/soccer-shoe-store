package com.dailycodework.shopping_cart.Service.Interface;

import jakarta.mail.MessagingException;

public interface IEmail {
    void sendverificationEmail(String to, String subject, String text) throws MessagingException;
}

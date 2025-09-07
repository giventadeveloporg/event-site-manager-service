package com.nextjstemplate.service.dto;

import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
public class EmailHostUrlPrefixDecoder {

    public String decodeEmailHostUrlPrefix(String encodedPrefix) {
        try {
            return new String(Base64.getDecoder().decode(encodedPrefix));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid base64 encoding for emailHostUrlPrefix: " + encodedPrefix);
        }
    }
}

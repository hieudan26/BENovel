package com.socialmedia.loginandregistration.model.payload.response.BaseCustomResponse;

import lombok.*;


import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class ErrorResponse {
    public ErrorResponse(String message, List<String> details) {
        super();
        this.message = message;
        this.details = details;
    }

    private String message;

    private List<String> details;
}

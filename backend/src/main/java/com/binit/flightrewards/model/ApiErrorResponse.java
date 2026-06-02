package com.binit.flightrewards.model;

import java.time.Instant;
import java.util.List;

public class ApiErrorResponse {

    public String message;

    public List<String> validationErrors;

    public String timestamp = Instant.now().toString();
}
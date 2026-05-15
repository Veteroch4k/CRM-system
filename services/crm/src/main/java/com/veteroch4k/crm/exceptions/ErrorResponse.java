package com.veteroch4k.crm.exceptions;

import java.time.LocalDateTime;

public record ErrorResponse (
    LocalDateTime time,
    int status,
    String error,
    String message)
{}

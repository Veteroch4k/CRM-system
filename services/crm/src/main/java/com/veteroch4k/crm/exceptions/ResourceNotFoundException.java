package com.veteroch4k.crm.exceptions;

public class ResourceNotFoundException extends RuntimeException{

  public ResourceNotFoundException(String message) {
    super(message);
  }
}

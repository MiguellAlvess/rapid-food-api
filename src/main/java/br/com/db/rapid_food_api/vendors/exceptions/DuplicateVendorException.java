package br.com.db.rapid_food_api.vendors.exceptions;

public class DuplicateVendorException extends RuntimeException {
    public DuplicateVendorException(String message) {
        super(message);
    }
}
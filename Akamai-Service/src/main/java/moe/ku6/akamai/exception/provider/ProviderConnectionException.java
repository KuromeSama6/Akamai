package moe.ku6.akamai.exception.provider;

import moe.ku6.akamai.exception.api.APIException;

public class ProviderConnectionException extends APIException {
    public ProviderConnectionException(String message) {
        super(504, 39, message);
    }
}
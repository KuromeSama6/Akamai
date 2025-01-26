package moe.ku6.akamai.exception.provider;

import moe.ku6.akamai.exception.api.APIException;

public class ProviderNotAvailableException extends APIException {
    public ProviderNotAvailableException(String message) {
        super(502, 58, message);
    }
}

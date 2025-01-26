package moe.ku6.akamai.exception.provider;

import moe.ku6.akamai.exception.api.APIException;
import moe.ku6.akamai.util.JsonWrapper;

public class ProviderRequestException extends APIException {
    public ProviderRequestException(int code, int statusCode, String message, JsonWrapper errorData) {
        super(code, statusCode, message, errorData);
    }

    public ProviderRequestException(String message) {
        super(502, 61, message);
    }
}

package moe.ku6.akamai.exception.api;

public class ForbiddenException extends APIException {
    public ForbiddenException() {
        super(403, 16, "operation not permitted");
    }
}

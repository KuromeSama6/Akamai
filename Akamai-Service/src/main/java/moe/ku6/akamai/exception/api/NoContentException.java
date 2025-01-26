package moe.ku6.akamai.exception.api;

public class NoContentException extends APIException {
    public NoContentException() {
        super(204, 0, "ok");
    }
}

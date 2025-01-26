package moe.ku6.akamai.exception.api;

public class NotFoundException extends APIException {

    public NotFoundException() {
        super(404, 15, "not found");
    }

    public NotFoundException(String message) {
        super(404, 15, message);
    }
}

package moe.ku6.akamai.exception.api;

public class FeatureNotImplementedException extends APIException {
    public FeatureNotImplementedException() {
        super(501, 15, "feature not implemented");
    }
}

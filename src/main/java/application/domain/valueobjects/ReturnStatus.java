package application.domain.valueobjects;

public final class ReturnStatus extends DomainCatalog {
    public static final ReturnStatus REQUESTED = new ReturnStatus("REQUESTED", "Requested", "Return initiated by the buyer.");
    public static final ReturnStatus APPROVED = new ReturnStatus("APPROVED", "Approved", "Return accepted.");
    public static final ReturnStatus REJECTED = new ReturnStatus("REJECTED", "Rejected", "Return denied.");
    public static final ReturnStatus COMPLETED = new ReturnStatus("COMPLETED", "Completed", "Return finalized.");

    private ReturnStatus(String code, String name, String description) {
        super(code, name, description);
    }
}
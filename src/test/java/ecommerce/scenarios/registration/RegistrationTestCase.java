package ecommerce.scenarios.registration;


public class RegistrationTestCase {
    private int testCaseId;
    private int userId;
    private boolean successful;
    private String explanation;
    private boolean includes;

    public int getTestCaseId() {
        return testCaseId;
    }

    public void setTestCaseId(int testCaseId) {
        this.testCaseId = testCaseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public boolean isIncludes() {
        return includes;
    }

    public void setIncludes(boolean includes) {
        this.includes = includes;
    }
}

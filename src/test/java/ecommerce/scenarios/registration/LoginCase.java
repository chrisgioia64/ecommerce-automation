package ecommerce.scenarios.registration;

public class LoginCase {
    private int id;
    private String email;
    private String password;
    private boolean successful;
    private String comments;
    private boolean includes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isIncludes() {
        return includes;
    }

    public void setIncludes(boolean includes) {
        this.includes = includes;
    }

    @Override
    public String toString() {
        return this.email + " " + this.password;
    }
}

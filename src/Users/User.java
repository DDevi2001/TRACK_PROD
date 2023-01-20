package Users;

public class User {
    private String id;
    private String role;
    public User(String id, String role) {
        this.id = id;
        this.role = role;
    }
    public String getId() {
        return id;
    }
    public String getRole() {
        return role;
    }
}

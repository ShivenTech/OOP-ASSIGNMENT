package user;

public abstract class User {
    private String id;
    private String name;
    private String contactNumber;
    private String email;
    private String password; // NEW: Every user now has a password!

    public User(String id, String name, String contactNumber, String email, String password) {
        this.id = id;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
        this.password = password;
    }

    public abstract String getRole();

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
package user.employee;

public class Admin extends Employee {
    public Admin(String id, String name, String phone, String email, String password) {
        super(id, name, phone, email, password);
    }
    @Override public String getRole() { return "Admin"; } 
}
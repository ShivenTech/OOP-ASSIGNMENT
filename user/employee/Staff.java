package user.employee;

public class Staff extends Employee {
    public Staff(String id, String name, String phone, String email, String password) {
        super(id, name, phone, email, password);
    }
    @Override public String getRole() { return "Staff"; } 
}
package user.employee;

import user.User;

public abstract class Employee extends User {
    private String password;

    public Employee(String id, String name, String phone, String email, String password) {
        super(id, name, phone, email); 
        this.password = password;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
package user.employee;

import user.User;

public abstract class Employee extends User {
    public Employee(String id, String name, String phone, String email, String password) {
        // Password is now handed directly to the User grandparent
        super(id, name, phone, email, password); 
    }
}
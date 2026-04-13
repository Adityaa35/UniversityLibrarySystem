// models/Admin.java
package models;

public class Admin extends User {
    public Admin(String userId, String name, String password) {
        super(userId, name, password, "ADMIN");
    }
    
    @Override
    public String getDashboardInfo() {
        return "Admin: " + name + " - Full Access";
    }
}

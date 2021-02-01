package entities;

import config.ApplicationConfig;
import enums.Role;

/**
 * if needed to move to a database in future
 * we can use this id as unique identifier
 */
public class User {
    private Integer id;
    private String name;
    private String password;
    private Role role;
    private String email;

    public User(Integer id, String name, String password, Role role, String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
        this.email = email;
    }

    public User(String name) {
        this.name = name;
        this.role = ApplicationConfig.getDefaultRole();
        this.password = ApplicationConfig.getDefaultPassword();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", email='" + email + '\'' +
                '}';
    }
}

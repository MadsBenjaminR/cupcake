package app.entities;

public class User {
    private int userId;
    private String email;
    private String password;
    private String role;
    private int balance;

    public User(int userId, String email, String password, String role, int balance) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = role;
        this.balance = balance;
    }


    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public int getBalance(){
        return balance;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
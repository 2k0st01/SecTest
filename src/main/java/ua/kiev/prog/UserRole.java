package ua.kiev.prog;

public enum UserRole {
    ADMIN, MODER ,USER;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}

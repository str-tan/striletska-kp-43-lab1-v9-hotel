package com.hotel.model;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Visitor {
    private int id;
    private String fullName;
    private String email;

    public Visitor() {}//для json

    public Visitor(String fullName, String email) {
        this.id = ThreadLocalRandom.current().nextInt(1000, 10000);
        this.fullName = fullName;
        this.email = email;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }

    public boolean isValidEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }

    @Override
    public String toString() {
        return "Відвідувач: " + fullName + " (" + email + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visitor visitor = (Visitor) o;
        return id == visitor.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
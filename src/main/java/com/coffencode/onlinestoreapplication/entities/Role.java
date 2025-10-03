package com.coffencode.onlinestoreapplication.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //"ROLE_USER", "ROLE_ADMIN"
    @Column(nullable = false, unique = true)
    private String name;

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    public Long getId() {return id; }
    public String getName() {return name; }
    public void setName(String name) { this.name = name; }
}

package com.eigenmusik.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Artist {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Artist() {

    }

    public Artist(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

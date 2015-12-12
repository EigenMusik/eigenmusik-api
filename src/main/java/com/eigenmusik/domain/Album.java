package com.eigenmusik.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by timcoulson on 12/12/2015.
 */
@Entity
public class Album {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Album() {

    }

    public Album(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

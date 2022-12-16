package org.example;

import java.util.List;

public class Book {
    Integer id;
    String name;
    List<String> authors;
    String publisher;
    Integer year;
    Integer pages;
    Double cost;
    String type;

    public Book(Integer id, String name, List<String> authors, String publisher, Integer year, Integer pages, Double cost, String type) {
        this.id = id;
        this.name = name;
        this.authors = authors;
        this.publisher = publisher;
        this.year = year;
        this.pages = pages;
        this.cost = cost;
        this.type = type;
    }
}
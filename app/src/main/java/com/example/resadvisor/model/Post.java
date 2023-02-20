package com.example.resadvisor.model;

public class Post {
    public String title;
    public String description;
    public int price;
    public String res_name;
    public String res_address;


    public Post( String title, String description, int price,
                String res_name, String res_address) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.res_name = res_name;
        this.res_address = res_address;
    }
}

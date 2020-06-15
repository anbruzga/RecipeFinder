package com.example.mobdev_cw2;

public class Product {
    private int id;
    private String name;
    private double price;
    private int weight;
    private String description;
    private int availability;

    public Product(int id, String name, double price, int weight, String description, int availability) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.description = description;
        this.availability = availability;
    }


    public Product(String name, double price, int weight, String description) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.description = description;
    }

    public Product(String name, double price, int weight, String description, int availability) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.description = description;
        this.availability = availability;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvailability() {
        if (this.availability == 1) return "Available";
        else return "Not Available";
    }

    // returns 1 if available, 0 if not available, -1 if wrong inpyt
    public int setAvailability(String availability) {
        if (availability.equalsIgnoreCase("Available")){
            this.availability = 1;
            return 1;
        }
        else if (availability.equalsIgnoreCase("Not available")){
            this.availability = 0;
            return 0;
        }
        else{
            System.out.println("Wrong input!");
            return -1;
        }
    }
}

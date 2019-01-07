package com.example.desel.improvedfitnessappv2;

import java.io.Serializable;

public class BMI implements Serializable
{
    private  String name, surname;
    private double weight, height, bmi;

    // To get this we type alt + insert

    public String getName()
    {
        return name;
    }

    public String getSurname()
    {
        return surname;
    }

    public double getWeight()
    {
        return weight;
    }

    public double getHeight()
    {
        return height;
    }

    public double getBmi()
    {
        return bmi;
    }

    // Constructor
    BMI(String name, String surname, double weight, double height, double bmi)
    {
        this.name = name;
        this.surname = surname;
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
    }
}

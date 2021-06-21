package io.chofito.proyectox.random;

import me.lucko.helper.random.Weighted;

public class ObjectWeighted implements Weighted {
    private String name;
    private double weight;

    public ObjectWeighted() {}

    public ObjectWeighted(String name) {
        this.name = name;
    }

    public ObjectWeighted(double weight) {
        this.weight = weight;
    }

    public ObjectWeighted(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public double getWeight() {
        return weight;
    }
}

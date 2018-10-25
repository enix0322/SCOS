package es.source.code.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class FoodList implements Serializable {
    List<Food> Food_data_cold;
    List<Food> Food_data_hot;
    List<Food> Food_data_sea;
    List<Food> Food_data_drink;

    public FoodList(){
        this.Food_data_cold = new LinkedList();
        this.Food_data_hot = new LinkedList();
        this.Food_data_sea = new LinkedList();
        this.Food_data_drink = new LinkedList();
    }
    public void setFood_data_cold(List<Food> food_data_cold) {
        this.Food_data_cold = food_data_cold;
    }

    public void setFood_data_hot(List<Food> food_data_hot) {
        this.Food_data_hot = food_data_hot;
    }

    public void setFood_data_sea(List<Food> food_data_sea) {
        this.Food_data_sea = food_data_sea;
    }

    public void setFood_data_drink(List<Food> food_data_drink) {
        this.Food_data_drink = food_data_drink;
    }

    public List<Food> getFood_data_cold() {
        return  this.Food_data_cold;
    }

    public List<Food> getFood_data_hot() {
        return  this.Food_data_hot;
    }

    public List<Food> getFood_data_sea() {
        return  this.Food_data_sea;
    }

    public List<Food> getFood_data_drink() {
        return  this.Food_data_drink;
    }
}

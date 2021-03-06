package es.source.code.model;

import java.io.Serializable;

public class Food implements Serializable {
    private String food_name;
    private int food_price;
    private int food_num;
    private int food_store_num;
    private String food_remark;
    private boolean food_order;

    public Food(String food_name, int food_price, int food_num, String food_remark) {
        this.food_name = food_name;
        this.food_price = food_price;
        this.food_num = food_num;
        this.food_remark = food_remark;
        this.food_order = false;
        this.food_store_num = 1;
    }

    public Food(String food_name, int food_price) {
        this.food_name = food_name;
        this.food_price = food_price;
        this.food_num = 1;
        this.food_remark = "";
        this.food_order = false;
        this.food_store_num = 1;
    }

    public String get_food_name() {
        if(food_name.equals(null)) {
            food_name = "";
        }
        return food_name;
    }

    public int get_food_price() {
        return food_price;
    }

    public int get_food_num() {
        return food_num;
    }

    public int get_food_store_num() {
        return food_store_num;
    }

    public boolean get_food_order() {
        return food_order;
    }

    public String get_food_remark() {
        if(food_remark.equals(null)) {
            food_remark = "";
        }
        return food_remark;
    }

    public void set_food_name(String food_name) {

        this.food_name = food_name;
    }

    public void set_food_price(int food_price) {

        this.food_price = food_price;
    }

    public void set_food_num(int food_num) {

        this.food_num = food_num;
    }

    public void set_food_store_num(int food_store_num) {

        this.food_store_num = food_store_num;
    }

    public void set_food_remark(String food_remark) {

        this.food_remark = food_remark;
    }

    public void set_food_order(boolean food_order) {

        this.food_order = food_order;
    }
}

package es.source.code.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class User implements Serializable {

    private String userName;
    private String password;
    private Boolean oldUser;
    private List<Food> Not_Order_Food;
    private List<Food> Order_Food;
    private FoodList foodList;

    public User(){
        this.userName = "temp";
        this.password = "0";
        this.Not_Order_Food = new LinkedList<>();
        this.Order_Food = new LinkedList<>();
        this.oldUser = false;
        this.foodList = new FoodList();
    }

    public User (String userName,String password) {
        this.userName = userName;
        this.password = password;
        this.Not_Order_Food = new LinkedList<>();
        this.Order_Food = new LinkedList<>();
        this.oldUser = false;
        this.foodList = new FoodList();
    }

    public void setFoodList(FoodList foodList) {
        this.foodList = foodList;
    }

    public FoodList getFoodList() {
        return foodList;
    }

    public String Getter_userName() {
        return userName;
    }

    public void Setter_userName(String userName) {
        this.userName = userName;
    }

    public String Getter_password() {
        return password;
    }

    public void Setter_password(String password) {
        this.password = password;
    }

    public Boolean Getter_oldUser() {
        return oldUser;
    }

    public void Setter_oldUser(Boolean oldUser) {
        this.oldUser = oldUser;
    }

    public void Add_Not_Order_Food_List(List<Food> food){
        if(food.size()!=0) {
            this.Not_Order_Food.addAll(food);
        }
        //去除双向连链表中的重复菜品，并修改菜品数目
        for (int i = 0; i < Not_Order_Food.size(); i++) {
            Food f = Not_Order_Food.get(i);
            Not_Order_Food.get(i).set_food_order(true);
            for (int j = i + 1; j < Not_Order_Food.size(); j++) {
                if (f.get_food_name().equals(Not_Order_Food.get(j).get_food_name())) {
                    f.set_food_num(f.get_food_num()+1);
                    Not_Order_Food.remove(j);
                    j--;
                }
            }
        }
    }

    public void Add_Not_Order_Food(Food food){
        Not_Order_Food.add(food);
    }
    public void Add_Order_Food_List(List<Food> food){
        if(food.size() !=0) {
            this.Order_Food.addAll(food);
        }
        //去除双向连链表中的重复菜品，并修改菜品数目
        for (int i = 0; i < Order_Food.size(); i++) {
            Food f = Order_Food.get(i);
            for (int j = i + 1; j < Order_Food.size(); j++) {
                if (f.get_food_name().equals(Order_Food.get(j).get_food_name())) {
                    f.set_food_num(f.get_food_num()+1);
                    Order_Food.remove(j);
                    j--;
                }
            }
        }
    }

    public void Clear_Not_Order_Food_List(){
        this.Not_Order_Food.clear();
    }

    public void Clear_Order_Food_List(){
        this.Order_Food.clear();
    }

    public void Delet_Not_Order_Food_List(Food food){
        Iterator<Food> it = Not_Order_Food.iterator();
        while(it.hasNext()){
            Food f= it.next();
            if(f.get_food_name().equals(food.get_food_name())){
                if(f.get_food_num()>1){
                    f.set_food_num(f.get_food_num()-1);
                }
                else {
                    it.remove();
                }
                break;
            }
        }
    }

    public List<Food> Get_Not_Order_Food_List(){
        return this.Not_Order_Food;
    }

    public List<Food> Get_Order_Food_List(){
        return this.Order_Food;
    }
}
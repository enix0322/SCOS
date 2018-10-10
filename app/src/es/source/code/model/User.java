package es.source.code.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class User implements Serializable {

    private String userName;
    private String password;
    private Boolean oldUser;
    private List<Food> Not_Order_Food;
    private List<Food> Order_Food;

    public User(){
        this.userName = "temp";
        this.password = "0";
        this.Not_Order_Food = new LinkedList<>();
        this.Order_Food = new LinkedList<>();
        this.oldUser = false;
    }

    public User (String userName,String password) {
        this.userName = userName;
        this.password = password;
        this.Not_Order_Food = new LinkedList<>();
        this.Order_Food = new LinkedList<>();
        this.oldUser = false;
    }
    public String Getter_userName() {
        return userName;
    }

    public void Setter_userName(String userName) {
        this.userName = userName;
        this.Not_Order_Food = new LinkedList<>();
        this.Order_Food = new LinkedList<>();
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
        this.Not_Order_Food.addAll(food);
    }

    public void Add_Order_Food_List(List<Food> food){
        this.Order_Food.addAll(food);
    }

    public void Clear_Not_Order_Food_List(){
        this.Not_Order_Food.clear();
    }

    public void Clear_Order_Food_List(){
        this.Order_Food.clear();
    }

    public List<Food> Get_Not_Order_Food_List(){
        return this.Not_Order_Food;
    }

    public List<Food> Get_Order_Food_List(){
        return this.Order_Food;
    }
}
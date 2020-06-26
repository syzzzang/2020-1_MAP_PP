package edu.skku.map.a2017311456_pp;

import java.util.HashMap;
import java.util.Map;

public class FirebasePost {
    public String name;
    public String password;


    public String todo;


    public FirebasePost(){

    }
    public FirebasePost(String todo){
        this.todo=todo;
    }
    public FirebasePost(String name, String password){
        this.name=name;
        this.password=password;

    }

    public Map<String,Object> toMap(){
        HashMap<String,Object> result=new HashMap<>();
        result.put("name",name);
        result.put("password",password);

        return result;
    }
    public void clear(){
        name="";
        password="";

    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }


}


package edu.skku.map.a2017311456_pp;

public class Posts {
    private String check;
    private String todo;
    private String name;
    private String day;

    public Posts(){
    }
    public Posts(String todo,String check,String name,String day){
        this.check=check;
        this.todo=todo;
        this.name=name;
        this.day=day;
    }
    public String getCheck() {
        return check;
    }
    public void setCheck(String check) {
        this.check = check;
    }
    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getName() {
        return name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setName(String name) {
        this.name = name;
    }
}

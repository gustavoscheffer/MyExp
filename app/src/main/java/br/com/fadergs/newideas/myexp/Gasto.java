package br.com.fadergs.newideas.myexp;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


// Classe que representa o objeto Gasto
public class Gasto {

    private String name;
    private double value;
    private String date;
    private String categoria;

    // Construtor default
    public Gasto(){

    }

    //Construtor full
    public Gasto(String name, double value, String date, String categoria) {
        this.name = name;
        this.value = value;
        this.date = date;
        this.categoria = categoria;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("nome", name);
        result.put("valor", value);
        result.put("data", date);
        result.put("categoria", categoria);

        return result;
    }
}

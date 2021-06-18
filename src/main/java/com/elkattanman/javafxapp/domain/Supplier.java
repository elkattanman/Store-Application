package com.elkattanman.javafxapp.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id ;
    private  String Name ;
    private  String email ;
    private  String phone ;
    private  String company;
    private  String city ;
    private  double totalDeptIn;

    public void addDept(double val){
        totalDeptIn+=val;
    }

    public void subDept(double val) throws Exception {
        if(val>totalDeptIn) throw new Exception("Value greater than current dept");
        totalDeptIn-=val;
    }
}

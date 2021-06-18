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
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id ;
    private  String Name ;
    private  String email ;
    private  String phone ;
    private  String city ;

    private  double totalDeptOut;

    public void addDept(double val){
        totalDeptOut +=val;
    }

    public void subDept(double val) throws Exception {
        if(val> totalDeptOut) throw new Exception("Value greater than current dept");
        totalDeptOut -=val;
    }
}

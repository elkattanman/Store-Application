package com.elkattanman.javafxapp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeptOut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    @ManyToOne
    private Customer customer;
    private double dept;
    private LocalDate date;

    public void addDept(double val){
        customer.addDept(val);
        dept+=val;
    }

    public void subDept(double val) throws Exception {
        customer.subDept(val);
        if(val>dept) throw new Exception("Value greater than current dept");
        dept-=val;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.finalproject2;

/**
 *
 * @author aaravsharma
 * @author nerubaya
 */
import java.util.*;


import java.util.ArrayList;

public class Agent {

    private int num;
    public int k;

    public boolean isAlive = true;
    public boolean isCooperator = true;

    public ArrayList<Agent> neighbors = new ArrayList<>();

    public double R;   // Big R: total payoff in a round

    public Agent(int num, double R) {
        this.num = num;
        this.R = R;
    }

    public int getNum() {
        return num;
    }

    public void addNeighbor(Agent a) {
        neighbors.add(a);
        k = neighbors.size();
    }
}

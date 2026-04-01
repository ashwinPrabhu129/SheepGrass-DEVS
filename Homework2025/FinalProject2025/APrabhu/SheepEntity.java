package Homework2025.FinalProject2025.APrabhu;

import GenCol.entity;

public class SheepEntity extends entity {
    public double toDieTime;
    public double toReproduceTime;

    public SheepEntity(String id, double dieT, double breedT) {
        super(id);
        this.toDieTime = dieT;
        this.toReproduceTime = breedT;
    }
}
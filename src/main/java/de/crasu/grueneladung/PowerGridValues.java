package de.crasu.grueneladung;

public class PowerGridValues {
    private Integer gas;
    private Integer coal;
    private Integer nuclear;

    PowerGridValues(Integer gas, Integer coal, Integer nuclear) {
        this.gas = gas;
        this.coal = coal;
        this.nuclear = nuclear;
    }
    
    public Integer getGas() {
        return gas;
    }
    
    public Integer getCoal() {
        return coal;
    }
    
    public Integer getNuclear() {
        return nuclear;
    }

    public Integer getOverallPower() {        
        return coal + gas + nuclear;
    }
    
}

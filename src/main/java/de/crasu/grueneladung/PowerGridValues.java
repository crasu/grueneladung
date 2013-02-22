package de.crasu.grueneladung;

public class PowerGridValues {
    private Integer gas;
    private Integer coal;
    private Integer nuclear;   
    
    public Integer getGas() {
        return gas;
    }
    
    public void setGas(Integer gas) {
        this.gas = gas;
    }
    
    public Integer getCoal() {
        return coal;
    }
    
    public void setCoal(Integer coal) {
        this.coal = coal;
    }
    
    public Integer getNuclear() {
        return nuclear;
    }
    
    public void setNuclear(Integer nuclear) {
        this.nuclear = nuclear;
    }

    public Integer getOverallPower() {        
        return coal + gas + nuclear;
    }
    
}

package de.crasu.grueneladung;

public class PowerGridValuesBuilder {
    private Integer gas = 1;
    private Integer coal = 1;
    private Integer nuclear = 1;

    public PowerGridValuesBuilder withGas(Integer gas) {
        this.gas = gas;
        return this;
    }

    public PowerGridValuesBuilder withCoal(Integer coal) {
        this.coal = coal;
        return this;
    }

    public PowerGridValuesBuilder withNuclear(Integer nuclear) {
        this.nuclear = nuclear;
        return this;
    }

    public PowerGridValues build() {
        return new PowerGridValues(gas, coal, nuclear);
    }
}
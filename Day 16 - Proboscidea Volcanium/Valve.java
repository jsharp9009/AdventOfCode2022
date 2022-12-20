public class Valve {
    Valve[] leadsTo;
    public Valve[] getLeadsTo() {
        return leadsTo;
    }
    public void setLeadsTo(Valve[] leadsTo) {
        this.leadsTo = leadsTo;
    }
    String valveName;
    public String getValveName() {
        return valveName;
    }
    Integer flowRate;
    public Integer getFlowRate() {
        return flowRate;
    }
    public Valve(String valveName, Integer flowRate) {
        this.valveName = valveName;
        this.flowRate = flowRate;
    }
}

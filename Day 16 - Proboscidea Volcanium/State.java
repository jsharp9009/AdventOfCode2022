public class State {
    Integer Minute;
    Integer ValveState;
    Valve CurrentValve;

    public State(Integer minute, Integer valveState, Valve currentValve) {
        this.Minute = minute;
        this.ValveState = valveState;
        this.CurrentValve = currentValve;
    }

    @Override  
    public String toString(){
        return Minute + " " + ValveState + " " + CurrentValve.valveName;
    }

    @Override
    public int hashCode(){
        var hash = 1;
        if(Minute != 0)
            hash = 23  * hash + Minute;
        if(ValveState != 0)
            hash = 29  * hash + ValveState;
        hash = 31  * hash + CurrentValve.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof State))
            return false;
        var compareTo = (State)obj;
        return Minute == compareTo.Minute
            //&& ValveState == compareTo.ValveState
            && CurrentValve.valveName.equals(compareTo.CurrentValve.valveName);
    }
}

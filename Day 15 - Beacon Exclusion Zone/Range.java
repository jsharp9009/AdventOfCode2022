public class Range
{
    public int low;
    public int high;

    public Range(int low, int high){
        this.low = low;
        this.high = high;
    }

    public boolean contains(int number){
        return (number >= low && number <= high);
    }

    public boolean Merge(Range e){
        if(e.high >= this.high && e.low <= this.low){
            this.high = e.high;
            this.low = e.low;
            return true;
        }
        if(this.high >= e.high && this.low <= e.low){
            return true;
        }

        if(this.high >= e.low && this.high < e.high){
            this.high = e.high;
            return true;
        }

        if (this.low <= e.high && e.low < this.low){
            this.low = e.low;
            return true;
        }
        return false;
    }
}
public class pFile{
    public pFile(String name, Integer size) {
        this.name = name;
        this.size = size;
    }

    String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    Integer size;
    public Integer getSize() {
        return size;
    }
    public void setSize(Integer size) {
        this.size = size;
    }
}
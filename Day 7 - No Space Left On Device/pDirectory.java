import java.util.ArrayList;

public class pDirectory{

    public pDirectory(pDirectory parent, String name) {
        this.parent = parent;
        this.Children = new ArrayList<pDirectory>();
        this.files = new ArrayList<pFile>();
        this.name = name;
    }

    String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    ArrayList<pFile> files;
    public ArrayList<pFile> getFiles() {
        return files;
    }
    public void setFiles(ArrayList<pFile> files) {
        this.files = files;
    }

    ArrayList<pDirectory> Children;
    public ArrayList<pDirectory> getChildren() {
        return Children;
    }
    public void setChildren(ArrayList<pDirectory> children) {
        Children = children;
    }

    pDirectory parent;
    public pDirectory getParent() {
        return parent;
    }
    public void setParent(pDirectory parent) {
        this.parent = parent;
    }
}
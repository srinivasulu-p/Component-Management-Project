import java.util.List;
import java.util.ArrayList;

public class Components {                                                   //Components holds the structure of Components
    private String name;
    private boolean installed = false;                                      //Installation status to perform installation and removal operations
    private boolean explicit = false;                                       //It stores type of installation to perform remove operations
    private List<Components> dependencies = new ArrayList<Components>();    //It stores dependencies that needs to installed my installing this component...and Remove these dependencies after removal of these component(if possible)
    private List<Components> dependents = new ArrayList<Components>();      // It contains all the components that are using these component to run successfully.

    /*
      Here are all the setter and getters methods for the instance variables.
    */
    
    public Components(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void isInstalled(boolean in){
        this.installed = in;
    }
    public boolean getIsInstalled(){
        return this.installed;
    }
    public void isExplicitly(boolean in){
        this.explicit = in;
    }
    public boolean getIsExplicitly(){
        return this.explicit;
    }
    public void addDependencies(Components in){
        this.dependencies.add(in);
    }
    public List<Components> getDependencies(){
        return this.dependencies;
    }
    public void setDependents(Components in){
        this.dependents.add(in);
    }
    public List<Components> getDependents(){
        return this.dependents;
    }
}

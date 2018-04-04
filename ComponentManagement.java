import java.util.HashMap;
import java.util.List;
import java.util.Set;

/*ComponentManagement class holds the implementation of all commands.*/

public class ComponentManagement
{

    /* "ComponentManagement" structure holds the <K,V> of type <String,Component>
     * (K) String Here --- Name of Component
     * (V) Component Here - It holds all the information happened with  component, whether it has dependencies,installed, how it is installed. removed,what are all components dependent on this, etc.
     * */
     
    static HashMap<String,Components> componentManagement = new HashMap<String,Components>();


    /* "performDepend" methods works in considers following conditions while storing information in "componentManagement" structure
     *   1. For a Depend command with components[], it checks in "componentManagement" to validate this depend command would be correct or wrong
     *   2. It Creates new component object and stores all its dependencies in its object and performs below steps
     *   3. If existing component is given as input, it updates the information by adding new dependencies. And it adds this component as dependent to its dependencies.
     *   4. This information about dependencies and dependents in componentManagement can be used to perform Install and Remove.
    * */
    
    
    public void performDepend(String[] dependenciesInfo)
    {
        int dependenciesInfoLength = dependenciesInfo.length;
        if(!componentManagement.containsKey(dependenciesInfo[1]))
        {
            Components mainComp = new Components(dependenciesInfo[1]);
            componentManagement.put(dependenciesInfo[1],mainComp);
        }
        for (int i=2;i<dependenciesInfoLength;i++)
        {
            if(componentManagement.containsKey(dependenciesInfo[i]))
            {
                List<Components> validateList = componentManagement.get(dependenciesInfo[i]).getDependencies();
                if(validateDepend(validateList,dependenciesInfo[1],dependenciesInfo[i])) {
                    componentManagement.get(dependenciesInfo[1]).addDependencies(componentManagement.get(dependenciesInfo[i]));
                    componentManagement.get(dependenciesInfo[i]).setDependents(componentManagement.get(dependenciesInfo[1]));
                }
            }
            else
            {
                Components dependencyComponent = new Components(dependenciesInfo[i]);
                componentManagement.put(dependenciesInfo[i],dependencyComponent);
                componentManagement.get(dependenciesInfo[1]).addDependencies(componentManagement.get(dependenciesInfo[i]));
                componentManagement.get(dependenciesInfo[i]).setDependents(componentManagement.get(dependenciesInfo[1]));
            }
        }
    }
    
    /*
        Utility method for performDepend method to validate the depend command
    */
    
    private boolean validateDepend(List<Components> validate,String one,String two){
        for(Components components: validate )
        {
            if(components.getName().equals(one))
            {
                System.out.println(two + " depends on " + components.getName() + ", ignoring command");
                return false;
            }
        }
        return true;
    }
    
    /*
    * "performInstall" implements installation process by considering following steps.
    * (keyComponent -- component to be installed , superComponent -- dependencies of keyComponent)
    *   1. If the given keyComponent is not available in "componentManagement" structure it creates new Component and stores this info in "componentManagement"
    *   2. If Component is already installed it displays appropriate message to console.
    *   3. If not, it installs keyComponent along with Dependencies if needed. This info about dependencies is from "componentManagement" structure.
    * */
    
    public void performInstall(String installComponent) {
        if(!componentManagement.containsKey(installComponent)) {
            Components newComponent = new Components(installComponent);
            newComponent.isExplicitly(true);
            newComponent.isInstalled(true);
            componentManagement.put(installComponent,newComponent);
            System.out.println("Installing " +installComponent);
        }
        else {
            Components toBeInstalled = componentManagement.get(installComponent);
            if((toBeInstalled.getIsInstalled())) {
                System.out.println(installComponent +" is already installed");
            }
            else {
                toBeInstalled.isInstalled(true);
                toBeInstalled.isExplicitly(true);
                List<Components> dependencyList = toBeInstalled.getDependencies();
                performInstallUtil(dependencyList);
                System.out.println("Installing " +toBeInstalled.getName());
            }
        }
    }
    public void performInstallUtil(List<Components> dependencyComponents){
        if(dependencyComponents.isEmpty()){
            return;
        }
        for(Components components: dependencyComponents){
            if(!components.getIsInstalled()){
                List<Components> superComponents = components.getDependencies();
                performInstallUtil(superComponents);
                components.isInstalled(true);
                components.isExplicitly(false);
                System.out.println("Installing " +components.getName());
            }

        }
    }

    /*
    * "performRemove" method implements remove implementation by considering following conditions
    *   (keyComponent -- component to be installed , superComponents -- dependencies of keyComponent, subComponents ---dependents on keyComponent)
    *   1. Checks whether given keyComponent is installed or not
    *   2. If installed ---does it have subComponents installed --- If subComponents are installed --Don't remove
    *   3. If Removed--- Check whether superComponents can be removed --and checks up the hierarchy
    * */
    
    public void performRemove(String removeComponent)
    {
        if(!(componentManagement.containsKey(removeComponent)) || !(componentManagement.get(removeComponent).getIsInstalled()))
        {
            System.out.println(removeComponent + " is not installed");
        }
        else
        {
            Components removal = componentManagement.get(removeComponent);
            List<Components> subComponentsRemoval = removal.getDependents();
            List<Components> superComponentsRemoval = removal.getDependencies();
            if(subComponentsRemoval.isEmpty() || canBeRemoved(subComponentsRemoval))
            {
                removal.isInstalled(false);
                System.out.println("Removing " +removal.getName());
                performRemoveUtil(superComponentsRemoval);
            }
            else
            {
                System.out.println(removal.getName() + " is still needed");
            }
        }
    }
    
    /*
        Utility Method for "performRemove", it is called to remove superComponents of keyComponent
    */
    
    public void performRemoveUtil(List<Components> superRemoval)
    {
        for(Components superComponent: superRemoval)
        {
            if(!superComponent.getIsExplicitly() && superComponent.getIsInstalled()) {
                List<Components> superComponentDependentsList = superComponent.getDependents();
                if (superComponentDependentsList.isEmpty() || canBeRemoved(superComponentDependentsList)) {
                    superComponent.isInstalled(false);
                    System.out.println("Removing " + superComponent.getName());
                    List<Components> parentComponents = superComponent.getDependencies();
                    performRemoveUtil(parentComponents);
                }
            }
        }
        return;
    }
    
    /*
        Another utility method for remove implementation. This methods checks th given list of components are installed or not and returns boolean.
    */
    
    public boolean canBeRemoved(List<Components> toBeRemoved)
    {
        for(Components toBeRemovedComponents: toBeRemoved){
            if(toBeRemovedComponents.getIsInstalled()){
                return false;
            }
        }
        return true;
    }
    
    /*
       This performList prints all the keys in "ComponentManagement" structure which are currently installed.
    */
    
    public void performList(){
        for(String key:componentManagement.keySet())
        {
            if (componentManagement.get(key).getIsInstalled())
                System.out.println(key);
        }
    }
}

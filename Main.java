import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        while (true) {
            String line = scan.nextLine();

            if (line == null || line.length() == 0) {
                continue;
            }
            if(line.length() > 80){
                System.out.println("Too Lengthy Input");
                continue;
            }

            if ("END".equals(line)) {
                System.out.println("END");
                break;
            }
            /*
              Breaking down the input command into words to check for DEPEND/INSTALL/REMOVE/LIST
            */
            String[] words = line.split("\\s");
            ComponentManagement pm = new ComponentManagement();       //ComponentManagement class can perform all the options like DEPEND/INSTALL/REMOVE/LIST
            if(words[0].equals("DEPEND")){
                if(words.length<3){
                   System.out.println("Invalid DEPEND command");
                }else {
                    pm.performDepend(words);                          //Array of strings in DEPEND command are sent to performDepend
                }
            }
            if(words[0].equals("INSTALL")){
                if(words.length<2){
                    System.out.println("Invalid INSTALL command");
                }else{
                    pm.performInstall(words[1]);                     //Name of Component is sent to perform Installation
                }
            }
            if(words[0].equals("REMOVE")){
                if(words.length<2){
                    System.out.println("Invalid REMOVE command");
                }else{
                    pm.performRemove(words[1]);                     // Name of component is sent to perform Removal
                }
            }
            if(words[0].equals("LIST")){                            //List of currently installed Components are executed in performList
                pm.performList();
            }
        }
    }
}

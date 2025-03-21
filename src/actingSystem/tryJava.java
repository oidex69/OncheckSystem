package actingSystem;

import java.util.*;

public class tryJava extends LinkedNode  {
    double hello[] = new double[5] ;
    public static void main(String[] args) {
        @SuppressWarnings("resource")
        Scanner scan = new Scanner(System.in);
        tryJava object = new tryJava();
        for(int i=0;i<5;i++){
            System.out.println("Enter number");
            object.hello[i]= scan.nextDouble();
        }

        for (int i=0;i<5;i++){
            System.out.println(object.hello[i]);
        }
    }
}

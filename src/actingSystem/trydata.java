package actingSystem;
import java.util.Scanner;

public class trydata {
    String[] data; 

    // Method to initialize the array with a given count
    void initial(int count) {
        data = new String[count];
    }

    public static void main(String[] args) {
        trydata object = new trydata();
        Scanner scan = new Scanner(System.in);

        // Get the size of the array from the user
        System.out.print("Enter the number of strings: ");
        int count = scan.nextInt();
        scan.nextLine(); // Consume the newline character

        object.initial(count); // Initialize the array

        int str = object.data.length;
        // Populate the array with user input
        System.out.println("Enter " + str + " strings:");
        for (int i = 0; i < count; i++) {
            System.out.print("String " + (i + 1) + ": ");
            object.data[i] = scan.nextLine(); // Store user input in the array
        }

        // Step 3: Print the contents of the array
        System.out.println("You entered:");
        for (String s : object.data) {
            System.out.println(s); // Print each string in the array
        }

        scan.close(); // Close the scanner
    }
}

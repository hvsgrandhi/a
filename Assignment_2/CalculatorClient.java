import CalculatorApp.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import java.util.Scanner;


public class CalculatorClient {
    public static void main(String[] args) {
        try {
            // Create and initialize the ORB
            ORB orb = ORB.init(args, null);

            // Get the Naming Service
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Resolve the Calculator object reference
            Calculator calcRef = CalculatorHelper.narrow(ncRef.resolve_str("Calculator"));

            Scanner sc = new Scanner(System.in);
            System.out.print("Enter first number: ");
            float a = sc.nextFloat();
            System.out.print("Enter second number: ");
            float b = sc.nextFloat();

            System.out.println("Addition: " + calcRef.add(a, b));
            System.out.println("Subtraction: " + calcRef.subtract(a, b));
            System.out.println("Multiplication: " + calcRef.multiply(a, b));
            System.out.println("Division: " + calcRef.divide(a, b));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

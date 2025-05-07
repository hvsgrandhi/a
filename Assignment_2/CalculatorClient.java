import CalculatorApp.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;

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

            // Call remote methods
            System.out.println("Addition: " + calcRef.add(10, 5));
            System.out.println("Subtraction: " + calcRef.subtract(10, 5));
            System.out.println("Multiplication: " + calcRef.multiply(10, 5));
            System.out.println("Division: " + calcRef.divide(10, 5));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

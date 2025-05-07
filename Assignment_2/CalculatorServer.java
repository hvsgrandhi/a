import CalculatorApp.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.CosNaming.*;

public class CalculatorServer {
    public static void main(String[] args) {
        try {
            // Create and initialize the ORB
            ORB orb = ORB.init(args, null);

            // Get reference to rootpoa & activate POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // Create the servant
            CalculatorImpl calcImpl = new CalculatorImpl();

            // Get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(calcImpl);
            Calculator href = CalculatorHelper.narrow(ref);

            // Get the naming service
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Bind the object reference to the name "Calculator"
            NameComponent path[] = ncRef.to_name("Calculator");
            ncRef.rebind(path, href);

            System.out.println("Calculator Server ready and waiting...");

            // Wait for invocations from clients
            orb.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

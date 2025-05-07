import CalculatorApp.*;

public class CalculatorImpl extends CalculatorPOA {
    public float add(float a, float b) {
        System.out.println("[Server] Client requested addition: " + a + " + " + b);
        float result = a + b;
        System.out.println("[Server] Addition completed. Result: " + result);
        return result;
    }

    public float subtract(float a, float b) {
        System.out.println("[Server] Client requested subtraction: " + a + " - " + b);
        float result = a - b;
        System.out.println("[Server] Subtraction completed. Result: " + result);
        return result;
    }

    public float multiply(float a, float b) {
        System.out.println("[Server] Client requested multiplication: " + a + " * " + b);
        float result = a * b;
        System.out.println("[Server] Multiplication completed. Result: " + result);
        return result;
    }

    public float divide(float a, float b) {
        System.out.println("[Server] Client requested division: " + a + " / " + b);
        if (b == 0) {
            System.out.println("[Server] Error: Division by zero attempted.");
            return 0;
        }
        float result = a / b;
        System.out.println("[Server] Division completed. Result: " + result);
        return result;
    }
}

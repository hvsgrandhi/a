import CalculatorApp.*;

public class CalculatorImpl extends CalculatorPOA {
    public float add(float a, float b) {
        return a + b;
    }
    public float subtract(float a, float b) {
        return a - b;
    }
    public float multiply(float a, float b) {
        return a * b;
    }
    public float divide(float a, float b) {
        if (b == 0) return 0;
        return a / b;
    }
}

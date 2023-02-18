import java.util.InputMismatchException;

public class Node {

    Numeral first;
    Operand operand;
    Numeral second;

    public Node(String expression) {
       if (expression.matches("\\d[*/^+-]\\d")) {

           String operand = expression.replaceAll("\\p{Alnum}", "");
           operand = operand.replaceAll("[.]", "");
           if (operand.length() == 1)
               this.operand = Operand.valueOfOperand(operand.charAt(0));
           else throw new InputMismatchException("Wrong operands");

           String[] numerals = expression.split("[*/^+-]");
           this.first = new Numeral(numerals[0]);
           this.second = new Numeral(numerals[1]);
       }

    }
}



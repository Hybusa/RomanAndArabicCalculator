import java.util.InputMismatchException;
import java.util.Scanner;

public class Calculator {
    public static void calculate() throws NegativeRomanException {

        Numeral result;
        while (true) {
            System.out.println("Enter a math question in Roman OR Arabic numbers.");
            Scanner input = new Scanner(System.in);
             if (input.hasNextLine()) {
                try {
                    StateMachineParser parser = new StateMachineParser(input.nextLine());
                    result =  parser.calculate();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            } else {
                System.out.println("No input");
            }
        }
        if(result.value < 0 && result.type == TypeOfNumbers.ROMAN)
            throw new NegativeRomanException("Roman numbers can't be negative");
        System.out.println("Result is: " + ConsoleColors.GREEN_BOLD + ConsoleColors.GREEN_UNDERLINED
                + result
                + ConsoleColors.RESET);
    }
}

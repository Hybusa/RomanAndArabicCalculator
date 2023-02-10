import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        String result;
        while (true) {
            System.out.println("Enter a math question in Roman OR Arabic numbers.");
            Scanner input = new Scanner(System.in);
            if (input.hasNextLine()) {
                try {
                    result = calc(input.nextLine());
                    break;
                } catch (InputMismatchException | NegativeRomanException e) {
                    System.out.println(e.getLocalizedMessage());
                }
            } else {
                System.out.println("No input");
            }
        }
        System.out.println(result);
    }


    public static String calc(String input) throws NegativeRomanException {
        FullExpressionParser Expression = new FullExpressionParser(input);
        return Expression.calculate();
    }

}
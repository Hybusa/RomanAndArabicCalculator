import java.util.InputMismatchException;
import java.util.Scanner;

public class Calculator {
    float firstNumber;
    float secondNumber;
    char operand;
    TypeOfNumbers typeOfNumbers;

    Calculator(String question) {

        String operand = question.replaceAll("\\p{Alnum}", "");
        operand = operand.replaceAll("[.]", "");
        this.operand = getOperandFromString(operand);

        if (operand.isEmpty())
            throw new InputMismatchException("Wrong input");

        String[] numerals = question.split("[*/^+-]");
        float[] numbers = getNumberFromString(numerals);

        if (numerals.length < 3) {
            this.firstNumber = numbers[0];
            this.secondNumber = numbers[1];
        } else {
            throw new InputMismatchException("Wrong input");
        }

        System.out.println("Your question is " + this.firstNumber + this.operand + this.secondNumber);
    }

    public static void calculator() throws NegativeRomanException {

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
    }




    private float[] getNumberFromString(String[] numbers) {
        if (numbers[0].isEmpty() || numbers[1].isEmpty())
            throw new InputMismatchException("Wrong question format");

        float[] result = new float[2];

        if (numbers[0].matches("[.0-9]+") && numbers[1].matches("[.0-9]+")) {
            result[0] = Float.parseFloat(numbers[0]);
            result[1] = Float.parseFloat(numbers[1]);
            this.typeOfNumbers = TypeOfNumbers.ARABIC;

        } else if (numbers[0].matches("[IVXLDM]+") && numbers[1].matches("[IVXLDM]+")) {
            result[0] = RomanNumerals.getNumberFromRomanNumeral(numbers[0]);
            result[1] = RomanNumerals.getNumberFromRomanNumeral(numbers[1]);
            this.typeOfNumbers = TypeOfNumbers.ROMAN;

        } else throw new InputMismatchException("Numbers have to be of the same format");
        return result;
    }

    private char getOperandFromString(String operand) {
        if (operand.length() == 1 && operand.matches("[*+/^-]")) {
            return operand.charAt(0);
        } else {
            throw new InputMismatchException("Wrong operand input");
        }
    }


    public static Numeral calculateSimpleExpression(Numeral first, Operand operand, Numeral last)
    {
        if(first.type != last.type)
            throw new InputMismatchException("Numbers have to be of the same format");
        System.out.println("Simple expression: " + first + operand + last);
        Numeral result = new Numeral();
        result.type = first.type;
        switch (operand)
        {
            case POW:
                result.value = Math.pow(first.value, last.value);
                break;
            case MULTIPLICATION:
                result.value = first.value * last.value;
                break;
            case DIVISION:
                result.value = first.value/ last.value;
                break;
            case SUBTRACTION:
                result.value = first.value - last.value;
                break;
            case ADDITION:
                result.value = first.value + last.value;
                break;
            default:
                throw  new InputMismatchException("Something went terribly wrong and I am sorry for that.");
        }
        return result;
    }
    public String calculate() throws NegativeRomanException {
        float result = 0;
        switch (this.operand) {
            case '*':
                result = this.firstNumber * this.secondNumber;
                break;
            case '/':
                result = this.firstNumber / this.secondNumber;
                break;
            case '+':
                result = this.firstNumber + this.secondNumber;
                break;
            case '-':
                result = this.firstNumber - this.secondNumber;
                break;
            case '^':
                result = (int)Math.pow(this.firstNumber,this.secondNumber);
                break;
            default:
                break;
        }
        if (result < 0 && this.typeOfNumbers.equals(TypeOfNumbers.ROMAN))
            throw new NegativeRomanException("Roman number cant be less then zero");
        else
            return String.valueOf(result);
    }
}

import java.util.*;

public class StateMachineParser {

    Stack<Object> stack = new Stack<>();
    String expression;
    ParserState state = ParserState.BEGINNING;
    OperandState previousOperandState = OperandState.DUMMY;
    BracketState bracketState = BracketState.CLOSED;

    StateMachineParser(String expression) {

        this.expression = expression;
        checkAndTrimTheExpression();

        if (!this.expression.matches("[*/^+IVCXLDM()0-9-.]+"))
            throw new InputMismatchException("Wrong input");
        if (expression.charAt(0) == '-' || expression.charAt(0) == '+')
            stack.push(new Numeral("0"));

        System.out.println("Your expression is " + ConsoleColors.PURPLE_BRIGHT
                + this.expression
                + ConsoleColors.RESET);
    }

    private void checkAndTrimTheExpression() {
        expression = expression.trim();
        expression = expression.replaceAll("\\p{C}", "?");
        expression = expression.replace(" ", "");
        if (expression.length() < 1)
            throw new InputMismatchException("Wrong Input: Empty String");
        expression = expression.toUpperCase();
        if (expression.substring(0, 1).matches("[*/^+]"))
            throw new InputMismatchException("Wrong expression beginning");
        if (!checkBracketsMatch())
            throw new InputMismatchException("Wrong brackets");
    }

    private Numeral composeAndCalculateSimpleExpression() {
        return calculateSimpleExpression((Numeral) stack.pop()
                , (Operand) stack.pop(), (Numeral) stack.pop());
    }

    public Numeral calculate() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            final char lookup = expression.charAt(i);

            state = checkState(lookup, state);

            switch (state) {

                case BRACKET_OPENING:

                    bracketState = BracketState.OPENED;
                    ParserState inBracketState = ParserState.BEGINNING;
                    StringBuilder bracketBuilder = new StringBuilder();
                    bracketState.counter++;
                    while (bracketState.counter > 0) {
                        final char inBracketLookup = expression.charAt(++i);
                        inBracketState = checkState(inBracketLookup, inBracketState);
                        switch (inBracketState) {
                            case BRACKET_OPENING:
                                bracketState.counter++;
                                break;
                            case BRACKET_CLOSING:
                                bracketState.counter--;
                                break;
                            case ERROR:
                                throw new RuntimeException("Something Went Terribly Wrong. I'm sorry");
                            default:
                                break;
                        }
                        bracketBuilder.append(inBracketLookup);
                    }
                    bracketBuilder.deleteCharAt(bracketBuilder.length() - 1);
                    StateMachineParser inBracketParser = new StateMachineParser(bracketBuilder.toString());
                    stack.push(inBracketParser.calculate());
                    bracketState = BracketState.CLOSED;
                    break;
                case IN_NUMERAL:
                    sb.append(lookup);
                    break;
                case OPERAND:
                    if (sb.length() > 0)
                        stack.push(new Numeral(sb.toString()));
                    sb.setLength(0);
                    Operand operand = Operand.valueOfOperand(lookup);
                    if (operand.operandState.intValue >= previousOperandState.intValue)
                        stack.push(composeAndCalculateSimpleExpression());

                    previousOperandState = operand.operandState;
                    stack.push(operand);
                    break;
                case ERROR:
                default:
                    throw new RuntimeException("Something Went Terribly Wrong. I'm sorry");
            }
        }
        if (bracketState.counter != 0)
            throw new InputMismatchException("Wrong Amount Of Brackets");

        if (sb.length() > 0)
            stack.push(new Numeral(sb.toString()));
        while (stack.size() > 1) {
            stack.push(composeAndCalculateSimpleExpression());
        }
        return (Numeral) stack.pop();
    }

    private boolean checkBracketsMatch() {
        int pos = 0;
        int leftBracketCounter = 0;
        int rightBracketCounter = 0;
        while (true) {
            pos = expression.indexOf('(', pos);
            if (pos >= 0) {
                leftBracketCounter++;
                pos++;
            } else
                break;
        }
        pos = 0;
        while (true) {
            pos = expression.indexOf(')', pos);
            if (pos >= 0) {
                rightBracketCounter++;
                pos++;
            } else
                break;
        }

        return leftBracketCounter == rightBracketCounter;
    }

    private ParserState checkState(char lookup, ParserState currentState) {
        String string = String.valueOf(lookup);
        if (string.matches("[IVXCLDM0-9.]"))
            return ParserState.IN_NUMERAL;
        if (string.matches("[*/^+-]"))
            return ParserState.OPERAND;
        if (string.matches("[)]") && currentState == ParserState.OPERAND)
            return ParserState.ERROR;
        if (string.matches("\\("))
            return ParserState.BRACKET_OPENING;
        if (string.matches("\\)"))
            return ParserState.BRACKET_CLOSING;

        return currentState;
    }

    private Numeral calculateSimpleExpression(Numeral last, Operand operand, Numeral first) {
        if (first.type != last.type)
            throw new InputMismatchException("Numbers have to be of the same format");
        System.out.println("Simple expression: " + ConsoleColors.CYAN_BRIGHT
                + first + operand + last
                + ConsoleColors.RESET);
        Numeral result = new Numeral();
        result.type = first.type;
        switch (operand) {
            case POW:
                result.value = Math.pow(first.value, last.value);
                break;
            case MULTIPLICATION:
                result.value = first.value * last.value;
                break;
            case DIVISION:
                result.value = first.value / last.value;
                break;
            case SUBTRACTION:
                result.value = first.value - last.value;
                break;
            case ADDITION:
                result.value = first.value + last.value;
                break;
            default:
                throw new InputMismatchException("Something went terribly wrong and I am sorry for that.");
        }
        return result;
    }
}

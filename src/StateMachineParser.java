
import java.util.InputMismatchException;

import java.util.Stack;

public class StateMachineParser {
    Stack<Object> stack = new Stack<>();

    String expression;
    ParserState state = ParserState.BEGINNING;
    ParserState previousState;
    OperandState previousOperandState = OperandState.DUMMY;
    BracketState bracketState = BracketState.CLOSED;

    StateMachineParser(String expression) {

        this.expression = expression;
        checkAndTrimTheExpression();

        if (!this.expression.matches("[*/^+IVXLDM()0-9-]+"))
            throw new InputMismatchException("Wrong input");

        System.out.println("Your expression is " + this.expression);
    }

    private void checkAndTrimTheExpression() {
        expression = expression.trim();
        expression = expression.replace(" ", "");
        expression = expression.toUpperCase();
        if (expression.substring(0, 1).matches("[*/^+]"))
            throw new InputMismatchException("Wrong expression beginning");
        if (!checkBracketsMatch())
            throw new InputMismatchException("Wrong brackets");
    }

    private Numeral composeAndCalculateSimpleExpression() {
        Numeral simpleExpressionLast = (Numeral) stack.pop();
        Operand simpleExpressionOperand = (Operand) stack.pop();
        Numeral simpleExpressionFirst = (Numeral) stack.pop();
        return Calculator.calculateSimpleExpression(simpleExpressionFirst
                , simpleExpressionOperand, simpleExpressionLast);
    }

    public Numeral calculate() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            final char lookup = expression.charAt(i);
            ParserState currentState = checkState(lookup, state);
            if (state != currentState) {
                previousState = state;
                state = currentState;
            }


            switch (state) {

                case BRACKET_OPENING:

                    int j = expression.lastIndexOf(')');
                    StateMachineParser parser = new StateMachineParser(expression.substring(i+1, j));
                    i = j;
                    stack.push(parser.calculate());

                    break;
                case IN_NUMERAL:
                    sb.append(lookup);
                    break;
                case OPERAND:
                    if(sb.length() > 0)
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
        if (bracketState.counter != 0) {
            throw new InputMismatchException("Wrong Amount Of Brackets");
        }

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
        if (string.matches("[IVXLDM0-9]"))
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
}
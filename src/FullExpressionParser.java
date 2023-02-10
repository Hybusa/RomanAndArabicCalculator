import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;

public class FullExpressionParser {
    List<String> expressions = new ArrayList<>();

    FullExpressionParser(String expression) throws NegativeRomanException {
        expression = expression.trim();
        expression = expression.replace(" ", "");
        expression = expression.toUpperCase();

        expression = calculateAndRemoveBrackets(expression);
        String[] numeralsArr = expression.split("\\p{Punct}");
        String operands = expression.replaceAll("\\p{Alnum}","");
        char[] operandsArr = operands.toCharArray();


        System.out.println(Arrays.toString(numeralsArr));
        System.out.println(Arrays.toString(operandsArr));

        for(int i =0 ; i < numeralsArr.length;i++)
        {
            expressions.add(numeralsArr[i]);
            if(i < operandsArr.length)
                expressions.add(String.valueOf(operandsArr[i]));
        }

    }


    public String calculate() throws NegativeRomanException {
        String[] cases ={"^","*","+","-"};

       while(expressions.size()>1)
       {
           int i;
           for (i=0; i < cases.length; i++)
           {
               if(expressions.contains(cases[i])) break;
           }
           String operand = cases[i];
           while(expressions.contains(operand)) {
               int pos = expressions.indexOf(operand);

               Calculator calc = new Calculator(expressions.get(pos - 1)
                       + expressions.get(pos) + expressions.get(pos + 1));

               expressions.remove(pos - 1);
               pos = expressions.indexOf(operand);
               expressions.remove(pos+1);
               pos = expressions.indexOf(operand);
               expressions.set(pos, calc.calculate());
           }

       }
        return RomanNumerals.getRomanNumeralFromNumber(Integer.parseInt(expressions.get(0)))
                + " (" + expressions.get(0) + ")";
    }

    private String calculateAndRemoveBrackets(String expression) throws NegativeRomanException {
        while (expression.contains("(")) {
            if (!checkBracketsMatch(expression)) {
                throw new InputMismatchException("Wrong amount of brackets");
            }

            int bracketsBegin = expression.indexOf('(');
            int bracketsEnd = expression.indexOf(')');

            Calculator calc = new Calculator(expression.substring(bracketsBegin+1, bracketsEnd));

            expression = expression.replace(expression.substring(bracketsBegin, bracketsEnd+1),  calc.calculate());
        }
        return expression;

    }

    private boolean checkBracketsMatch(String expression) {
        int pos = 0;
        int leftBracketCounter = 0;
        int rightBracketCounter = 0;
        while (true) {
            pos = expression.indexOf('(', pos);
            if (pos > 0) {
                leftBracketCounter++;
                pos++;
            } else
                break;
        }
        pos = 0;
        while (true) {
            pos = expression.indexOf(')', pos);
            if (pos > 0) {
                rightBracketCounter++;
                pos++;
            }
            else
                break;
        }

        return leftBracketCounter == rightBracketCounter;
    }


}

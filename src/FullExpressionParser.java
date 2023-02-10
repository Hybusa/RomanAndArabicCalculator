import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class FullExpressionParser {
    List<String> expressions = new ArrayList<>();

    FullExpressionParser(String expression) throws NegativeRomanException {
        expression = expression.trim();
        expression = expression.replace(" ", "");
        expression = expression.toUpperCase();

        if(!expression.matches("[*/^+IVXLDM()0-9-]+"))
            throw new InputMismatchException("Wrong input");

        expression = calculateAndRemoveBrackets(expression);

        String[] numeralsArr = expression.split("[*/^+-]");
        if(numeralsArr.length < 1)
            throw new InputMismatchException("Wrong input");

        String operands = expression.replaceAll("\\p{Alnum}","");
        operands = operands.replaceAll("[.]","");
        char[] operandsArr = operands.toCharArray();
        if(operandsArr.length < 1)
            throw new InputMismatchException("Wrong input");

        for(int i =0 ; i < numeralsArr.length;i++)
        {
            expressions.add(numeralsArr[i]);
            if(i < operandsArr.length)
                expressions.add(String.valueOf(operandsArr[i]));
        }
       // System.out.println(expressions);

    }


    public String calculate() throws NegativeRomanException {
        String[] cases ={"^","*","/","-","+"};

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
               if(calc.typeOfNumbers == TypeOfNumbers.ROMAN)
                    expressions.set(pos, RomanNumerals.getRomanNumeralFromNumber((int)Float.parseFloat(calc.calculate())));
               else
                   expressions.set(pos, calc.calculate());
           }
         //  System.out.println(expressions);
       }
        return expressions.get(0);
    }

    private String calculateAndRemoveBrackets(String expression) throws NegativeRomanException {
        if(expression.matches(".*[IVXLDM0-9]\\(.*") || expression.matches(".*\\)[IVXLDM0-9].*"))
            throw new InputMismatchException("Wrong input");
        while (expression.contains("(")) {
            if (!checkBracketsMatch(expression)) {
                throw new InputMismatchException("Wrong amount of brackets");
            }

            int bracketsBegin = expression.indexOf('(');
            int bracketsEnd = expression.indexOf(')');

            FullExpressionParser calc = new FullExpressionParser(expression.substring(bracketsBegin+1, bracketsEnd));

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
            }
            else
                break;
        }

        return leftBracketCounter == rightBracketCounter;
    }


}

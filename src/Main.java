public class Main {
    public static void main(String[] args) {
/*
        Operand op = Operand.valueOfOperand('*');
        System.out.println(op.toString());

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
        System.out.println(result);*/


        StateMachineParser parser = new StateMachineParser("1 + (2- 3 + (3-8 + 6 ) + 32 )) /2 + 5^2 - 1");
        System.out.println(parser.calculate());


    }
}
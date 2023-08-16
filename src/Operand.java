import java.util.InputMismatchException;

public enum Operand {
    MULTIPLICATION('*', OperandState.SECOND_PRIO),
    DIVISION('/', OperandState.SECOND_PRIO),
    POW('^',OperandState.FIRST_PRIO),
    ADDITION('+', OperandState.THIRD_PRIO),
    SUBTRACTION('-', OperandState.THIRD_PRIO);

    private final char operand;
    public final OperandState operandState;

     Operand(final char operand, OperandState operandState) {
        this.operand = operand;
        this.operandState = operandState;

    }

    public static Operand valueOfOperand(char operand) {

        for (Operand op : values()) {
            if (op.operand == operand) {
                return op;
            }
        }
        throw new InputMismatchException("Something wrong with operand");
    }

    @Override
    public String toString() {
        return String.valueOf(operand);
    }


}

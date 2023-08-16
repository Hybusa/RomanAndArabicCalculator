public enum OperandState {
    FIRST_PRIO(1),
    SECOND_PRIO(2),
    THIRD_PRIO(3),
    FOURTH_PRIO(4),
    DUMMY(Integer.MAX_VALUE);

    final int intValue;
    OperandState(int intValue)
    {
        this.intValue = intValue;
    }
}

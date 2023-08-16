import java.util.InputMismatchException;

public class Numeral {

    double value;
    TypeOfNumbers type;
    Numeral(){}

    Numeral(String numeral) {

        if (numeral.matches("[.0-9]+")) {
            this.type = TypeOfNumbers.ARABIC;
            this.value = Double.parseDouble(numeral);

        } else if (numeral.matches("[IVCXLDM]+")) {
            this.type = TypeOfNumbers.ROMAN;
            this.value = RomanNumerals.getNumberFromRomanNumeral(numeral);
        }
        else
            throw new InputMismatchException("Wrong input");
    }

    @Override
    public String toString() {

        if(this.type == TypeOfNumbers.ARABIC)
            return String.valueOf(this.value);
        return RomanNumerals.getRomanNumeralFromNumber((int)this.value);
    }
}

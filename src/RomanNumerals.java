public enum RomanNumerals {
    I(1),
    V(5),
    X(10),
    L(50),
    C(100),
    D(500),
    M(1000);

    private final int number;
    RomanNumerals(int number){
        this.number = number;
    }

    public static int getNumberFromRomanNumeral(String numeral) {
        numeral = numeral.replace("IV","IIII");
        numeral = numeral.replace("IX","VIIII");
        numeral = numeral.replace("XL","XXXX");
        numeral = numeral.replace("XC","LXXXX");
        numeral = numeral.replace("CD","CCCC");
        numeral = numeral.replace("CM","DCCCC");

        int number = 0;
        for (int i = 0; i < numeral.length(); i++)
        {
            number = number + RomanNumerals.valueOf(String.valueOf(numeral.charAt(i))).number;
        }
        return number;
    }

    public static String getRomanNumeralFromNumber(int number) {
        int[] values = {1000,900,500,400,100,90,50,40,10,9,5,4,1};
        String[] romanLetters = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
        StringBuilder roman = new StringBuilder();
        for(int i=0;i<values.length;i++)
        {
            while(number >= values[i])
            {
                number = number - values[i];
                roman.append(romanLetters[i]);
            }
        }
        return roman.toString();
    }
}

package logic;

/**
 * Created by Marco on 2/8/2018.
 */

public class PhoneNumberTranslator {
    public String ToNumber(String raw)
    {
        if (!raw.matches("[-[0-9][A-Z]]+"))
            return "";
        else
            raw = raw.toUpperCase();
        StringBuilder newNumber = new StringBuilder();
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (" -0123456789".indexOf(c) > -1)
                newNumber.append(c);
            else {
                char result = TranslateToNumber(c);
                newNumber.append(result);
            }
        }
        return newNumber.toString();
    }

    private char TranslateToNumber(char c) {
        if ("ABC".indexOf(c) > -1)
            return '2';
        else if ("DEF".indexOf(c) > -1)
            return '3';
        else if ("GHI".indexOf(c) > -1)
            return '4';
        else if ("JKL".indexOf(c) > -1)
            return '5';
        else if ("MNO".indexOf(c) > -1)
            return '6';
        else if ("PQRS".indexOf(c) > -1)
            return '7';
        else if ("TUV".indexOf(c) > -1)
            return '8';
        else if ("WXYZ".indexOf(c) > -1)
            return '9';
        else
            return c;
    }
}

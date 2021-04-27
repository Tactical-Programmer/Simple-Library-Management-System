
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Validations {

    public LocalDate GetCurrentDate() {
        String dataActual = "";
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        dataActual = sdf.format(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate _date = LocalDate.parse(dataActual, formatter);

        return _date;
    }

    public String ConvertDate(LocalDate date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = date.format(dateFormatter);

        return formattedDate;
    }

    public String CalculateLoanDuration(LocalDate date) {
        date = date.plusDays(14);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = date.format(dateFormatter);

        return formattedDate;
    }

    public String ValidateName(String string, int min, int max, String campo) {
        char c;
        short stringLength;
        String name = "";
        int nrOfLetters;
        int nrOfSpaces;

        if (string.isEmpty()) {
            JOptionPane.showMessageDialog(new JFrame(), "O Campo do " + campo + " está vazio.");
            return null;
        } else {
            try {
                name = string;
                stringLength = (short) name.length();
                nrOfLetters = 0;
                nrOfSpaces = 0;
                for (int i = 0; i < stringLength; i++) {
                    c = name.charAt(i);
                    if (Character.isLetter(c)) {
                        nrOfLetters++;
                    } else if (Character.isSpaceChar(c)) {
                        nrOfSpaces++;
                    }
                }
                if ((nrOfLetters + nrOfSpaces != stringLength)) {
                    JOptionPane.showMessageDialog(new JFrame(), "O " + campo + " contem número(s) e/ou caracter(es) invalido.");
                } else if (nrOfLetters < min || nrOfLetters > max) {
                    JOptionPane.showMessageDialog(new JFrame(), "O " + campo + " não está dentro do limite de caracteres.");
                }
                if ((nrOfLetters + nrOfSpaces != stringLength) || (nrOfLetters < min || nrOfLetters > max)) {
                    name = null;
                }
            } catch (IndexOutOfBoundsException ie) {
                JOptionPane.showMessageDialog(new JFrame(), "Erro inesperado no Campo do " + campo + "!");
                name = null;
            }
            return name;
        }
    }

    public String ValidateString(String string, int min, int max, String campo) {
        char c;
        short stringLength;
        String name = "";
        int nrOfLetters;

        if (string.isEmpty()) {
            JOptionPane.showMessageDialog(new JFrame(), "O campo do " + campo + " está vazio.");
            return null;
        } else {
            try {
                name = string;
                stringLength = (short) name.length();
                nrOfLetters = 0;
                for (int i = 0; i < stringLength; i++) {
                    c = name.charAt(i);
                    if (Character.isLetter(c)) {
                        nrOfLetters++;
                    }
                }
                if (nrOfLetters < min || nrOfLetters > max) {
                    JOptionPane.showMessageDialog(new JFrame(), "O campo do " + campo + " não está dentro do limite de caracteres.");
                }
                if ((nrOfLetters < min || nrOfLetters > max)) {
                    name = null;
                }
            } catch (IndexOutOfBoundsException ie) {
                JOptionPane.showMessageDialog(new JFrame(), "Erro inesperado no campo do " + campo + "!");
                name = null;
            }
            return name;
        }
    }

    public String ValidateID(String string, byte size) throws IOException {
        char c;
        byte length;
        String id = "";
        int nrOfDigits = 0, nrOfLetters = 0;

        if (string.isEmpty()) {
            JOptionPane.showMessageDialog(new JFrame(), "O Campo de texto está vazio.");
            return null;
        } else {
            try {
                id = string;
                length = (byte) id.length();
                nrOfDigits = 0;
                nrOfLetters = 0;
                for (int i = 0; i < length; i++) {
                    c = id.charAt(i);
                    if (Character.isDigit(c)) {
                        nrOfDigits++;
                    }
                }
                if (Character.isLetter(id.charAt(length - 1))) {
                    nrOfLetters++;
                }
                System.out.println(length + ";" + size + ";" + nrOfDigits + ";" + nrOfLetters);
                if ((nrOfDigits != length - 1) || (length != size) || (nrOfLetters != 1)) {
                    JOptionPane.showMessageDialog(new JFrame(), "O número de BI deve conter 12 numeros e uma letra, respectivamente). ");
                }
                if ((nrOfDigits != length - 1) || (length != size) || (nrOfLetters != 1)) {
                    id = null;
                }
            } catch (IndexOutOfBoundsException ie) {
                JOptionPane.showMessageDialog(new JFrame(), "Erro inesperado no campo do Número de BI!");
                id = null;
            }
            return id;
        }
    }

    public int ValidateInt(String string, int min, int max, String campo) {
        int number = 0;

        if (string.isEmpty()) {
            JOptionPane.showMessageDialog(new JFrame(), "O Campo do " + campo + " está vazio.");
            return -1;
        } else {
            try {
                number = Integer.parseInt(string);
                if (number < min || number > max) {
                    JOptionPane.showMessageDialog(new JFrame(), "O " + campo + " não está dentro do limite.");
                }
                if (number < min || number > max) {
                    number = -1;
                }
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(new JFrame(), "Erro inesperado no campo do " + campo + "!");
                number = -1;
            }
            return number;
        }
    }

    public Long ValidateLong(String string, int size, String campo) throws IOException {
        String numberString;
        Long number = null;

        if (string.isEmpty()) {
            JOptionPane.showMessageDialog(new JFrame(), "O Campo do " + campo + " está vazio.");
            return null;
        } else {
            numberString = string;
            try {
                number = Long.parseLong(numberString);
                if (numberString.length() != size) {
                    JOptionPane.showMessageDialog(new JFrame(), "O " + campo + " não está dentro do limite (Deve conter " + size + " numeros).");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(new JFrame(), "O campo do " + campo + " deve conter apenas números!");
                number = null;
            }
            if (numberString.length() != size || number == null) {
                number = null;
            }
            return number;
        }
    }

    public Long ValidatePhoneNumber(String string) throws IOException {
        String numberString = null;
        int firstTwoNumbers = 0;
        Long number = null;

        if (string.isEmpty()) {
            JOptionPane.showMessageDialog(new JFrame(), "O Campo do Número de Telefone está vazio.");
            return null;
        } else {
            try {
                System.out.print("+" + string + "+");
                numberString = string;
                firstTwoNumbers = Integer.parseInt(numberString.substring(0, 2));
                number = Long.parseLong(numberString);

                if (numberString.length() != 9) {
                    JOptionPane.showMessageDialog(new JFrame(), "O número de telefone não está dentro do limite (Deve conter 9 digitos).");
                } else if (firstTwoNumbers < 82 || firstTwoNumbers > 87) {
                    JOptionPane.showMessageDialog(new JFrame(), "Os primeiros dois números do número de telefone devem estar entre 82 e 87.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(new JFrame(), "O número de telefone deve conter apenas números.");
                numberString = null;
                number = null;
            }
            if (numberString.length() != 9 || number == null || firstTwoNumbers < 82 || firstTwoNumbers > 87) {
                number = null;
            }
            return number;
        }
    }
}
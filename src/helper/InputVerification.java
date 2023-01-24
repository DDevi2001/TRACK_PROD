package helper;

import java.text.DecimalFormat;
import java.util.Scanner;

public class InputVerification {
    private static Scanner scan = new Scanner(System.in);

    public static String getOption(int length) {
        while (true) {
            System.out.println();
            System.out.print("ENTER YOUR PREFERENCE: ");
            String option = scan.nextLine();
            if (option.matches("[1-" + length + "]")) {
                return option;
            }
            System.out.println("ENTER VALID OPTION");
        }
    }

    public static String getString() {
        while (true) {
            String inputS = scan.nextLine().trim();
            if (inputS.matches("^[A-Za-z\\s?]*$")) {
                return inputS;
            }
            System.out.println("INPUT SHOULD CONTAIN ONLY ALPHABETS");
        }
    }

    public static float getFloat() {
        while (true) {
            String inputF = scan.nextLine();
            try {
                double input = Double.parseDouble(inputF);
                return Float.parseFloat(new DecimalFormat("0.00").format(input));
            } catch (Exception e) {
                System.out.println("ENTER A INTEGER VALUE");
            }
        }
    }

    public static int getInt() {
        while (true) {
            String inputI = scan.nextLine();
            try {
                return Integer.parseInt(inputI);
            } catch (Exception e) {
                System.out.println("ENTER A INTEGER VALUE");
            }
        }
    }

    public static String getID() {
        while (true) {
            String inputID = scan.nextLine();
            if (inputID.matches("(PRD|CUS|ORD|INV|MAN|SAL)[0-9]{4}")) {
                return inputID;
            }
            System.out.println("INVALID ID");
        }
    }

    public static boolean yesOrNoCheck() {
        while (true) {
            String input = scan.nextLine();
            if (input.matches("[yYnN]")) {
                return input.matches("[yY]");
            }
            System.out.println("ENTER VALID OPTION");
        }
    }
}

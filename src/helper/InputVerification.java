package helper;

import java.util.Scanner;

public class InputVerification {
    private static Scanner scan = new Scanner(System.in);

    public static String getOption(int length) {
        System.out.print("\nENTER YOUR PREFERENCE: ");
        String option = scan.nextLine();
        if (!option.matches("[1-" + length + "]")) {
            System.out.println("Please enter valid option");
            return getOption(length);
        } else {
            return option;
        }
    }

    public static String getString() {
        String inputS = scan.nextLine().trim();
        if (!inputS.matches("^[A-Za-z\\s?]*$")) {
            System.out.println("Input should contain only alphabets");
            return getString();
        } else {
            return inputS;
        }
    }

    public static float getFloat() {
        String inputF = scan.nextLine();
        try {
            float input = Float.parseFloat(inputF);
            if (inputF.matches("^-?\\d+\\.?\\d{0,2}$")) {
                return input;
            } else {
                System.out.println("Limit value to two decimal place");
                return getFloat();
            }

        } catch (Exception e) {
            System.out.println("Please enter a integer");
            return getFloat();
        }
    }

    public static int getInt() {
        String inputI = scan.nextLine();
        try {
            return Integer.parseInt(inputI);
        } catch (Exception e) {
            System.out.println("Please enter a integer");
            return getInt();
        }
    }

    public static String isID(){
        String inputID = scan.nextLine();
        if (inputID.matches("(PRD|CUS|ORD|INV|MAN|SAL)[0-9]{4}")) {
            return inputID;
        }
        System.out.println("Enter valid ID");
        return isID();
    }

    public static String yesOrNoCheck() {
        String input = scan.nextLine();
        if (input.matches("[y|n]")) {
            return input;
        } else {
            System.out.println("Enter valid option");
            return yesOrNoCheck();
        }
    }
}

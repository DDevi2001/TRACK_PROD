package main;

import ui.ApplicationUI;
import helper.InputVerification;
import helper.Utils;

public class MainApp {
    public static void main(String[] args) {
        ApplicationUI applicationUI = new ApplicationUI();
        main:
        while (true) {
            System.out.println("\n_________________________________________________________________________________________________________________________________________________________________________________________");
            System.out.println("                                                                                   TRACK-PROD");
            System.out.println("_________________________________________________________________________________________________________________________________________________________________________________________");
            MainFunctions[] mainFunctions = MainFunctions.values();
            Utils.printOptions(mainFunctions);
            MainFunctions preference = mainFunctions[Integer.parseInt(InputVerification.getOption(mainFunctions.length)) - 1];
            switch (preference) {
                case SIGNIN:
                    applicationUI.signIN();
                    break  ;
                case SIGNUP:
                    applicationUI.signUP();
                    break ;
                case EXIT:
                    break main;
            }
        }
    }
}
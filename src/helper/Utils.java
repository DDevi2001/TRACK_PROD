package helper;

import java.time.LocalDate;

public class Utils {

    private static String id;
    private static int customer_no = 0;
    private static int product_no = 0;
    private static int order_no = 0;

    public static void printOptions(Enum[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println((i+1)+". "+array[i]);
        }
    }

    public static String generateID(String type) {
        switch (type){
            case "Customer":
                return String.format("CUS%04d",++customer_no);
            case "Product":
                return String.format("PRD%04d",++product_no);
            case "Order":
                return String.format(LocalDate.now().toString().substring(0,4)+"ORD%05d",order_no);
        }
        return id;
    }
}

/**
 * @author Kuyper Reynolds
 */
import java.util.Scanner;
import java.util.regex.Pattern;

public class SafeInput {

    /**
     * Get user input that is not empty
     * @param pipe a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @return a String response that is not zero length
     */
    public static String getNonZeroLenString(Scanner pipe, String prompt) {
        String retString = "";

        do {
            System.out.print(prompt + ": ");
            retString = pipe.nextLine();
        } while(retString.isEmpty());

        return retString;
    }

    /**
     * Get user input that is an integer
     * @param pipe a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @return an integer response
     */
    public static int getInt(Scanner pipe, String prompt) {
        boolean done = false;
        int response = 0;
        do {
            System.out.print(prompt + ": ");
            if (pipe.hasNextInt()) {
                response = pipe.nextInt();
                pipe.nextLine();
                done = true;
            } else {
                System.out.println("ERROR: input must be an integer, not '" + pipe.nextLine() + "'.");
            }
        } while (!done);
        return response;
    }

    /**
     * Get user input that is a double
     * @param pipe a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @return a double response
     */
    public static double getDouble(Scanner pipe, String prompt) {
        boolean done = false;
        double response = 0;
        do {
            System.out.print(prompt + ": ");
            if (pipe.hasNextDouble()) {
                response = pipe.nextDouble();
                pipe.nextLine();
                done = true;
            } else {
                System.out.println("ERROR: input must be a double, not '" + pipe.nextLine() + "'.");
            }
        } while (!done);
        return response;
    }

    /**
     * Get user input that is an integer within the specified range
     * @param pipe a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @return an integer response within the specified range
     */
    public static int getRangedInt(Scanner pipe, String prompt, int lo, int hi) {
        boolean done = false;
        int retInt = 0;
        do {
            System.out.print(prompt + " [" + lo + "-" + hi + "]: ");
            if (pipe.hasNextInt()) {
                retInt = pipe.nextInt();
                if (retInt >= lo && retInt <= hi) {
                    pipe.nextLine();
                    done = true;
                } else {
                    System.out.println("ERROR: input integer must be" + " [" + lo + "-" + hi + "]" + ", not '" + retInt + "'.");
                    pipe.nextLine();
                }
            } else {
                System.out.println("ERROR: input must be an integer , not '" + pipe.nextLine() + "'.");
            }
        } while (!done);
        return retInt;
    }

    /**
     * Get user input that is a double within the specified range
     * @param pipe a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @return a double response within the specified range
     */
    public static double getRangedDouble(Scanner pipe, String prompt, double lo, double hi) {
        boolean done = false;
        double retDouble = 0;
        do {
            System.out.print(prompt + " [" + lo + "-" + hi + "]: ");
            if (pipe.hasNextDouble()) {
                retDouble = pipe.nextDouble();
                if (retDouble >= lo && retDouble <= hi) {
                    pipe.nextLine();
                    done = true;
                } else {
                    System.out.println("ERROR: input double must be" + " [" + lo + "-" + hi + "]" + ", not '" + retDouble + "'.");
                    pipe.nextLine();
                }
            } else {
                System.out.println("ERROR: input must be a double , not '" + pipe.nextLine() + "'.");
            }
        } while (!done);
        return retDouble;
    }

    /**
     * Get user response to a yes or no question
     * @param pipe a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @return a boolean value (true for 'Y', false for 'N')
     */
    public static boolean getYNConfirm(Scanner pipe, String prompt) {
        boolean done = false;
        boolean response = false;
        do {
            System.out.print(prompt + " [Y/N]: ");
            String input = pipe.nextLine();
            if (input.equalsIgnoreCase("y")) {
                response = true;
                done = true;
            } else if (input.equalsIgnoreCase("n")) {
                done = true;
            } else
                System.out.println("ERROR: input must be [Y/N], not '" + input + "'.");
        } while (!done);
        return response;
    }

    /**
     * Validate user input with provided RegEx string
     * @param pipe a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @param regEx RegEx string
     * @return The validated user input string
     */
    public static String getRegExString(Scanner pipe, String prompt, String regEx) {
        boolean done = false;
        String response = "";

        do {
            System.out.print(prompt + ": ");
            if (pipe.hasNext(regEx)) {
                response = pipe.nextLine();
                done = true;
            } else {
                System.out.println("ERROR: '" + pipe.nextLine() + "' does not meet the criteria.");
            }
        } while (!done);

        return response;
    }

    /**
     * Print a message into a formatted header
     * @param msg the message included in the header
     */
    public static void prettyHeader(String msg) {
        int TOTAL_WIDTH = 60;
        int CENTER_STAR_COUNT = 3;
        int msgLength = msg.length();
        int whitespace = (TOTAL_WIDTH - msgLength) / 2 - CENTER_STAR_COUNT;

        if (whitespace < 1) {
            whitespace = 1;
        }

        for (int i = 0; i < TOTAL_WIDTH; i++) {
            System.out.print("*");
        }
        System.out.println();

        for (int i = 0; i < CENTER_STAR_COUNT; i++) {
            System.out.print("*");
        }

        for (int i = 0; i < whitespace; i++) {
            System.out.print(" ");
        }

        System.out.print(msg);

        if (msgLength % 2 == 1) {
            System.out.print(" ");
        }

        for (int i = 0; i < whitespace; i++) {
            System.out.print(" ");
        }

        for (int i = 0; i < CENTER_STAR_COUNT; i++) {
            System.out.print("*");
        }

        System.out.println();

        for (int i = 0; i < TOTAL_WIDTH; i++) {
            System.out.print("*");
        }

        System.out.println();
    }
}
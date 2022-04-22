package org.example;


import java.util.Scanner;

public class App 
{
    private static final ReversePolishNotation rpn = new ReversePolishNotation();

    private static final Scanner s = new Scanner(System.in);

    private static final DBBridge db = new DBBridge();

    private static String command = "";

    public static void main( String[] args ) {
        mainMenu();
    }

    private static void mainMenu() {
        while (!command.equals("3")) {
            clearConsole();
            System.out.print( "MAIN MENU:\n" +
                    "1) Calculate expression;\n" +
                    "2) Vies expressions storage;\n" +
                    "3) Quit.\n\n" +
                    "Enter the operation number: " );
            command = s.nextLine();

            switch(command) {
                case "1":
                    calcMenu();
                    break;
                case "2":
                    storageMenu();
            }
        }
    }

    private static void calcMenu() {
        System.out.println( "Print expression: " );
        command = s.nextLine();
        Double result = rpn.run(command);
        if(result == null) {
            return;
        }
        System.out.printf( "%s=%f\n", command, result);
        String expr = command;
        command = "";

        while (!command.equals("2")) {
            clearConsole();
            System.out.print("1) Save expression with result;\n" +
                    "2) Back to Main Menu without saving.\n\n" +
                    "Enter the operation number: ");
            command = s.nextLine();

            if (command.equals("1")) {
                db.create(expr, result);
                System.out.print("Expression and its result saved to db. Back to Main Menu...\n\n");
                return;
            }
        }
    }

    public static void storageMenu() {
        String[] cmds;
        int id;
        Double res;
        while (!command.equals("5")) {
            clearConsole();
            System.out.print( "DB MENU:\n" +
                    "1) Show list of expressions;\n" +
                    "2) Show list of expression by condition;\n" +
                    "3) Edit expression by id;\n" +
                    "4) Delete expression by id;\n" +
                    "5) Back to Main Menu.\n\n" +
                    "Enter the operation number: " );
            command = s.nextLine();

            switch(command) {
                case "1":
                    db.list();
                    break;
                case "2":
                    try {
                        System.out.print("Enter command like [<operand> <number>]," +
                                " where operand is in ['=', '<', '>']: ");
                        cmds = s.nextLine().split(" ");
                        db.list(cmds[0], Double.parseDouble(cmds[1]));
                    } catch (Exception e) {
                        System.out.println("Check if the entered commands are correct. " +
                                "Notice that between operand and number must be space.\n");
                    }
                    break;
                case "3":
                    try {
                        System.out.print("Enter expression ID, that must be edited: ");
                        id = Integer.parseInt(s.nextLine());
                        System.out.print("Enter new expression: ");
                        command = s.nextLine();
                        res = rpn.run(command);
                        if(res == null) {
                            continue;
                        }
                        db.update(id, command, res);
                    } catch (Exception e) {
                        System.out.println("Check if the entered commands and expression are correct.\n");
                    }
                    command = "";
                    break;
                case "4":
                    System.out.print("Enter expression ID, that must be deleted: ");
                    try {
                        db.delete( Integer.parseInt( s.nextLine() ) );
                    } catch (Exception e) {
                        System.out.println("Check if the entered commands are correct\n");
                    }
                    break;
            }
        }
    }

    public static void clearConsole()
    {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception ignore) {}
    }
}

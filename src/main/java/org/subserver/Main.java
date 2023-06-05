package org.subserver;

import org.subserver.interfaces.ConsoleColors;
import org.subserver.models.ServerInfo;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Scanner;

import static org.subserver.functions.CreateProcess.createProcess;
import static org.subserver.functions.StartServerProcess.startServerProcess;

public class Main {

    public static HashMap<String, ServerInfo> serversOnline = new HashMap<String, ServerInfo>();

    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                for (ServerInfo serverInfo : serversOnline.values()) {
                    try {
                        ProcessBuilder processBuilder = new ProcessBuilder("kill", "-9", String.valueOf(serverInfo.getPID()));
                        processBuilder.start();
                    } catch (IOException ignored) {}
                }
            }
        });
        System.out.println(ConsoleColors.ANSI_YELLOW + "===========================================================" + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_YELLOW + "      SUBSERVER - CREATE A MINECRAFT NETWORK IN UNIQUE CONTAINER           " + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_YELLOW + "===========================================================" + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_BLUE + "HELP COMMANDS - Version v1.0" + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_BLUE + "                             " + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_YELLOW + "[" + ConsoleColors.ANSI_RED + "1" + ConsoleColors.ANSI_YELLOW + "]" + "- Create a subserver" + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_YELLOW + "[" + ConsoleColors.ANSI_RED + "2" + ConsoleColors.ANSI_YELLOW + "]" + "- Start a server" + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_YELLOW + "[" + ConsoleColors.ANSI_RED + "3" + ConsoleColors.ANSI_YELLOW + "]" + "- KILL A SERVER" + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_YELLOW + "[" + ConsoleColors.ANSI_RED + "4" + ConsoleColors.ANSI_YELLOW + "]" + "- Verify Server info" + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_YELLOW + "[" + ConsoleColors.ANSI_RED + "5" + ConsoleColors.ANSI_YELLOW + "]" + "- Stop SubProcess manager" + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_YELLOW + "" + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_YELLOW + "===========================================================" + ConsoleColors.ANSI_RESET);
        Scanner scanner = new Scanner(System.in);
        System.out.print(ConsoleColors.ANSI_BLUE + "Enter a command: " + ConsoleColors.ANSI_RESET);
        String command = scanner.nextLine();
        switch (command) {
            default: {
                System.out.println(ConsoleColors.ANSI_RED + "Command not found!" + ConsoleColors.ANSI_RESET);
                main(args);
                scanner.close();
                break;
            }
            case "1": {
                createProcess();
                scanner.close();
                break;
            }
            case "2": {
                System.out.print(ConsoleColors.ANSI_BLUE + "Type name of server: " + ConsoleColors.ANSI_RESET);
                String name = scanner.nextLine();
                startServerProcess(name, args, serversOnline);
                scanner.close();
                break;
            }
            case "4": {
                System.out.print(ConsoleColors.ANSI_BLUE + "Type name of server: " + ConsoleColors.ANSI_RESET);
                String name = scanner.nextLine();
                ServerInfo serverInfo = serversOnline.get(name);
                if (serverInfo == null) {
                    System.out.println(ConsoleColors.ANSI_RED + "Server not found!" + ConsoleColors.ANSI_RESET);
                    main(args);
                    scanner.close();
                    return;
                }
                System.out.println(ConsoleColors.ANSI_YELLOW + "===========================================================" + ConsoleColors.ANSI_RESET);
                System.out.println(ConsoleColors.ANSI_YELLOW + "      SUBSERVER - CREATE A MINECRAFT NETWORK IN UNIQUE CONTAINER           " + ConsoleColors.ANSI_RESET);
                System.out.println(ConsoleColors.ANSI_YELLOW + "===========================================================" + ConsoleColors.ANSI_RESET);
                System.out.println(ConsoleColors.ANSI_BLUE + "SERVER INFO - " + name + ConsoleColors.ANSI_RESET);
                System.out.println(ConsoleColors.ANSI_CYAN + "                             " + ConsoleColors.ANSI_RESET);
                System.out.println(ConsoleColors.ANSI_CYAN + "Server name: " + serverInfo.getName() + ConsoleColors.ANSI_RESET);
                System.out.println(ConsoleColors.ANSI_CYAN + "Server PID: " + serverInfo.getPID() + ConsoleColors.ANSI_RESET);
                System.out.println(ConsoleColors.ANSI_CYAN + "PRESS L TO VIEW LOGS SERVER AND PRESS S TO STOP" + ConsoleColors.ANSI_RESET);
                System.out.println(ConsoleColors.ANSI_YELLOW + "=============================================================" + ConsoleColors.ANSI_RESET);
                System.out.print(ConsoleColors.ANSI_BLUE + "Enter a command: " + ConsoleColors.ANSI_RESET);
                String command2 = scanner.nextLine();
                if (command2.equalsIgnoreCase("l")) {
                    String stop = scanner.nextLine();
                    while (true) {
                        System.out.print("\033[H\033[2J");
                        if (stop.equalsIgnoreCase("s")) {
                            main(args);
                            break;
                        }
                        Thread.sleep(1000);
                    }
                }
                break;
            }
            case "5": {
                System.out.println(ConsoleColors.ANSI_RED + "Stopping SubProcess manager..." + ConsoleColors.ANSI_RESET);
                System.exit(0);
                break;
            }
            case "stop": {
                for (ServerInfo serverInfo : serversOnline.values()) {
                    try {
                        ProcessBuilder processBuilder = new ProcessBuilder("kill", "-9", String.valueOf(serverInfo.getPID()));
                        processBuilder.start();
                    } catch (IOException ignored) {}
                }
                System.exit(0);
            }
        }
    }
}
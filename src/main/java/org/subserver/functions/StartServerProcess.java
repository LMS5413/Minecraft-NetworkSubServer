package org.subserver.functions;

import org.subserver.interfaces.ConsoleColors;
import org.subserver.models.ServerInfo;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Scanner;

import static org.subserver.Main.main;

public class StartServerProcess {
    public static void startServerProcess(String name, String[] args, HashMap<String, ServerInfo> serversOnline) throws Exception {
        System.out.print("\033[H\033[2J");
        System.out.println(ConsoleColors.ANSI_YELLOW + "===========================================================" + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_YELLOW + "      SUBSERVER - CREATE A MINECRAFT NETWORK IN UNIQUE CONTAINER           " + ConsoleColors.ANSI_RESET);
        System.out.println(ConsoleColors.ANSI_YELLOW + "===========================================================" + ConsoleColors.ANSI_RESET);
        System.out.println();
        System.out.println(ConsoleColors.ANSI_YELLOW + "STATUS - VERIFING IF SERVER EXISTS" + ConsoleColors.ANSI_RESET);
        File file = new File("./subservers/" + name);
        Thread.sleep(3000);
        if (!file.exists()) {
            System.out.println(ConsoleColors.ANSI_RED + "SERVER NOT FOUND. KILLING IN 5 SECONDS..." + ConsoleColors.ANSI_RESET);
            Thread.sleep(5000);
            System.out.print("\033[H\033[2J");
            main(args);
            return;
        }
        System.out.println(ConsoleColors.ANSI_GREEN + "STATUS - SERVER FOUND, VERIFYING IF SERVER IS ONLINE..." + ConsoleColors.ANSI_RESET);
        Thread.sleep(1000);
        if (serversOnline.containsKey(name)) {
            System.out.println(ConsoleColors.ANSI_RED + "ERROR - SERVER IS ONLINE. KILLING IN 5 SECONDS..." + ConsoleColors.ANSI_RESET);
            Thread.sleep(5000);
            System.out.print("\033[H\033[2J");
            main(args);
            return;
        }
        System.out.println(ConsoleColors.ANSI_GREEN + "STATUS - SERVER FOUND, READING CONFIG..." + ConsoleColors.ANSI_RESET);
        Thread.sleep(1000);
        File fileConfig = new File("./subservers/" + name + "/subserverprocess.conf");
        if (!fileConfig.exists()) {
            System.out.println(ConsoleColors.ANSI_RED + "ERROR - CONFIG NOT FOUND. KILLING IN 5 SECONDS..." + ConsoleColors.ANSI_RESET);
            Thread.sleep(5000);
            System.out.print("\033[H\033[2J");
            main(args);
            return;
        }
        String content = "";
        Scanner myReader = new Scanner(fileConfig);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            content += data + "\n";
        }
        myReader.close();
        System.out.println(ConsoleColors.ANSI_GREEN + "DONE! EXECUTING TASK..." + ConsoleColors.ANSI_RESET);
        ProcessBuilder processBuilder = new ProcessBuilder(content.split("\n")[0].replace("SH_START=", "").split(" "));
        processBuilder.directory(new File("./subservers/" + name + "/"));
        Process process = processBuilder.start();
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("You need to agree to the EULA in order to run the server. Go to eula.txt for more info.")) {
                System.out.println(ConsoleColors.ANSI_RED + "ERROR - YOU NEED TO AGREE TO THE EULA IN ORDER TO RUN THE SERVER. GO TO EULA.TXT FOR MORE INFO. KILLING IN 5 SECONDS..." + ConsoleColors.ANSI_RESET);
                Thread.sleep(5000);
                System.out.print("\033[H\033[2J");
                main(args);
                return;
            }
            if (line.contains("For help, type \"help\"")) {
                System.out.println(System.getProperty("java.version"));
                String pid = !System.getProperty("java.version").startsWith("1.8.0") ? process.toString().split("=")[1].split(",")[0]:getProcessPid(process) + "";
                System.out.println(ConsoleColors.ANSI_GREEN + "SERVER STARTED SUCESSFUL!. USE THIS PID TO CLOSE SERVER: " + pid + ConsoleColors.ANSI_RESET);
                ServerInfo serverInfo = new ServerInfo(Integer.parseInt(pid), name, line);
                serversOnline.put(name, serverInfo);
                Thread.sleep(5000);
                System.out.print("\033[H\033[2J");
                main(args);
            }
            ServerInfo serverInfo = serversOnline.get(name);
            if (serverInfo != null && serverInfo.getLogs() != null) {
                serverInfo.setLogs(serverInfo.getLogs() + "\n" + line);
            }
        }
        int exitCode = process.waitFor();
        if (exitCode == 1) {
            System.out.println(ConsoleColors.ANSI_RED + "AN OCURRED INTERNAL ERROR. VERIFY IF \n \n1 - IF JAVA IS INSTALLED\n2 - IF JAR EXEC EXISTS." + ConsoleColors.ANSI_RESET);
        }
    }
    private static int getProcessPid(Process process) throws Exception {
        Class<?> processClass = process.getClass();
        if (processClass.getName().equals("java.lang.UNIXProcess")) {
            Field pidField = processClass.getDeclaredField("pid");
            pidField.setAccessible(true);
            return (int) pidField.get(process);
        } else {
            throw new UnsupportedOperationException("Obter PID não é suportado nesta plataforma.");
        }
    }
}

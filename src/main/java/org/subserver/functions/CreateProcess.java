package org.subserver.functions;

import org.subserver.interfaces.ConsoleColors;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CreateProcess {
    public static void createProcess() throws IOException {
        File SubserversFolder = new File("./subservers");
        if (!SubserversFolder.exists()) {
            SubserversFolder.mkdir();
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print(ConsoleColors.ANSI_YELLOW + "Type name of server: " + ConsoleColors.ANSI_RESET);
        String name = scanner.nextLine();
        File SubserverFolder = new File("./subservers/" + name);
        if (SubserverFolder.exists()) {
            System.out.println(ConsoleColors.ANSI_RED + "Server already exists! " + ConsoleColors.ANSI_RESET);
            createProcess();
            return;
        }
        SubserverFolder.mkdir();
        File SubserverProcessFile = new File("./subservers/" + name + "/subserverprocess.conf");
        SubserverProcessFile.createNewFile();
        String str = "SH_START=java -Xmx7G -jar server.jar\nSTOP_COMMAND=stop";
        BufferedWriter writer = new BufferedWriter(new FileWriter("./subservers/" + name + "/subserverprocess.conf"));
        writer.write(str);
        writer.close();
        System.out.println(ConsoleColors.ANSI_GREEN + "Server created sucessful! Now, up your server from folder subservers/" + name + " and configure subserverprocess.conf." + ConsoleColors.ANSI_RESET);
    }
}

package com.crowdin.cli;

import com.crowdin.cli.commands.Actions;
import com.crowdin.cli.commands.actions.CliActions;
import com.crowdin.cli.commands.picocli.CommandNames;
import com.crowdin.cli.commands.picocli.PicocliRunner;
import com.crowdin.cli.properties.PropertiesBuilders;

public class Cli {

    public static void main(String[] args) {
        try {
            PicocliRunner picocliRunner = PicocliRunner.getInstance();
            Actions actions = new CliActions();
            PropertiesBuilders propertiesBuilders = new PropertiesBuilders();
            int exitCode = picocliRunner.execute(actions, propertiesBuilders, args);
            if (exitCode != -1 && !picocliRunner.hasMatchedArg("plain")) {
                picocliRunner.execute(actions, propertiesBuilders, CommandNames.CHECK_NEW_VERSION);
            }

            System.exit(exitCode);
        } catch (Exception e) {
            System.out.println("There is exception:");
            e.printStackTrace();
        }
    }
}

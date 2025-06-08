package org.example;

import org.example.loader.CustomClassLoader;
import org.example.plugin.MenuAction;
import org.example.ui.MainMenuConsole;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        try {
            CustomClassLoader loader = new CustomClassLoader(Path.of("external_plugins"));
            Class<?> clazz = loader.loadClass("ProjectStarter");
            MenuAction action = (MenuAction) clazz.getDeclaredConstructor().newInstance();

            action.run();

        } catch (Exception e) {
            e.printStackTrace();
        }

        new MainMenuConsole().mainMenu();
    }
}

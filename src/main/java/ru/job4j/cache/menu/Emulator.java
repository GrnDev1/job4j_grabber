package ru.job4j.cache.menu;

import ru.job4j.cache.DirFileCache;

import java.util.Scanner;

public class Emulator {
    public static final int SELECT_DIR = 1;
    public static final int LOAD_INFO = 2;
    public static final int GET_INFO = 3;
    public static final String PATH = "Введите относительный путь до директории:";
    public static final String FILENAME = "Введите относительный путь до файла";
    public static final String EXIT = "Конец работы";
    public static final String SELECT = "Выберите меню";

    public static final String MENU = """
                Введите 1, чтобы указать кэшируемую директорию (относительный путь).
                Введите 2, чтобы загрузить содержимое файла в кэш.
                Введите 3, получить содержимое файла из кэша.
                Введите любое другое число для выхода.
            """;

    private static DirFileCache validateDir(Scanner scanner, DirFileCache dirFileCache) {
        if (dirFileCache == null) {
            System.out.println(PATH);
            dirFileCache = new DirFileCache(scanner.nextLine());
            System.out.println("Директория сохранена");
        }
        return dirFileCache;
    }

    private static void start(Scanner scanner) {
        DirFileCache dirFileCache = null;
        boolean run = true;
        while (run) {
            System.out.println(MENU);
            System.out.println(SELECT);
            int userChoice = Integer.parseInt(scanner.nextLine());
            if (SELECT_DIR == userChoice) {
                dirFileCache = validateDir(scanner, dirFileCache);
            } else if (LOAD_INFO == userChoice) {
                dirFileCache = validateDir(scanner, dirFileCache);
                System.out.println(FILENAME);
                dirFileCache.get(scanner.nextLine());
                System.out.println("Данные загружены!");
            } else if (GET_INFO == userChoice) {
                dirFileCache = validateDir(scanner, dirFileCache);
                System.out.println(FILENAME);
                System.out.println(dirFileCache.get(scanner.nextLine()));
            } else {
                run = false;
                System.out.println(EXIT);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        start(scanner);
    }
}

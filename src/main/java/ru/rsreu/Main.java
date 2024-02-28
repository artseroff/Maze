package ru.rsreu;

import ru.rsreu.exception.InterruptProgramException;
import ru.rsreu.generator.DFSMazeGenerator;
import ru.rsreu.generator.EllerMazeGenerator;
import ru.rsreu.generator.MazeGenerator;
import ru.rsreu.searcher.BFSPathSearcher;
import ru.rsreu.searcher.Coordinate;
import ru.rsreu.searcher.DFSPathSearcher;
import ru.rsreu.searcher.PathSearcher;
import ru.rsreu.view.ConsoleMazeRenderer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String WRONG_INPUT = "Неверный ввод, повторите попытку";
    private static final String WRONG_NUMBER = "Неверный номер алгоритма";

    private Main() {
    }

    public static void main(String[] args) {
        LOGGER.info("""
            |Вы вошли в программу, позволяющую генерировать и искать решение лабиринтов"
            |. Чтобы выйти нажмите Ctrl + D""");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                Maze maze = generateMaze(reader, chooseGenerator(reader));
                LOGGER.info("Сгенерированный лабиринт:" + ConsoleMazeRenderer.getMazeView(maze));
                processSolutions(reader, processFinder(reader), maze);
                LOGGER.info("Создать новый лабиринт? (да/нет)");
                String input = null;
                try {
                    input = getInput(reader);
                } catch (IOException ex) {
                    LOGGER.warn(WRONG_INPUT);
                }
                if (!"да".equalsIgnoreCase(input)) {
                    break;
                }
            }
        } catch (InterruptProgramException e) {
            LOGGER.info("Выход...");
        } catch (IOException e) {
            LOGGER.error("Произошла непредвиденная ошибка ввода-вывода");
        }

    }

    private static int chooseGenerator(BufferedReader reader) throws InterruptProgramException {
        LOGGER.info("""
            Выберите алгоритм генерации лабиринта (введите номер алгоритма):
            1 - генерация дерева с помощью обхода в глубину,
            2 - генерация методом Эллера""");
        int generatorNumber = 0;
        while (generatorNumber == 0) {
            try {
                generatorNumber = Integer.parseInt(getInput(reader));
                if (generatorNumber < 1 || generatorNumber > 2) {
                    generatorNumber = 0;
                    LOGGER.warn(WRONG_INPUT);
                }

            } catch (NumberFormatException | IOException e) {
                LOGGER.warn(WRONG_INPUT);
            }
        }
        return generatorNumber;
    }

    private static Maze generateMaze(BufferedReader reader, int generatorNumber)
        throws InterruptProgramException {
        LOGGER.info(("Введите размеры лабиринта (размер лабиринта должен быть не более %dx%d\n"
            + "Стенки не учитываются)").formatted(
            MazeGenerator.MAX_ROWS,
            MazeGenerator.MAX_COLS
        ));
        int rows;
        int cols;
        MazeGenerator generator = null;
        while (generator == null) {
            try {
                LOGGER.info("Количество строк лабиринта: ");
                rows = Integer.parseInt(getInput(reader));
                LOGGER.info("Количество столбцов лабиринта: ");
                cols = Integer.parseInt(getInput(reader));
                generator = switch (generatorNumber) {
                    case 1 -> new DFSMazeGenerator(rows, cols);
                    case 2 -> new EllerMazeGenerator(rows, cols);
                    default -> throw new IllegalArgumentException(WRONG_NUMBER);
                };
                break;
            } catch (NumberFormatException | IOException e) {
                LOGGER.warn(WRONG_INPUT);
            } catch (IllegalArgumentException e) {
                LOGGER.warn(e.getMessage());
            }
        }

        return generator.generate();
    }

    private static void processSolutions(BufferedReader reader, int searcherNumber, Maze maze)
        throws InterruptProgramException {

        while (true) {
            LOGGER.info("Запустить поиск решения в этом лабиринте? (да/нет)");
            String input = null;
            try {
                input = getInput(reader);
            } catch (IOException ex) {
                LOGGER.warn(WRONG_INPUT);
            }
            if (!"да".equalsIgnoreCase(input)) {
                break;
            }

            var path = findPath(reader, searcherNumber, maze);
            if (path.isEmpty()) {
                LOGGER.warn("Для введенных координат решения не существует");
            } else {
                LOGGER.info("Найденное решение:" + ConsoleMazeRenderer.getMazeView(maze, path));
            }
        }
    }

    private static int processFinder(BufferedReader reader) throws InterruptProgramException {
        LOGGER.info("""
            Выберите алгоритм решения лабиринта (введите номер алгоритма):
            1 - поиск в глубину,
            2 - поиск в ширину""");
        int algorithm = 0;
        while (algorithm == 0) {
            try {
                algorithm = Integer.parseInt(getInput(reader));
                if (algorithm < 1 || algorithm > 2) {
                    algorithm = 0;
                    LOGGER.warn(WRONG_INPUT);
                }
            } catch (IOException e) {
                LOGGER.warn(WRONG_INPUT);
            }
        }
        return algorithm;
    }

    @SuppressWarnings("MagicNumber")
    private static List<Coordinate> findPath(BufferedReader reader, int searcherNumber, Maze maze)
        throws InterruptProgramException {
        LOGGER.info("Введите координаты начальной и конечной клеток\n"
            + "(4 цифры через пробел в формате \"x1 y1 x2 y2\", где 1 - начальная клетка, 2 - конечная):");
        while (true) {
            try {
                String[] input = getInput(reader).split("\\s");
                if (input.length != 4) {
                    LOGGER.warn(WRONG_INPUT);
                    continue;
                }
                var start = new Coordinate(Integer.parseInt(input[1]) - 1, Integer.parseInt(input[0]) - 1);
                var end = new Coordinate(Integer.parseInt(input[3]) - 1, Integer.parseInt(input[2]) - 1);
                PathSearcher searcher = switch (searcherNumber) {
                    case 1 -> new DFSPathSearcher(maze, start, end);
                    case 2 -> new BFSPathSearcher(maze, start, end);
                    default -> throw new IllegalArgumentException(WRONG_NUMBER);
                };
                return searcher.search();
            } catch (NumberFormatException | IOException e) {
                LOGGER.warn(WRONG_INPUT);
            } catch (IllegalArgumentException e) {
                LOGGER.warn(e.getMessage());
            }
        }
    }

    private static String getInput(BufferedReader reader) throws IOException, InterruptProgramException {
        String input = reader.readLine();
        if (input == null) {
            throw new InterruptProgramException();
        }
        return input.trim();
    }
}

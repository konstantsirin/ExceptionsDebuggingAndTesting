import java.util.Scanner;
import Exception.IncorrectNameException;
import Exception.IncorrectPhoneException;
import Exception.IncorrectEmailException;
import Exception.NotKeyInStorageException;
import org.apache.logging.log4j.*;

public class Main {
    // private static final Logger LOGGER = LogManager.getLogger(Main.class);
    private static final Logger LOGGER = LogManager.getRootLogger();

    private static final Marker INPUT_HISTORY_MARKER = MarkerManager.getMarker("INPUT_HISTORY");
    private static final Marker ERROR_MARKER = MarkerManager.getMarker("ERROR");
    private static final String ADD_COMMAND = "add Василий Петров " +
            "vasily.petrov@gmail.com +79215637722";
    private static final String COMMAND_EXAMPLES = "\t" + ADD_COMMAND + "\n" +
            "\tlist\n\tcount\n\tremove Василий Петров";
    private static final String COMMAND_ERROR = "Wrong command! Available command examples: \n" +
            COMMAND_EXAMPLES;
    private static final String helpText = "Command examples:\n" + COMMAND_EXAMPLES;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CustomerStorage executor = new CustomerStorage();

        while (true) {
            String command = scanner.nextLine();
            String[] tokens = command.split("\\s+", 2);
            LOGGER.info(INPUT_HISTORY_MARKER, "Пользователь ввел команду {}", command);
            try {
                if (tokens[0].equals("add")) {
                    LOGGER.info(INPUT_HISTORY_MARKER, "Пользователь ввел команду add со значением {}", tokens[1]);
                    executor.addCustomer(tokens[1]);
                } else if (tokens[0].equals("list")) {
                    LOGGER.info(INPUT_HISTORY_MARKER, "Пользователь запросил список пользователей");
                    executor.listCustomers();
                } else if (tokens[0].equals("remove")) {
                    LOGGER.info(INPUT_HISTORY_MARKER, "Пользователь удаляет пользователя {}", tokens[1]);
                    executor.removeCustomer(tokens[1]);
                } else if (tokens[0].equals("count")) {
                    System.out.println("There are " + executor.getCount() + " customers");
                    LOGGER.info(INPUT_HISTORY_MARKER, "There are {} customers", executor.getCount());
                } else if (tokens[0].equals("help")) {
                    System.out.println(helpText);
                    LOGGER.info(INPUT_HISTORY_MARKER, helpText);
                } else {
                    System.out.println(COMMAND_ERROR);
                    LOGGER.info(INPUT_HISTORY_MARKER, COMMAND_ERROR);
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                System.out.println("Передано неверное количество аргуметов");
                LOGGER.warn(ERROR_MARKER, "======================================");
                LOGGER.warn(ERROR_MARKER, "Передано неверное количество аргуметов");
                LOGGER.warn(ERROR_MARKER, "Stack trace", ex);
                // ex.printStackTrace(System.out);
            } catch (IncorrectNameException | IncorrectPhoneException | IncorrectEmailException ex) {
                System.out.println(ex.getMessage());
                LOGGER.warn(ERROR_MARKER, "======================================");
                LOGGER.warn(ERROR_MARKER, ex.getMessage());
                LOGGER.warn(ERROR_MARKER, "Stack trace", ex);
                // ex.printStackTrace(System.out);
            } catch (NotKeyInStorageException ex) {
                System.out.println(ex.getMessage());
                LOGGER.warn(ERROR_MARKER, "======================================");
                LOGGER.warn(ERROR_MARKER, ex.getMessage());
                LOGGER.warn(ERROR_MARKER, "Stack trace", ex);
                // ex.printStackTrace(System.out);
            }


        }
    }
}

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Exception.IncorrectNameException;
import Exception.IncorrectPhoneException;
import Exception.IncorrectEmailException;
import Exception.NotKeyInStorageException;
public class CustomerStorage {
    private final Map<String, Customer> storage;

    public CustomerStorage() {
        storage = new HashMap<>();
    }

    public void addCustomer(String data) throws ArrayIndexOutOfBoundsException, IncorrectPhoneException, IncorrectEmailException, IncorrectNameException {
        final int INDEX_NAME = 0;
        final int INDEX_SURNAME = 1;
        final int INDEX_EMAIL = 2;
        final int INDEX_PHONE = 3;

        String[] components = data.split("\\s+");
        if (components.length > 4) {
            throw new ArrayIndexOutOfBoundsException("Передано больше аргументов, чем необходимо");
        }
        String name = components[INDEX_NAME] + " " + components[INDEX_SURNAME];
        Pattern patternName = Pattern.compile("^([А-Яа-яЁёA-Za-z]+)(\\s+)([А-Яа-яЁёA-Za-z]+)$");
        Pattern patternPhone = Pattern.compile("^(\\+)?(\\d{11})$");
        Pattern patternEmail = Pattern.compile("^(.*)@(.*).(.*)$");

        Matcher matcherName = patternName.matcher(name);
        Matcher matcherPhone = patternPhone.matcher(components[INDEX_PHONE]);
        Matcher matcherEmail = patternEmail.matcher(components[INDEX_EMAIL]);

        if (!matcherName.find()) {
            throw new IncorrectNameException("Имя может содержать только буквы");
        }

        if (!matcherPhone.find()) {
            throw new IncorrectPhoneException("Некорректный номер телефона");
        }

        if (!matcherEmail.find()) {
            throw new IncorrectEmailException("Некорректный e-mail");
        }

        storage.put(name, new Customer(name, components[INDEX_PHONE], components[INDEX_EMAIL]));
    }

    public void listCustomers() {
        storage.values().forEach(System.out::println);
    }

    public void removeCustomer(String name) throws NotKeyInStorageException {
        if (!storage.containsKey(name)) {
            throw new NotKeyInStorageException("В хранилище отсутствует пользователь " + name + ". Удаление невозможно.");
        }
        storage.remove(name);
    }

    public Customer getCustomer(String name) {
        return storage.get(name);
    }

    public int getCount() {
        return storage.size();
    }
}
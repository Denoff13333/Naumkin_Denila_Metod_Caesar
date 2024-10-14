import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class Main {

    private static final String RUS_ALPHABET = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private static final String ENG_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Выберите режим:");
        System.out.println("1. Шифрование");
        System.out.println("2. Расшифровка");
        System.out.println("3. Brute Force");
        int choice = scanner.nextInt();

        System.out.println("Введите путь к файлу:");
        String filePath = scanner.next();

        if (!validateFile(filePath)) {
            System.out.println("Файл не найден!");
            return;
        }

        System.out.println("Введите ключ (целое число):");
        while (!scanner.hasNextInt()) {
            System.out.println("Ошибка! Введите целое число для ключа:");
            scanner.next();
        }
        int key = scanner.nextInt();

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            switch (choice) {
                case 1:
                    String encryptedText = caesarCipher(content, key, true);
                    Files.write(Paths.get("encrypted.txt"), encryptedText.getBytes());
                    System.out.println("Файл успешно зашифрован и сохранен как 'encrypted.txt'.");
                    break;
                case 2:
                    String decryptedText = caesarCipher(content, key, false);
                    Files.write(Paths.get("decrypted.txt"), decryptedText.getBytes());
                    System.out.println("Файл успешно расшифрован и сохранен как 'decrypted.txt'.");
                    break;
                case 3:
                    bruteForce(content);
                    break;
                default:
                    System.out.println("Неверный выбор!");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при работе с файлами: " + e.getMessage());
        }
    }

    private static boolean validateFile(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    public static String caesarCipher(String text, int key, boolean encrypt) {
        StringBuilder result = new StringBuilder();
        int shift = encrypt ? key : -key;

        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                result.append(shiftCharacter(ch, shift));
            } else {
                result.append(ch);
            }
        }

        return result.toString();
    }

    public static char shiftCharacter(char ch, int shift) {
        String alphabet = Character.isUpperCase(ch) ? ENG_ALPHABET : ENG_ALPHABET.toLowerCase();

        if (RUS_ALPHABET.indexOf(Character.toUpperCase(ch)) != -1) {
            alphabet = Character.isUpperCase(ch) ? RUS_ALPHABET : RUS_ALPHABET.toLowerCase();
        }

        int originalIndex = alphabet.indexOf(ch);
        int newIndex = (originalIndex + shift) % alphabet.length();

        if (newIndex < 0) {
            newIndex += alphabet.length();
        }

        return alphabet.charAt(newIndex);
    }

    public static void bruteForce(String text) {
        for (int key = 1; key <= 25; key++) {
            String decrypted = caesarCipher(text, key, false);
            System.out.println("Ключ " + key + ": " + decrypted);
        }
    }
}

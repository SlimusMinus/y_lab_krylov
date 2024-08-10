package org.example.in;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тестовый класс для проверки метода {@code checkInput} в классе {@code InputAuthData}.
 * <p>
 * Данный класс содержит тесты, которые проверяют корректность обработки ввода пользователем данных
 * с консоли и вывод соответствующих сообщений об ошибках.
 * </p>
 */
@DisplayName("Проверка класса InputAuthData на ")
class InputAuthDataTest {

    /**
     * Проверяет метод {@code checkInput}, чтобы убедиться, что некорректный ввод обрабатывается правильно.
     * <p>
     * В этом тесте эмулируется ввод с консоли, который содержит неверное значение, за которым следует правильное число.
     * Метод должен игнорировать некорректное значение и принять правильное число, при этом выводя сообщение об ошибке.
     * </p>
     */
    @Test
    @DisplayName("обработку некорректного и корректного ввода")
    void checkInput() {
        String input = "abc\n2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        int result = InputAuthData.checkInput(3);
        assertThat(result).isEqualTo(2);
        assertThat(outputStream.toString()).contains("Некорректный ввод. Пожалуйста, введите число.");
    }
}
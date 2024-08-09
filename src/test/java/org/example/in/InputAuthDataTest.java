package org.example.in;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

class InputAuthDataTest {

    @Test
    void checkInput() {
        String input = "abc\n2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Scanner scanner = new Scanner(System.in);
        int result = InputAuthData.checkInput(3);
        assertThat(result).isEqualTo(2);
        assertThat(outputStream.toString()).contains("Некорректный ввод. Пожалуйста, введите число.");
    }
}
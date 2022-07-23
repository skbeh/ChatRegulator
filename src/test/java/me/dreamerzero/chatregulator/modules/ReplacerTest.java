package me.dreamerzero.chatregulator.modules;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReplacerTest {
    @Test
    @DisplayName("First Letter Uppercase")
    void firstLetterUppercase(){
        String original = "peruviankkit";
        String expected = "Peruviankkit";

        String replaced = Replacer.firstLetterUpercase(original);
        assertEquals(replaced, expected);
    }

    @Test
    @DisplayName("Final Dot")
    void finalDot(){
        String original = "peruviankkit";
        String expected = "peruviankkit.";

        String replaced = Replacer.addFinalDot(original);

        assertEquals(replaced, expected);
    }

    @Test
    @DisplayName("Full Format")
    void applyFullFormat(){
        String original = "peruviankkit";
        String expected = "Peruviankkit.";

        String replaced = Replacer.applyFormat(original);

        assertEquals(replaced, expected);
    }
}

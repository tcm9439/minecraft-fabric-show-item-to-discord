package net.maisyt.util.string;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class NamedPlaceholderStringTest {

    @Test
    void formatTest() {
        NamedPlaceholderString stringToSubstitute =
                new NamedPlaceholderString("Hello ${Name}. Here is ${Robot}. Can I call you ${Name}?");
        NamedPlaceholderString chineseStringToSubstitute =
                new NamedPlaceholderString("安安，${Name}。我是${Robot}。");

         Map<String, String> values = new HashMap<>();
         values.put("Name", "Klein");
         values.put("Robot", "bot-Y17");

         String expected = "Hello Klein. Here is bot-Y17. Can I call you Klein?";
         String result = stringToSubstitute.format(values);
         assertEquals(expected, result);

         expected = "安安，Klein。我是bot-Y17。";
         result = chineseStringToSubstitute.format(values);
         assertEquals(expected, result);

    }
}
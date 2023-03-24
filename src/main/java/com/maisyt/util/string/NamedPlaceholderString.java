package com.maisyt.util.string;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Named Placeholder : ${PlaceholderName}
 */
public class NamedPlaceholderString {
    /**
     * String with placeholders in format of "Some text ${PlaceholderName} some other text"
     */
    String stringWithPlaceholders;

    public NamedPlaceholderString(String stringWithPlaceholders) {
        this.stringWithPlaceholders = stringWithPlaceholders;
    }

    /**
     * Format a string with placeholders with supplied values.
     * @param placeholderValues Map of placeholders name to value to plug in
     * @return the String with placeholders key replaced by the values
     */
    public String format(Map<String, String> placeholderValues) {
        StringBuilder formatter = new StringBuilder(stringWithPlaceholders);
        List<String> reorderPlaceHolderValues = new ArrayList<>();

        Matcher matcher = Pattern.compile("\\$\\{(\\w+)}").matcher(stringWithPlaceholders);

        while (matcher.find()) { // get next group
            String placeholderKey = matcher.group(1);
            String fullPlaceholderKey = String.format("${%s}", placeholderKey);
            int index = formatter.indexOf(fullPlaceholderKey);

            if (index != -1) {
                formatter.replace(index, index + fullPlaceholderKey.length(), "%s");
                reorderPlaceHolderValues.add(placeholderValues.get(placeholderKey));
            }
        }

        return String.format(formatter.toString(), reorderPlaceHolderValues.toArray());
    }
}


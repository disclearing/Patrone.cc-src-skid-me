package zone.potion.server.filter;

import com.google.common.collect.ImmutableList;
import zone.potion.utils.StringUtil;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

public class Filter {
    private static final List<String> FILTERED_PHRASES = ImmutableList.of(
    );
    private static final String[] SINGLE_FILTERED_WORDS = {""};
    private static final String[] WHITELISTED_LINKS = {
            ""
    };

    public boolean isFiltered(String msg) {
        if (true) {
            return false;
        }
        msg = msg.toLowerCase().trim();

        for (String word : msg.split(" ")) {
            Matcher matcher = StringUtil.IP_REGEX.matcher(word);

            if (matcher.matches()) {
                return true;
            }
        }

        for (String word : msg
                .replace("3", "e")
                .replace("1", "i")
                .replace("!", "i")
                .replace("/\\", "a")
                .replace("/-\\", "a")
                .replace("()", "o")
                .replace("2", "z")
                .replace("@", "a")
                .replace("|", "l")
                .replace("7", "t")
                .replace("4", "a")
                .replace("0", "o")
                .replace("5", "s")
                .replace("8", "b")
                .trim().split(" ")) {
            Matcher matcher = StringUtil.URL_REGEX.matcher(word);

            boolean filtered = false;

            if (matcher.matches()) {
                int matches = 0;

                for (String link : WHITELISTED_LINKS) {
                    if (word.contains(link)) {
                        matches++;
                    }
                }

                filtered = matches == 0;
            }

            if (filtered) {
                return true;
            }
        }

        for (String word : msg
                .replace("3", "e")
                .replace("1", "i")
                .replace("!", "i")
                .replace("/\\", "a")
                .replace("/-\\", "a")
                .replace("()", "o")
                .replace("2", "z")
                .replace("@", "a")
                .replace("|", "l")
                .replace("7", "t")
                .replace("4", "a")
                .replace("0", "o")
                .replace("5", "s")
                .replace("8", "b")
                .replace(" ", "")
                .trim().split(" ")) {
            Matcher matcher = StringUtil.URL_REGEX.matcher(word);

            boolean filtered = false;

            if (matcher.matches()) {
                int matches = 0;

                for (String link : WHITELISTED_LINKS) {
                    if (word.contains(link)) {
                        matches++;
                    }
                }

                filtered = matches == 0;
            }

            if (filtered) {
                return true;
            }
        }

        for (String word : msg
                .replace("3", "e")
                .replace("1", "i")
                .replace("!", "i")
                .replace("/\\", "a")
                .replace("/-\\", "a")
                .replace("()", "o")
                .replace("2", "z")
                .replace("@", "a")
                .replace("|", "l")
                .replace("7", "t")
                .replace("4", "a")
                .replace("0", "o")
                .replace("5", "s")
                .replace("8", "b")
                .trim().replaceAll("\\p{Punct}|\\d", ":").replace(":dot:", ".").split(" ")) {
            Matcher matcher = StringUtil.URL_REGEX.matcher(word);

            boolean filtered = false;

            if (matcher.matches()) {
                int matches = 0;

                for (String link : WHITELISTED_LINKS) {
                    if (word.contains(link)) {
                        matches++;
                    }
                }

                filtered = matches == 0;
            }

            if (filtered) {
                return true;
            }
        }


        for (String word : msg
                .replace("3", "e")
                .replace("1", "i")
                .replace("!", "i")
                .replace("/\\", "a")
                .replace("/-\\", "a")
                .replace("()", "o")
                .replace("2", "z")
                .replace("@", "a")
                .replace("|", "l")
                .replace("7", "t")
                .replace("4", "a")
                .replace("0", "o")
                .replace("5", "s")
                .replace("8", "b")
                .replace(" ", "")
                .trim().replaceAll("\\p{Punct}|\\d", ":").replace(":dot:", ".").split(" ")) {
            Matcher matcher = StringUtil.URL_REGEX.matcher(word);

            boolean filtered = false;

            if (matcher.matches()) {
                int matches = 0;

                for (String link : WHITELISTED_LINKS) {
                    if (word.contains(link)) {
                        matches++;
                    }
                }

                filtered = matches == 0;
            }

            if (filtered) {
                return true;
            }
        }

        String parsed = msg
                .replace("3", "e")
                .replace("1", "i")
                .replace("!", "i")
                .replace("/\\", "a")
                .replace("/-\\", "a")
                .replace("()", "o")
                .replace("2", "z")
                .replace("@", "a")
                .replace("|", "l")
                .replace("7", "t")
                .replace("4", "a")
                .replace("0", "o")
                .replace("5", "s")
                .replace("8", "b")
                .trim();

        String noPuncParsed = parsed.replaceAll("\\p{Punct}|\\d", "").trim();

        for (String word : SINGLE_FILTERED_WORDS) {
            if (noPuncParsed.equalsIgnoreCase(word) || noPuncParsed.startsWith(word + " ")
                    || noPuncParsed.endsWith(" " + word) || noPuncParsed.contains(" " + word + " ")) {
                return true;
            }
        }

        for (String phrase : FILTERED_PHRASES) {
            if (parsed.contains(phrase)) {
                return true;
            }
        }

        Optional<String> filterablePhrase = FILTERED_PHRASES.stream().map(phrase -> phrase.replaceAll(" ", "")).filter(parsed::contains).findFirst();

        if (filterablePhrase.isPresent()) {
            return true;
        }

        String[] split = parsed.trim().split(" ");

        for (String word : split) {
            if (FILTERED_PHRASES.contains(word)) {
                return true;
            }
        }

        return false;
    }
}

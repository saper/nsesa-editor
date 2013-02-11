/**
 * Copyright 2013 European Parliament
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations under the Licence.
 */
package org.nsesa.editor.gwt.core.client.ui.overlay;


import java.util.LinkedHashMap;

/**
 * @author <a href="philip.luppens@gmail.com">Philip Luppens</a>
 * @version $Id: TextUtils.java 5027 2012-02-17 14:01:17Z pluppens $
 */
public class TextUtils {
    // xml conversion: & - &amp; < - &lt; > - &gt; " - &quot; ' - &apos;
    private static final LinkedHashMap<Character, String> XML_ESCAPER = new LinkedHashMap<Character, String>();

    static {
        XML_ESCAPER.put(new Character('&'), "&amp;");
        XML_ESCAPER.put(new Character('<'), "&lt;");
        XML_ESCAPER.put(new Character('>'), "&gt;");
        XML_ESCAPER.put(new Character('"'), "&quot;");
        XML_ESCAPER.put(new Character('\''), "&apos;");
    }

    public static String stripTags(String html, boolean collapseWhiteSpace) {
        if (html == null)
            return null;
        if ("".equals(html.trim()))
            return "";

        html = html.replaceAll("<.*?>", "");
        if (collapseWhiteSpace) {
            // collapse all the whitespace
            html = collapseWhiteSpace(html);
        }
        return html.trim();
    }

    public static String collapseWhiteSpace(String content) {
        return content.replaceAll("\\s+", " ");
    }

    public static String limit(String input, int max) {
        if (input.length() > max) {
            input = input.substring(0, max - 4) + " ...";
        }
        return input;
    }

    public static String capitalize(String input) {
        if (input != null) {
            if ("".equals(input.trim()))
                return input;
            return input.substring(0, 1).toUpperCase() + input.substring(1);
        }
        return null;
    }

    /**
     * Strips all HTML tags
     *
     * @param {Mixed} value The text from which to strip tags
     * @return {String} The stripped text
     */
    public static String stripTags(String html) {
        return html.replaceAll("\\<.*?\\>", "");
    }

    private final static char[] alphabet;

    static {
        // create the alphabet - a to b
        alphabet = new char[26];
        for (int i = 0; i < alphabet.length; i++) {
            alphabet[i] = (char) (i + (int) 'a');
        }
    }

    public static String getLiteralForNumber(int index) {
        if (index > alphabet.length * alphabet.length)
            throw new IllegalArgumentException("Cannot serve a number greater than " +
                    alphabet.length * alphabet.length);
        /*
       if the index is bigger than the number of chars in the alphabet,
       we'll add the the extra char equal to the number of times
       this index will overflow the alphabet.
        */
        if (index > alphabet.length) {
            return String.valueOf((alphabet[index / alphabet.length]) + alphabet[index % alphabet.length]);
        }
        return String.valueOf(alphabet[index]);
    }

    public static String markBoldItalic(String input) {
        if (input == null) return null;
        return "<span class='bold_italic'>" + input + "</span>";
    }


    public static String dump(StringBuilder sb, int level, Throwable e) {

        for (StackTraceElement traceElement : e.getStackTrace()) {
            sb.append(traceElement.getMethodName()).append(" in ").append(traceElement.getClassName()).append(" at line ").append(traceElement.getLineNumber()).append("\n");
        }
        if (e.getCause() != null) {
            sb.append("\n\n------------ UNDERLYING CAUSE-----------------\n\n");
            sb.append(dump(sb, ++level, e.getCause()));
        }
        return sb.toString();
    }


    public static boolean hasLength(String str) {
        return (str != null && str.length() > 0);
    }

    /**
     * Escape all xml special chars with their corresponding values as follows
     *
     * @param str The string to be escaped
     * @return The string escaped
     */
    public static String escapeXML(String str) {
        if (str == null) return null;
        StringBuilder sb = new StringBuilder(str.length() * 2);
        for (int i = 0; i < str.length(); i++) {
            Character ch = new Character(str.charAt(i));
            String escape = XML_ESCAPER.get(ch);
            sb.append(escape == null ? ch.charValue() : escape);
        }
        return sb.toString();
    }

    public static boolean hasText(String str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            // isWhitespace is not supported yet by gwt
            if (!Character.isSpace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether a string starts with digits and might contains after digits only letters
     *
     * @param str
     * @return
     */
    public static boolean startsWithDigitsFollowedByLetters(String str) {
        int strLen = str.length();
        boolean startsWithDigits = false;

        int startForLetters = 0;
        for (int i = 0; i < strLen; i++) {
            if (Character.isDigit(str.charAt(i))) {
                startsWithDigits = true;
                continue;
            }
            ;
            startForLetters = i;
            break;
        }
        boolean followedByLetters = true;
        if (startsWithDigits && startForLetters > 0) {
            for (int i = startForLetters; i < strLen; i++) {
                if (!Character.isLetter(str.charAt(i))) {
                    followedByLetters = false;
                    break;
                }
                ;
            }
        }
        return startsWithDigits && followedByLetters;

    }

    private static final String DOCUMENT_START_TAG = "<document>";
    private static final String DOCUMENT_END_TAG = "</document>";
    private static final String BOM_CHAR = "\uFEFF";

    /**
     * Strip start and end document tags
     *
     * @param html
     * @return
     */
    public static String stripDocumentTags(String html) {
        if (html == null) {
            return null;
        }
        html = html.trim();
        if (html.startsWith(BOM_CHAR)) {
            html = html.substring(1);
        }
        if (html.startsWith(DOCUMENT_START_TAG)) {
            html = html.substring(DOCUMENT_START_TAG.length());
        }
        if (html.endsWith(DOCUMENT_END_TAG)) {
            html = html.substring(0, html.length() - DOCUMENT_END_TAG.length());
        }
        return html;
    }

    /**
     * Creates a <tt>String</tt> of <tt>amount</tt> * <tt>indent</tt>.
     *
     * @param amount the number of times the indent string will be repeated.
     * @param indent the string part to repeat.
     * @return the repeated string.
     */
    public static String repeat(final int amount, final String indent) {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be less than 0.");
        if (indent == null) return null;

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            sb.append(indent);
        }
        return sb.toString();
    }
}

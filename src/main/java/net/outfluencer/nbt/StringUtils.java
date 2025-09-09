package net.outfluencer.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Utility class for string operations in NBT serialization/deserialization.
 */
final class StringUtils {

    private StringUtils() {
        // Utility class
    }

    /**
     * Reads a string from the input for tag names, including appropriate memory accounting.
     *
     * @param input   the input to read from
     * @param limiter the limiter for memory accounting
     * @return the read string
     * @throws IOException if an I/O error occurs
     */
    static String readTagName(DataInput input, NbtLimiter limiter) throws IOException {
        limiter.countBytes(Tag.STRING_SIZE);
        String string = input.readUTF();
        limiter.countBytes(string.length(), Character.BYTES);
        return string;
    }

    /**
     * Reads a string from the input for tag values, including appropriate memory accounting.
     *
     * @param input   the input to read from
     * @param limiter the limiter for memory accounting
     * @return the read string
     * @throws IOException if an I/O error occurs
     */
    static String readTagValue(DataInput input, NbtLimiter limiter) throws IOException {
        limiter.countBytes(Tag.OBJECT_HEADER + Tag.STRING_SIZE);
        String string = input.readUTF();
        limiter.countBytes(string.length(), Character.BYTES);
        return string;
    }

    /**
     * Writes a string to the output.
     *
     * @param string the string to write
     * @param output the output to write to
     * @throws IOException if an I/O error occurs
     */
    static void writeString(String string, DataOutput output) throws IOException {
        output.writeUTF(string);
    }
}
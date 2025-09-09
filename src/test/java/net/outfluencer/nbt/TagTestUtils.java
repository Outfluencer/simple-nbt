package net.outfluencer.nbt;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Utility class for testing NBT tag serialization/deserialization.
 */
final class TagTestUtils {

    private TagTestUtils() {
        // Utility class
    }

    /**
     * Tests that a tag can be serialized and deserialized correctly.
     *
     * @param tag the tag to test
     * @throws IOException if an I/O error occurs during serialization/deserialization
     */
    static void assertSerializationRoundtrip(Tag tag) throws IOException {
        byte[] serialized = Tag.toByteArray(tag);
        Tag deserialized = Tag.fromByteArray(serialized);
        assertEquals(tag, deserialized);
    }
}
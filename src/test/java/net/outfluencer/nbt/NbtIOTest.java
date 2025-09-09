package net.outfluencer.nbt;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NbtIOTest {

    @Test
    public void testByteTag() throws IOException {
        TagTestUtils.assertSerializationRoundtrip(new ByteTag((byte) 0));
        TagTestUtils.assertSerializationRoundtrip(new ByteTag(Byte.MAX_VALUE));
        TagTestUtils.assertSerializationRoundtrip(new ByteTag(Byte.MIN_VALUE));
    }

    @Test
    public void testShortTag() throws IOException {
        TagTestUtils.assertSerializationRoundtrip(new ShortTag((short) 0));
        TagTestUtils.assertSerializationRoundtrip(new ShortTag(Short.MAX_VALUE));
        TagTestUtils.assertSerializationRoundtrip(new ShortTag(Short.MIN_VALUE));
    }

    @Test
    public void testIntTag() throws IOException {
        TagTestUtils.assertSerializationRoundtrip(new IntTag(0));
        TagTestUtils.assertSerializationRoundtrip(new IntTag(Integer.MAX_VALUE));
        TagTestUtils.assertSerializationRoundtrip(new IntTag(Integer.MIN_VALUE));
    }

    @Test
    public void testLongTag() throws IOException {
        TagTestUtils.assertSerializationRoundtrip(new LongTag(0L));
        TagTestUtils.assertSerializationRoundtrip(new LongTag(Long.MAX_VALUE));
        TagTestUtils.assertSerializationRoundtrip(new LongTag(Long.MIN_VALUE));
    }

    @Test
    public void testDoubleTag() throws IOException {
        TagTestUtils.assertSerializationRoundtrip(new DoubleTag(0d));
        TagTestUtils.assertSerializationRoundtrip(new DoubleTag(Double.MAX_VALUE));
        TagTestUtils.assertSerializationRoundtrip(new DoubleTag(Double.MIN_VALUE));
        TagTestUtils.assertSerializationRoundtrip(new DoubleTag(Double.NaN));
        TagTestUtils.assertSerializationRoundtrip(new DoubleTag(Double.POSITIVE_INFINITY));
        TagTestUtils.assertSerializationRoundtrip(new DoubleTag(Double.NEGATIVE_INFINITY));
    }

    @Test
    public void testFloatTag() throws IOException {
        TagTestUtils.assertSerializationRoundtrip(new FloatTag(0f));
        TagTestUtils.assertSerializationRoundtrip(new FloatTag(Float.MAX_VALUE));
        TagTestUtils.assertSerializationRoundtrip(new FloatTag(Float.MIN_VALUE));
        TagTestUtils.assertSerializationRoundtrip(new FloatTag(Float.NaN));
        TagTestUtils.assertSerializationRoundtrip(new FloatTag(Float.POSITIVE_INFINITY));
        TagTestUtils.assertSerializationRoundtrip(new FloatTag(Float.NEGATIVE_INFINITY));
    }

    @Test
    public void testStringTag() throws IOException {
        TagTestUtils.assertSerializationRoundtrip(new StringTag("Outfluencer"));
        TagTestUtils.assertSerializationRoundtrip(new StringTag(""));
        TagTestUtils.assertSerializationRoundtrip(new StringTag(String.valueOf(System.currentTimeMillis())));
    }

    @Test
    public void testByteArrayTag() throws IOException {
        byte[] largeArray = new byte[1 << 20];
        ThreadLocalRandom.current().nextBytes(largeArray);
        TagTestUtils.assertSerializationRoundtrip(new ByteArrayTag(largeArray));
        
        TagTestUtils.assertSerializationRoundtrip(new ByteArrayTag(new byte[0]));
    }

    @Test
    public void testIntArrayTag() throws IOException {
        int[] largeArray = new int[1 << 20];
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = ThreadLocalRandom.current().nextInt();
        }
        TagTestUtils.assertSerializationRoundtrip(new IntArrayTag(largeArray));
        
        TagTestUtils.assertSerializationRoundtrip(new IntArrayTag(new int[0]));
    }

    @Test
    public void testLongArrayTag() throws IOException {
        long[] largeArray = new long[1 << 20];
        for (int i = 0; i < largeArray.length; i++) {
            largeArray[i] = ThreadLocalRandom.current().nextLong();
        }
        TagTestUtils.assertSerializationRoundtrip(new LongArrayTag(largeArray));
        
        TagTestUtils.assertSerializationRoundtrip(new LongArrayTag(new long[0]));
    }

    @Test
    public void testListTag() throws IOException {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tags.add(new IntTag(i));
        }
        TagTestUtils.assertSerializationRoundtrip(new ListTag(tags, Tag.INT));

        // Test list with mismatched types throws exception
        List<Tag> mismatchedTags = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mismatchedTags.add(new IntTag(i));
        }
        mismatchedTags.add(new ByteTag(Byte.MIN_VALUE));
        assertThrows(NbtFormatException.class, () -> Tag.toByteArray(new ListTag(mismatchedTags, Tag.INT)));
        
        // Test list with END tag throws exception
        assertThrows(NbtFormatException.class, () -> Tag.toByteArray(new ListTag(Collections.singletonList(new EndTag()), Tag.END)));
    }

    @Test
    public void testCompoundTag() throws IOException {
        Map<String, Tag> map = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            map.put("" + i, new IntTag(i));
            map.put("a" + i, new ByteTag((byte) i));
            map.put("b" + i, new ShortTag((short) i));
            map.put("c" + i, new LongTag(i));
            map.put("f" + i, new FloatTag(i));
            map.put("d" + i, new DoubleTag(i));
        }
        TagTestUtils.assertSerializationRoundtrip(new CompoundTag(map));

        // Test compound with END tag throws exception
        Map<String, Tag> invalidMap = new HashMap<>();
        invalidMap.put("", new EndTag());
        assertThrows(NbtFormatException.class, () -> Tag.toByteArray(new CompoundTag(invalidMap)));
    }
}

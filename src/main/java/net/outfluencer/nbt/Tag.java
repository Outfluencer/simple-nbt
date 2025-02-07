package net.outfluencer.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.function.Supplier;

public interface Tag {

    Supplier<? extends Tag>[] CONSTRUCTORS = new Supplier[] {
      EndTag::new,
      ByteTag::new,
      ShortTag::new,
      IntTag::new,
      LongTag::new,
      FloatTag::new,
      DoubleTag::new,
      ByteArrayTag::new,
      StringTag::new,
      ListTag::new,
      CompoundTag::new,
      IntArrayTag::new,
      LongArrayTag::new
    };

    byte END = 0;
    byte BYTE = 1;
    byte SHORT = 2;
    byte INT = 3;
    byte LONG = 4;
    byte FLOAT = 5;
    byte DOUBLE = 6;
    byte BYTE_ARRAY = 7;
    byte STRING = 8;
    byte LIST = 9;
    byte COMPOUND = 10;
    byte INT_ARRAY = 11;
    byte LONG_ARRAY = 12;

    void read(DataInput input, NbtLimiter limiter) throws IOException;

    void write(DataOutput output) throws IOException;

    byte getId();

    static Tag createTag(byte id) {
        if (id < END || id > LONG_ARRAY) {
            throw new NbtFormatException("Invalid tag id: " + id);
        }
        return CONSTRUCTORS[id].get();
    }
}

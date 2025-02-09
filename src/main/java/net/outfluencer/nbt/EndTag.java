package net.outfluencer.nbt;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@Data
@NoArgsConstructor
public class EndTag implements Tag {

    public static final EndTag INSTANCE = new EndTag();

    @Override
    public void read(DataInput input, NbtLimiter limiter) throws IOException {
        limiter.countBytes(OBJECT_HEADER);
    }

    @Override
    public void write(DataOutput output) throws IOException {

    }

    @Override
    public byte getId() {
        return Tag.END;
    }
}

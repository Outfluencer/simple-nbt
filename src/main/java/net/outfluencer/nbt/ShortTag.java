package net.outfluencer.nbt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortTag implements Tag {

    private short value;

    @Override
    public void read(DataInput input, NbtLimiter limiter) throws IOException {
        limiter.countBytes(OBJECT_HEADER + Short.BYTES);
        value = input.readShort();
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeShort(value);
    }

    @Override
    public byte getId() {
        return Tag.SHORT;
    }
}

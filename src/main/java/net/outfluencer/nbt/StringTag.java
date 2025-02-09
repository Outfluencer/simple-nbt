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
public class StringTag implements Tag {

    private String value;

    @Override
    public void read(DataInput input, NbtLimiter limiter) throws IOException {
        limiter.countBytes(OBJECT_HEADER + STRING_SIZE);
        String string = input.readUTF();
        limiter.countBytes(string.length(), Character.BYTES);
        value = string;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeUTF(value);
    }

    @Override
    public byte getId() {
        return Tag.STRING;
    }
}

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
public class NamedTag implements Tag {
    private String name;
    private Tag tag;

    @Override
    public void read(DataInput input, NbtLimiter limiter) throws IOException {
        limiter.countBytes(48L);
        byte type = input.readByte();
        StringTag stringTag = new StringTag();
        stringTag.read(input, limiter);
        name = stringTag.getValue();
        tag = Tag.readById(type, input, limiter);
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeByte(tag.getId());
        StringTag stringTag = new StringTag();
        stringTag.setValue(name);
        stringTag.write(output);
        tag.write(output);
    }

    @Override
    public byte getId() {
        return -1;
    }

    /**
     * Reads the data of the {@link DataInput} and parses it into a {@link NamedTag}
     *
     * @param input input to read from
     * @param limiter limitation of the read data
     * @return the initialized {@link Tag}
     */
    public static Tag fromData(DataInput input, NbtLimiter limiter) throws IOException {
        NamedTag tag = new NamedTag();
        tag.read(input, limiter);
        return tag;
    }
}

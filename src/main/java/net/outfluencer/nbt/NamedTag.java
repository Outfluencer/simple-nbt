package net.outfluencer.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

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
        tag = Tag.createTag(type);
        tag.read(input, limiter);
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
}

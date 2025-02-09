package net.outfluencer.nbt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListTag implements Tag {

    public static final int LIST_HEADER = 12;

    private List<Tag> value;
    private byte listType;

    @Override
    public void read(DataInput input, NbtLimiter limiter) throws IOException {
        limiter.push();
        // I have no idea how we get to 37, maybe they add another 12 bytes for List object overhead
        // int headers = OBJECT_HEADER + ARRAY_HEADER; // 20
        // int withRead = headers + Byte.BYTES + Integer.BYTES; // 25
        // wea are off by 12 bytes but idk why mojang is counting these 12 bytes
        limiter.countBytes(OBJECT_HEADER + LIST_HEADER + ARRAY_HEADER + Byte.BYTES + Integer.BYTES);
        listType = input.readByte();
        int length = input.readInt();

        if (listType == Tag.END && length > 0) {
            throw new NbtFormatException("Missing type in ListTag");
        }

        limiter.countBytes(length, OBJECT_REFERENCE);
        List<Tag> tagList = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            tagList.add(Tag.readById(listType, input, limiter));
        }
        limiter.pop();

        value = tagList;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        if (listType == Tag.END && !value.isEmpty()) {
            throw new NbtFormatException("Missing type in ListTag");
        }
        output.writeByte(listType);
        output.writeInt(value.size());
        for (Tag tag : value) {
            if (tag.getId() != listType) {
                throw new NbtFormatException("ListTag type mismatch");
            }
            tag.write(output);
        }
    }

    @Override
    public byte getId() {
        return Tag.LIST;
    }
}

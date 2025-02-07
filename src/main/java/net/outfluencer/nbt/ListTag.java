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

    private List<Tag> value;
    private byte listType;

    @Override
    public void read(DataInput input, NbtLimiter limiter) throws IOException {
        limiter.push();
        limiter.countBytes(37L);
        listType = input.readByte();
        int length = input.readInt();

        if (listType == Tag.END && length > 0) {
            throw new NbtFormatException("Missing type in ListTag");
        }

        limiter.countBytes(length, 4L);
        List<Tag> tagList = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            Tag tag = Tag.createTag(listType);
            tag.read(input, limiter);
            tagList.add(tag);
        }
        limiter.pop();

        value = tagList;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        output.writeByte(getListType());
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

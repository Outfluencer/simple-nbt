package net.outfluencer.nbt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompoundTag implements Tag {

    private Map<String, Tag> value;

    @Override
    public void read(DataInput input, NbtLimiter limiter) throws IOException {
        limiter.push();
        limiter.countBytes(48L);
        Map<String, Tag> map = new HashMap<>();
        for (byte type; (type = input.readByte()) != 0; ) {
            String name = readString(input, limiter);
            Tag tag = Tag.readById(type, input, limiter);
            if (map.put(name, tag) == null) {
                limiter.countBytes(36L);
            }
        }
        limiter.pop();
        value = map;
    }

    private String readString(DataInput input, NbtLimiter limiter) throws IOException {
        limiter.countBytes(28L);
        String string = input.readUTF();
        limiter.countBytes(string.length(), 2);
        return string;
    }

    @Override
    public void write(DataOutput output) throws IOException {
        for (Map.Entry<String, Tag> entry : value.entrySet()) {
            String name = entry.getKey();
            Tag tag = entry.getValue();
            output.writeByte(tag.getId());
            if (tag.getId() != 0) {
                output.writeUTF(name);
                tag.write(output);
            }
        }
        output.writeByte(0);
    }

    @Override
    public byte getId() {
        return Tag.COMPOUND;
    }
}

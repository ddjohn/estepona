package se.avelon.gibraltar.helpers;

import com.android.ddmlib.RawImage;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class RawImageConverter {

    public static BufferedImage toBufferedImage(RawImage raw) {
        BufferedImage img =
                new BufferedImage(raw.width, raw.height,
                                  BufferedImage.TYPE_INT_ARGB);

        ByteBuffer buf = ByteBuffer.wrap(raw.data)
                                   .order(ByteOrder.LITTLE_ENDIAN);

        for (int y = 0; y < raw.height; y++) {
            for (int x = 0; x < raw.width; x++) {

                int value;
                if (raw.bpp == 16) {
                    value = buf.getShort() & 0xFFFF;
                } else { // usually 32
                    value = buf.getInt();
                }

                int r = extract(value, raw.red_offset,   raw.red_length);
                int g = extract(value, raw.green_offset, raw.green_length);
                int b = extract(value, raw.blue_offset,  raw.blue_length);
                int a = raw.alpha_length > 0
                        ? extract(value, raw.alpha_offset, raw.alpha_length)
                        : 255;

                int argb = (a << 24) | (r << 16) | (g << 8) | b;
                img.setRGB(x, y, argb);
            }
        }
        return img;
    }

    private static int extract(int value, int offset, int length) {
        if (length == 0) return 0;
        int mask = (1 << length) - 1;
        int v = (value >> offset) & mask;
        return (v * 255) / mask; // normalize to 8-bit
    }
}

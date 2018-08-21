package br.com.estudos.chat.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class RawMessage {
    private static final int HEADER_SIZE = 4;

    private byte[] headerBytes;

    private int size;

    private String text;

    private byte[] bytes;

    public RawMessage(byte[] bytes) {
        this.bytes = bytes;
        try {
            this.headerBytes = Arrays.copyOfRange(bytes, 0, HEADER_SIZE);
            this.size = (int) headerBytesToSize(this.headerBytes);
            this.text = new String(Arrays.copyOfRange(bytes, HEADER_SIZE, HEADER_SIZE + this.size), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static long headerBytesToSize(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    public static byte[] headerSizeInBytes(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(value);
        buffer.flip();
        return buffer.array();
    }

    public static RawMessage apply(String text) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            byte[] sbytes = text.getBytes("UTF-8");
            byte[] bytesSize = headerSizeInBytes(sbytes.length);
            outputStream.write(bytesSize);
            outputStream.write(sbytes);
            byte[] byteArray = outputStream.toByteArray();
            RawMessage rawMessage = new RawMessage(byteArray);
            return rawMessage;
        } catch (IOException e) {
        }

        return null;
    }

    public byte[] getHeaderBytes() {
        return headerBytes;
    }

    public int getSize() {
        return size;
    }

    public String getText() {
        return text;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return getText();
    }
}

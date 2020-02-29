package ru.otus.hw15.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Component
public class Serializer {
  private static final Logger logger = LoggerFactory.getLogger(Serializer.class);

  public byte[] serialize(Object data) {
    try (
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(baos)
    ) {
      os.writeObject(data);
      os.flush();

      return baos.toByteArray();
    } catch (Exception e) {
      logger.error("Serialization error, data:" + data, e);

      throw new RuntimeException("Serialization error:" + e.getMessage());
    }
  }

  public <T> T deserialize(byte[] data, Class<T> classOfT) {
    try (
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(bis)
    ) {
      return classOfT.cast(is.readObject());
    } catch (Exception e) {
      logger.error("DeSerialization error, classOfT:" + classOfT, e);

      throw new RuntimeException("DeSerialization error:" + e.getMessage());
    }
  }
}
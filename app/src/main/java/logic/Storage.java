package logic;

/**
 * Created by Marco on 2/22/2018.
 */

public interface Storage<T> {
    T read();

    boolean create(T data);

    boolean isAvailable();
}

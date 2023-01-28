package me.partlysunny.mobs;

public class ToughenerParameter<T> {

    private final String key;
    private T value;

    public ToughenerParameter(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public String key() {
        return key;
    }

    public T value() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}

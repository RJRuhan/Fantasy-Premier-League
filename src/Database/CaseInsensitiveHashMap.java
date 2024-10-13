package Database;

import java.util.HashMap;

public class CaseInsensitiveHashMap<T> extends HashMap<String,T> {

    @Override
    public T put(String key, T value) {
        return super.put(key.toLowerCase(), value);
    }


    public T get(String key) {
        return super.get(key.toLowerCase());
    }
}
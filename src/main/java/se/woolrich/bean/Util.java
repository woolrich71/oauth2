package se.woolrich.bean;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class Util {


    enum resolver {
        id((b, o) -> b.id = Util.getLong(o));

        BiConsumer<Bean, Object> setter;

        resolver(BiConsumer<Bean,Object> setter) {
            this.setter = setter;
        }

        void set(Bean b, HashMap<String, Object> map) {
            setter.accept(b, map.get(toString()));
        }
    }

    class Bean {
        Long id;
        public Long getId() {
            return id;
        }
    }


    public static void main(String[] args) {
        Util util = new Util();
        util.resolve();
    }

    void resolve() {
        HashMap<String, Object >  map = new HashMap<>();
        map.put("id", 3l);

        Bean b = new Bean();
        resolver.id.set(b, map);

        assert(b.getId() == map.get("id"));

    }

    static Long getLong(Object o) {
        return (Long)o;
    }
}

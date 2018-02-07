package se.woolrich.bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class Util {


    enum resolver {
        id((b, o) -> b.id = Util.getLong(o))
        ,type((b, o) -> b.type = Util.getType(o))
        ,name((b, o) -> b.name = Util.getString(o))
        ;

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
        Class type;
        String name;
        public Long getId() {
            return id;
        }

        public Class getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }


    public static void main(String[] args) {
        Util util = new Util();
        util.resolve();
    }

    void resolve() {
        HashMap<String, Object >  map = new HashMap<>();
        map.put("id", 3L);
        map.put("type", Integer.class);
        map.put("name", "int");


        Bean b = new Bean();
        Arrays.stream(resolver.values())
                .forEach(x -> x.set(b, map));

        assert(b.getId() == map.get("id"));
        assert(b.getType() == map.get("type"));
        assert(b.getName().equals(map.get("name")));

    }

    static Long getLong(Object o) {
        return (Long)o;
    }

    static String getString(Object o) {
        return (String)o;
    }

    static Class getType(Object o) {
        return (Class)o;
    }

}

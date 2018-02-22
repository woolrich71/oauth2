package se.woolrich.bean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Util {


    enum SQLMapper {
        id((ps, o, idx) ->  Util.setLong(ps, o.id, idx), (rs, o, name) -> o.id = Util.getLong(rs, name)),
        type((ps, o, idx) ->  Util.setType(ps, o.type, idx), (rs, o, name) -> o.type = Util.getType(rs, name)),
        name((ps, o, idx) -> Util.setString(ps, o.name, idx), (rs, o, name) -> o.name = Util.getString(rs, name))
        ;

        TriConsumer<PreparedStatement, Bean, Integer> prepare;
        TriConsumer<ResultSet, Bean, String> resolver;

        SQLMapper(ThrowingConsumer<PreparedStatement, Bean, Integer, Exception> prepare
                , ThrowingConsumer<ResultSet, Bean, String, Exception> resolver) {
            this.prepare = throwing(prepare);
            this.resolver = throwing(resolver);
        }

        void prepare(PreparedStatement ps, Bean b)   {
            prepare.accept(ps, b,  ordinal());
        }

        void resolve(ResultSet rs, Bean b) {
            resolver.accept(rs, b, toString() );
        }
    }



    class Bean {
        Long id;
        Class type;
        String name;
    }


    public static void main(String[] args) {
        Util util = new Util();
        util.prepare();
        util.resolve();
    }

    void prepare() {
        Bean b = new Bean();
        Arrays.stream(SQLMapper.values())
                .forEach(x -> x.prepare(null, b));
    }

    void resolve() {
        ResultSet rs = null;
        Bean b = new Bean();
        Arrays.stream(SQLMapper.values())
                .forEach(x -> x.resolve(null, b));

    }

    static void setLong(PreparedStatement ps , Long lng, Integer idx) throws SQLException {}
    static Long getLong(ResultSet o, String name) throws SQLException { return 1L; }

    static String getString(ResultSet rs, String name) {
        return name;
    }
    static void setString(PreparedStatement ps , String o, Integer idx) throws SQLException {}

    static Class getType(ResultSet rs, String name) { return name.getClass(); }
    static void setType(PreparedStatement ps , Class o, Integer idx) throws SQLException {}




    static <T, U, V> TriConsumer<T, U, V> throwing(ThrowingConsumer<T, U, V, Exception> throwing) {
        return (a, b, c) -> {
            try {
                throwing.accept(a, b, c);
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @FunctionalInterface
    interface ThrowingConsumer<T, U, V, E extends Exception> {
        void accept(T t, U u, V v) throws E;
    }

    @FunctionalInterface
    interface TriConsumer<T, U, V> {
        public void accept(T t, U u, V v);
    }
}

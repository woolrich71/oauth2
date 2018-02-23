package se.woolrich.bean;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Util {


    private static final SQLMapper[] _VALUES = SQLMapper.values();

    public Bean bean() {
        return new Bean();
    }

    enum SQLMapper {
        id((ps, o, idx) ->  Util.setLong(ps, o.id, idx), (rs, o, name) -> o.id = Util.getLong(rs, name)),
        type((ps, o, idx) ->  Util.setType(ps, o.type, idx), (rs, o, name) -> o.type = Util.getType(rs, name)),
        name((ps, o, idx) -> Util.setString(ps, o.name, idx), (rs, o, name) -> o.name = Util.getString(rs, name))
        ;

        TriConsumer<PreparedStatement, Bean, Integer> prepare;
        TriConsumer<ResultSet, Bean, String> resolve;

        SQLMapper(ThrowingConsumer<PreparedStatement, Bean, Integer, Exception> prepare
                , ThrowingConsumer<ResultSet, Bean, String, Exception> resolver) {
            this.prepare = throwing(prepare);
            this.resolve = throwing(resolver);
        }

        void prepare(PreparedStatement ps, Bean b)   {
            prepare.accept(ps, b,  ordinal());
        }

        void resolve(ResultSet rs, Bean b) {
            resolve.accept(rs, b, toString() );
        }
    }

    public void prepare(Bean b, PreparedStatement ps) {
        Arrays.stream(_VALUES)
                .forEach(x -> x.prepare(ps, b));
    }

    public void resolve(Bean b, ResultSet rs) {
        Arrays.stream(_VALUES)
                .forEach(x -> x.resolve(rs, b));
    }

    public void resolveImperative(Bean b, ResultSet rs) {
        try {
            b.id =  Util.getLong(rs, "id");
            b.type =  Util.getType(rs, "type");
            b.name =  Util.getString(rs, "Name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void prepareImperative(Bean b, PreparedStatement ps) {
        try {
            Util.setLong(ps, b.id, 1);
            Util.setType(ps, b.type, 2);
            Util.setString(ps, b.name, 3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public class Bean {
        Long id;
        Class type;
        String name;
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
        void accept(T t, U u, V v);
    }
}

package dev.chudziudgi.ohmc.drop.paper.database;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class InstantPersister extends BaseDataType {

    private static final InstantPersister INSTANCE = new InstantPersister();

    private InstantPersister() {
        super(SqlType.DATE, new Class<?>[] { Instant.class });
    }

    public static InstantPersister getSingleton() {
        return INSTANCE;
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return Instant.parse(defaultStr);
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getTimestamp(columnPos);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return switch (sqlArg) {
            case null -> null;
            case Timestamp timestamp -> timestamp.toInstant();
            case Long l -> Instant.ofEpochMilli(l);
            default -> throw new IllegalArgumentException("Cannot convert " + sqlArg.getClass() + " to Instant");
        };
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        if (javaObject == null) {
            return null;
        }
        if (javaObject instanceof Instant) {
            return Timestamp.from((Instant) javaObject);
        }
        throw new IllegalArgumentException("Cannot convert " + javaObject.getClass() + " to SQL");
    }

    @Override
    public boolean isAppropriateId() {
        return false;
    }
}

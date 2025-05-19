package com.jonathan.coordinapp.data.local.db;

public final class BackupContract {
    private BackupContract() {
    }

    public static final String TABLE_NAME = "backup_local";

    public static final class Columns {
        public static final String ID = "_id";
        public static final String ETIQUETA = "etiqueta1d";
        public static final String LAT = "latitud";
        public static final String LNG = "longitud";
        public static final String OBS = "observacion";
        public static final String TIMESTAMP = "timestamp";
    }

    static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Columns.ETIQUETA + " TEXT NOT NULL, " +
                    Columns.LAT + " REAL NOT NULL, " +
                    Columns.LNG + " REAL NOT NULL, " +
                    Columns.OBS + " TEXT, " +
                    Columns.TIMESTAMP + " INTEGER NOT NULL" +
                    ");";

    static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}

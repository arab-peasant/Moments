package com.soundgallery.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SoundGalleryDatabase_Impl extends SoundGalleryDatabase {
  private volatile PhotoDao _photoDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(5) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `photo_entries` (`uri` TEXT NOT NULL, `mediaStoreId` INTEGER NOT NULL, `displayName` TEXT NOT NULL, `dateTaken` INTEGER NOT NULL, `linkedSongId` INTEGER, `linkedSongUri` TEXT, `linkedSongTitle` TEXT, `linkedSongArtist` TEXT, `linkedSongAlbum` TEXT, `linkedSongDuration` INTEGER, `caption` TEXT, `isLiked` INTEGER NOT NULL, `isManuallyAdded` INTEGER NOT NULL, `customDate` INTEGER, `lastPlaybackPosition` INTEGER NOT NULL, PRIMARY KEY(`uri`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'cd06d3d9f911a05e3236d9d09c80ff5b')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `photo_entries`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPhotoEntries = new HashMap<String, TableInfo.Column>(15);
        _columnsPhotoEntries.put("uri", new TableInfo.Column("uri", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoEntries.put("mediaStoreId", new TableInfo.Column("mediaStoreId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoEntries.put("displayName", new TableInfo.Column("displayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoEntries.put("dateTaken", new TableInfo.Column("dateTaken", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoEntries.put("linkedSongId", new TableInfo.Column("linkedSongId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoEntries.put("linkedSongUri", new TableInfo.Column("linkedSongUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoEntries.put("linkedSongTitle", new TableInfo.Column("linkedSongTitle", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoEntries.put("linkedSongArtist", new TableInfo.Column("linkedSongArtist", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoEntries.put("linkedSongAlbum", new TableInfo.Column("linkedSongAlbum", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoEntries.put("linkedSongDuration", new TableInfo.Column("linkedSongDuration", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoEntries.put("caption", new TableInfo.Column("caption", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoEntries.put("isLiked", new TableInfo.Column("isLiked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoEntries.put("isManuallyAdded", new TableInfo.Column("isManuallyAdded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoEntries.put("customDate", new TableInfo.Column("customDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPhotoEntries.put("lastPlaybackPosition", new TableInfo.Column("lastPlaybackPosition", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPhotoEntries = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPhotoEntries = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPhotoEntries = new TableInfo("photo_entries", _columnsPhotoEntries, _foreignKeysPhotoEntries, _indicesPhotoEntries);
        final TableInfo _existingPhotoEntries = TableInfo.read(db, "photo_entries");
        if (!_infoPhotoEntries.equals(_existingPhotoEntries)) {
          return new RoomOpenHelper.ValidationResult(false, "photo_entries(com.soundgallery.app.data.model.PhotoEntry).\n"
                  + " Expected:\n" + _infoPhotoEntries + "\n"
                  + " Found:\n" + _existingPhotoEntries);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "cd06d3d9f911a05e3236d9d09c80ff5b", "d180c879faff618c530fe1b0cf1263a8");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "photo_entries");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `photo_entries`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(PhotoDao.class, PhotoDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public PhotoDao photoDao() {
    if (_photoDao != null) {
      return _photoDao;
    } else {
      synchronized(this) {
        if(_photoDao == null) {
          _photoDao = new PhotoDao_Impl(this);
        }
        return _photoDao;
      }
    }
  }
}

package com.soundgallery.app.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.soundgallery.app.data.model.PhotoEntry;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PhotoDao_Impl implements PhotoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PhotoEntry> __insertionAdapterOfPhotoEntry;

  private final EntityInsertionAdapter<PhotoEntry> __insertionAdapterOfPhotoEntry_1;

  private final EntityDeletionOrUpdateAdapter<PhotoEntry> __deletionAdapterOfPhotoEntry;

  private final EntityDeletionOrUpdateAdapter<PhotoEntry> __updateAdapterOfPhotoEntry;

  private final SharedSQLiteStatement __preparedStmtOfLinkSongToPhoto;

  private final SharedSQLiteStatement __preparedStmtOfUnlinkSongFromPhoto;

  private final SharedSQLiteStatement __preparedStmtOfSetLiked;

  private final SharedSQLiteStatement __preparedStmtOfSetCaption;

  private final SharedSQLiteStatement __preparedStmtOfSetCustomDate;

  private final SharedSQLiteStatement __preparedStmtOfSetLastPlaybackPosition;

  private final SharedSQLiteStatement __preparedStmtOfRemovePhotoFromGallery;

  public PhotoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPhotoEntry = new EntityInsertionAdapter<PhotoEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `photo_entries` (`uri`,`mediaStoreId`,`displayName`,`dateTaken`,`linkedSongId`,`linkedSongUri`,`linkedSongTitle`,`linkedSongArtist`,`linkedSongAlbum`,`linkedSongDuration`,`caption`,`isLiked`,`isManuallyAdded`,`customDate`,`lastPlaybackPosition`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PhotoEntry entity) {
        statement.bindString(1, entity.getUri());
        statement.bindLong(2, entity.getMediaStoreId());
        statement.bindString(3, entity.getDisplayName());
        statement.bindLong(4, entity.getDateTaken());
        if (entity.getLinkedSongId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getLinkedSongId());
        }
        if (entity.getLinkedSongUri() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLinkedSongUri());
        }
        if (entity.getLinkedSongTitle() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getLinkedSongTitle());
        }
        if (entity.getLinkedSongArtist() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getLinkedSongArtist());
        }
        if (entity.getLinkedSongAlbum() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getLinkedSongAlbum());
        }
        if (entity.getLinkedSongDuration() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getLinkedSongDuration());
        }
        if (entity.getCaption() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getCaption());
        }
        final int _tmp = entity.isLiked() ? 1 : 0;
        statement.bindLong(12, _tmp);
        final int _tmp_1 = entity.isManuallyAdded() ? 1 : 0;
        statement.bindLong(13, _tmp_1);
        if (entity.getCustomDate() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getCustomDate());
        }
        statement.bindLong(15, entity.getLastPlaybackPosition());
      }
    };
    this.__insertionAdapterOfPhotoEntry_1 = new EntityInsertionAdapter<PhotoEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `photo_entries` (`uri`,`mediaStoreId`,`displayName`,`dateTaken`,`linkedSongId`,`linkedSongUri`,`linkedSongTitle`,`linkedSongArtist`,`linkedSongAlbum`,`linkedSongDuration`,`caption`,`isLiked`,`isManuallyAdded`,`customDate`,`lastPlaybackPosition`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PhotoEntry entity) {
        statement.bindString(1, entity.getUri());
        statement.bindLong(2, entity.getMediaStoreId());
        statement.bindString(3, entity.getDisplayName());
        statement.bindLong(4, entity.getDateTaken());
        if (entity.getLinkedSongId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getLinkedSongId());
        }
        if (entity.getLinkedSongUri() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLinkedSongUri());
        }
        if (entity.getLinkedSongTitle() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getLinkedSongTitle());
        }
        if (entity.getLinkedSongArtist() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getLinkedSongArtist());
        }
        if (entity.getLinkedSongAlbum() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getLinkedSongAlbum());
        }
        if (entity.getLinkedSongDuration() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getLinkedSongDuration());
        }
        if (entity.getCaption() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getCaption());
        }
        final int _tmp = entity.isLiked() ? 1 : 0;
        statement.bindLong(12, _tmp);
        final int _tmp_1 = entity.isManuallyAdded() ? 1 : 0;
        statement.bindLong(13, _tmp_1);
        if (entity.getCustomDate() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getCustomDate());
        }
        statement.bindLong(15, entity.getLastPlaybackPosition());
      }
    };
    this.__deletionAdapterOfPhotoEntry = new EntityDeletionOrUpdateAdapter<PhotoEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `photo_entries` WHERE `uri` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PhotoEntry entity) {
        statement.bindString(1, entity.getUri());
      }
    };
    this.__updateAdapterOfPhotoEntry = new EntityDeletionOrUpdateAdapter<PhotoEntry>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `photo_entries` SET `uri` = ?,`mediaStoreId` = ?,`displayName` = ?,`dateTaken` = ?,`linkedSongId` = ?,`linkedSongUri` = ?,`linkedSongTitle` = ?,`linkedSongArtist` = ?,`linkedSongAlbum` = ?,`linkedSongDuration` = ?,`caption` = ?,`isLiked` = ?,`isManuallyAdded` = ?,`customDate` = ?,`lastPlaybackPosition` = ? WHERE `uri` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PhotoEntry entity) {
        statement.bindString(1, entity.getUri());
        statement.bindLong(2, entity.getMediaStoreId());
        statement.bindString(3, entity.getDisplayName());
        statement.bindLong(4, entity.getDateTaken());
        if (entity.getLinkedSongId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getLinkedSongId());
        }
        if (entity.getLinkedSongUri() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLinkedSongUri());
        }
        if (entity.getLinkedSongTitle() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getLinkedSongTitle());
        }
        if (entity.getLinkedSongArtist() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getLinkedSongArtist());
        }
        if (entity.getLinkedSongAlbum() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getLinkedSongAlbum());
        }
        if (entity.getLinkedSongDuration() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getLinkedSongDuration());
        }
        if (entity.getCaption() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getCaption());
        }
        final int _tmp = entity.isLiked() ? 1 : 0;
        statement.bindLong(12, _tmp);
        final int _tmp_1 = entity.isManuallyAdded() ? 1 : 0;
        statement.bindLong(13, _tmp_1);
        if (entity.getCustomDate() == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, entity.getCustomDate());
        }
        statement.bindLong(15, entity.getLastPlaybackPosition());
        statement.bindString(16, entity.getUri());
      }
    };
    this.__preparedStmtOfLinkSongToPhoto = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "\n"
                + "        UPDATE photo_entries SET\n"
                + "            linkedSongId = ?,\n"
                + "            linkedSongUri = ?,\n"
                + "            linkedSongTitle = ?,\n"
                + "            linkedSongArtist = ?,\n"
                + "            linkedSongAlbum = ?,\n"
                + "            linkedSongDuration = ?\n"
                + "        WHERE uri = ?\n"
                + "    ";
        return _query;
      }
    };
    this.__preparedStmtOfUnlinkSongFromPhoto = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE photo_entries SET linkedSongId = NULL, linkedSongUri = NULL, linkedSongTitle = NULL, linkedSongArtist = NULL, linkedSongAlbum = NULL, linkedSongDuration = NULL WHERE uri = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetLiked = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE photo_entries SET isLiked = ? WHERE uri = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetCaption = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE photo_entries SET caption = ? WHERE uri = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetCustomDate = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE photo_entries SET customDate = ? WHERE uri = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetLastPlaybackPosition = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE photo_entries SET lastPlaybackPosition = ? WHERE uri = ?";
        return _query;
      }
    };
    this.__preparedStmtOfRemovePhotoFromGallery = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE photo_entries SET isManuallyAdded = 0 WHERE uri = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertPhoto(final PhotoEntry photo, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPhotoEntry.insert(photo);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertPhotos(final List<PhotoEntry> photos,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPhotoEntry_1.insert(photos);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deletePhoto(final PhotoEntry photo, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPhotoEntry.handle(photo);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePhoto(final PhotoEntry photo, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfPhotoEntry.handle(photo);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object linkSongToPhoto(final String photoUri, final long songId, final String songUri,
      final String title, final String artist, final String album, final long duration,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfLinkSongToPhoto.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, songId);
        _argIndex = 2;
        _stmt.bindString(_argIndex, songUri);
        _argIndex = 3;
        _stmt.bindString(_argIndex, title);
        _argIndex = 4;
        _stmt.bindString(_argIndex, artist);
        _argIndex = 5;
        _stmt.bindString(_argIndex, album);
        _argIndex = 6;
        _stmt.bindLong(_argIndex, duration);
        _argIndex = 7;
        _stmt.bindString(_argIndex, photoUri);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfLinkSongToPhoto.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object unlinkSongFromPhoto(final String photoUri,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUnlinkSongFromPhoto.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, photoUri);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUnlinkSongFromPhoto.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setLiked(final String photoUri, final boolean liked,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetLiked.acquire();
        int _argIndex = 1;
        final int _tmp = liked ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, photoUri);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetLiked.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setCaption(final String photoUri, final String caption,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetCaption.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, caption);
        _argIndex = 2;
        _stmt.bindString(_argIndex, photoUri);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetCaption.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setCustomDate(final String photoUri, final long date,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetCustomDate.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, date);
        _argIndex = 2;
        _stmt.bindString(_argIndex, photoUri);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetCustomDate.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setLastPlaybackPosition(final String photoUri, final long position,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetLastPlaybackPosition.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, position);
        _argIndex = 2;
        _stmt.bindString(_argIndex, photoUri);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetLastPlaybackPosition.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object removePhotoFromGallery(final String photoUri,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfRemovePhotoFromGallery.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, photoUri);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfRemovePhotoFromGallery.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PhotoEntry>> getAllPhotos() {
    final String _sql = "SELECT * FROM photo_entries WHERE isManuallyAdded = 1 ORDER BY COALESCE(customDate, dateTaken) DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"photo_entries"}, new Callable<List<PhotoEntry>>() {
      @Override
      @NonNull
      public List<PhotoEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfMediaStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaStoreId");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfDateTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTaken");
          final int _cursorIndexOfLinkedSongId = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongId");
          final int _cursorIndexOfLinkedSongUri = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongUri");
          final int _cursorIndexOfLinkedSongTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongTitle");
          final int _cursorIndexOfLinkedSongArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongArtist");
          final int _cursorIndexOfLinkedSongAlbum = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongAlbum");
          final int _cursorIndexOfLinkedSongDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongDuration");
          final int _cursorIndexOfCaption = CursorUtil.getColumnIndexOrThrow(_cursor, "caption");
          final int _cursorIndexOfIsLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "isLiked");
          final int _cursorIndexOfIsManuallyAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "isManuallyAdded");
          final int _cursorIndexOfCustomDate = CursorUtil.getColumnIndexOrThrow(_cursor, "customDate");
          final int _cursorIndexOfLastPlaybackPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlaybackPosition");
          final List<PhotoEntry> _result = new ArrayList<PhotoEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PhotoEntry _item;
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final long _tmpMediaStoreId;
            _tmpMediaStoreId = _cursor.getLong(_cursorIndexOfMediaStoreId);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final long _tmpDateTaken;
            _tmpDateTaken = _cursor.getLong(_cursorIndexOfDateTaken);
            final Long _tmpLinkedSongId;
            if (_cursor.isNull(_cursorIndexOfLinkedSongId)) {
              _tmpLinkedSongId = null;
            } else {
              _tmpLinkedSongId = _cursor.getLong(_cursorIndexOfLinkedSongId);
            }
            final String _tmpLinkedSongUri;
            if (_cursor.isNull(_cursorIndexOfLinkedSongUri)) {
              _tmpLinkedSongUri = null;
            } else {
              _tmpLinkedSongUri = _cursor.getString(_cursorIndexOfLinkedSongUri);
            }
            final String _tmpLinkedSongTitle;
            if (_cursor.isNull(_cursorIndexOfLinkedSongTitle)) {
              _tmpLinkedSongTitle = null;
            } else {
              _tmpLinkedSongTitle = _cursor.getString(_cursorIndexOfLinkedSongTitle);
            }
            final String _tmpLinkedSongArtist;
            if (_cursor.isNull(_cursorIndexOfLinkedSongArtist)) {
              _tmpLinkedSongArtist = null;
            } else {
              _tmpLinkedSongArtist = _cursor.getString(_cursorIndexOfLinkedSongArtist);
            }
            final String _tmpLinkedSongAlbum;
            if (_cursor.isNull(_cursorIndexOfLinkedSongAlbum)) {
              _tmpLinkedSongAlbum = null;
            } else {
              _tmpLinkedSongAlbum = _cursor.getString(_cursorIndexOfLinkedSongAlbum);
            }
            final Long _tmpLinkedSongDuration;
            if (_cursor.isNull(_cursorIndexOfLinkedSongDuration)) {
              _tmpLinkedSongDuration = null;
            } else {
              _tmpLinkedSongDuration = _cursor.getLong(_cursorIndexOfLinkedSongDuration);
            }
            final String _tmpCaption;
            if (_cursor.isNull(_cursorIndexOfCaption)) {
              _tmpCaption = null;
            } else {
              _tmpCaption = _cursor.getString(_cursorIndexOfCaption);
            }
            final boolean _tmpIsLiked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsLiked);
            _tmpIsLiked = _tmp != 0;
            final boolean _tmpIsManuallyAdded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsManuallyAdded);
            _tmpIsManuallyAdded = _tmp_1 != 0;
            final Long _tmpCustomDate;
            if (_cursor.isNull(_cursorIndexOfCustomDate)) {
              _tmpCustomDate = null;
            } else {
              _tmpCustomDate = _cursor.getLong(_cursorIndexOfCustomDate);
            }
            final long _tmpLastPlaybackPosition;
            _tmpLastPlaybackPosition = _cursor.getLong(_cursorIndexOfLastPlaybackPosition);
            _item = new PhotoEntry(_tmpUri,_tmpMediaStoreId,_tmpDisplayName,_tmpDateTaken,_tmpLinkedSongId,_tmpLinkedSongUri,_tmpLinkedSongTitle,_tmpLinkedSongArtist,_tmpLinkedSongAlbum,_tmpLinkedSongDuration,_tmpCaption,_tmpIsLiked,_tmpIsManuallyAdded,_tmpCustomDate,_tmpLastPlaybackPosition);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getPhotoByUri(final String uri,
      final Continuation<? super PhotoEntry> $completion) {
    final String _sql = "SELECT * FROM photo_entries WHERE uri = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, uri);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PhotoEntry>() {
      @Override
      @Nullable
      public PhotoEntry call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfMediaStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaStoreId");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfDateTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTaken");
          final int _cursorIndexOfLinkedSongId = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongId");
          final int _cursorIndexOfLinkedSongUri = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongUri");
          final int _cursorIndexOfLinkedSongTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongTitle");
          final int _cursorIndexOfLinkedSongArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongArtist");
          final int _cursorIndexOfLinkedSongAlbum = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongAlbum");
          final int _cursorIndexOfLinkedSongDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongDuration");
          final int _cursorIndexOfCaption = CursorUtil.getColumnIndexOrThrow(_cursor, "caption");
          final int _cursorIndexOfIsLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "isLiked");
          final int _cursorIndexOfIsManuallyAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "isManuallyAdded");
          final int _cursorIndexOfCustomDate = CursorUtil.getColumnIndexOrThrow(_cursor, "customDate");
          final int _cursorIndexOfLastPlaybackPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlaybackPosition");
          final PhotoEntry _result;
          if (_cursor.moveToFirst()) {
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final long _tmpMediaStoreId;
            _tmpMediaStoreId = _cursor.getLong(_cursorIndexOfMediaStoreId);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final long _tmpDateTaken;
            _tmpDateTaken = _cursor.getLong(_cursorIndexOfDateTaken);
            final Long _tmpLinkedSongId;
            if (_cursor.isNull(_cursorIndexOfLinkedSongId)) {
              _tmpLinkedSongId = null;
            } else {
              _tmpLinkedSongId = _cursor.getLong(_cursorIndexOfLinkedSongId);
            }
            final String _tmpLinkedSongUri;
            if (_cursor.isNull(_cursorIndexOfLinkedSongUri)) {
              _tmpLinkedSongUri = null;
            } else {
              _tmpLinkedSongUri = _cursor.getString(_cursorIndexOfLinkedSongUri);
            }
            final String _tmpLinkedSongTitle;
            if (_cursor.isNull(_cursorIndexOfLinkedSongTitle)) {
              _tmpLinkedSongTitle = null;
            } else {
              _tmpLinkedSongTitle = _cursor.getString(_cursorIndexOfLinkedSongTitle);
            }
            final String _tmpLinkedSongArtist;
            if (_cursor.isNull(_cursorIndexOfLinkedSongArtist)) {
              _tmpLinkedSongArtist = null;
            } else {
              _tmpLinkedSongArtist = _cursor.getString(_cursorIndexOfLinkedSongArtist);
            }
            final String _tmpLinkedSongAlbum;
            if (_cursor.isNull(_cursorIndexOfLinkedSongAlbum)) {
              _tmpLinkedSongAlbum = null;
            } else {
              _tmpLinkedSongAlbum = _cursor.getString(_cursorIndexOfLinkedSongAlbum);
            }
            final Long _tmpLinkedSongDuration;
            if (_cursor.isNull(_cursorIndexOfLinkedSongDuration)) {
              _tmpLinkedSongDuration = null;
            } else {
              _tmpLinkedSongDuration = _cursor.getLong(_cursorIndexOfLinkedSongDuration);
            }
            final String _tmpCaption;
            if (_cursor.isNull(_cursorIndexOfCaption)) {
              _tmpCaption = null;
            } else {
              _tmpCaption = _cursor.getString(_cursorIndexOfCaption);
            }
            final boolean _tmpIsLiked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsLiked);
            _tmpIsLiked = _tmp != 0;
            final boolean _tmpIsManuallyAdded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsManuallyAdded);
            _tmpIsManuallyAdded = _tmp_1 != 0;
            final Long _tmpCustomDate;
            if (_cursor.isNull(_cursorIndexOfCustomDate)) {
              _tmpCustomDate = null;
            } else {
              _tmpCustomDate = _cursor.getLong(_cursorIndexOfCustomDate);
            }
            final long _tmpLastPlaybackPosition;
            _tmpLastPlaybackPosition = _cursor.getLong(_cursorIndexOfLastPlaybackPosition);
            _result = new PhotoEntry(_tmpUri,_tmpMediaStoreId,_tmpDisplayName,_tmpDateTaken,_tmpLinkedSongId,_tmpLinkedSongUri,_tmpLinkedSongTitle,_tmpLinkedSongArtist,_tmpLinkedSongAlbum,_tmpLinkedSongDuration,_tmpCaption,_tmpIsLiked,_tmpIsManuallyAdded,_tmpCustomDate,_tmpLastPlaybackPosition);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PhotoEntry>> getLikedPhotos() {
    final String _sql = "SELECT * FROM photo_entries WHERE isLiked = 1 AND isManuallyAdded = 1 ORDER BY COALESCE(customDate, dateTaken) DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"photo_entries"}, new Callable<List<PhotoEntry>>() {
      @Override
      @NonNull
      public List<PhotoEntry> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfMediaStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaStoreId");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfDateTaken = CursorUtil.getColumnIndexOrThrow(_cursor, "dateTaken");
          final int _cursorIndexOfLinkedSongId = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongId");
          final int _cursorIndexOfLinkedSongUri = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongUri");
          final int _cursorIndexOfLinkedSongTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongTitle");
          final int _cursorIndexOfLinkedSongArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongArtist");
          final int _cursorIndexOfLinkedSongAlbum = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongAlbum");
          final int _cursorIndexOfLinkedSongDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "linkedSongDuration");
          final int _cursorIndexOfCaption = CursorUtil.getColumnIndexOrThrow(_cursor, "caption");
          final int _cursorIndexOfIsLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "isLiked");
          final int _cursorIndexOfIsManuallyAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "isManuallyAdded");
          final int _cursorIndexOfCustomDate = CursorUtil.getColumnIndexOrThrow(_cursor, "customDate");
          final int _cursorIndexOfLastPlaybackPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlaybackPosition");
          final List<PhotoEntry> _result = new ArrayList<PhotoEntry>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PhotoEntry _item;
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final long _tmpMediaStoreId;
            _tmpMediaStoreId = _cursor.getLong(_cursorIndexOfMediaStoreId);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final long _tmpDateTaken;
            _tmpDateTaken = _cursor.getLong(_cursorIndexOfDateTaken);
            final Long _tmpLinkedSongId;
            if (_cursor.isNull(_cursorIndexOfLinkedSongId)) {
              _tmpLinkedSongId = null;
            } else {
              _tmpLinkedSongId = _cursor.getLong(_cursorIndexOfLinkedSongId);
            }
            final String _tmpLinkedSongUri;
            if (_cursor.isNull(_cursorIndexOfLinkedSongUri)) {
              _tmpLinkedSongUri = null;
            } else {
              _tmpLinkedSongUri = _cursor.getString(_cursorIndexOfLinkedSongUri);
            }
            final String _tmpLinkedSongTitle;
            if (_cursor.isNull(_cursorIndexOfLinkedSongTitle)) {
              _tmpLinkedSongTitle = null;
            } else {
              _tmpLinkedSongTitle = _cursor.getString(_cursorIndexOfLinkedSongTitle);
            }
            final String _tmpLinkedSongArtist;
            if (_cursor.isNull(_cursorIndexOfLinkedSongArtist)) {
              _tmpLinkedSongArtist = null;
            } else {
              _tmpLinkedSongArtist = _cursor.getString(_cursorIndexOfLinkedSongArtist);
            }
            final String _tmpLinkedSongAlbum;
            if (_cursor.isNull(_cursorIndexOfLinkedSongAlbum)) {
              _tmpLinkedSongAlbum = null;
            } else {
              _tmpLinkedSongAlbum = _cursor.getString(_cursorIndexOfLinkedSongAlbum);
            }
            final Long _tmpLinkedSongDuration;
            if (_cursor.isNull(_cursorIndexOfLinkedSongDuration)) {
              _tmpLinkedSongDuration = null;
            } else {
              _tmpLinkedSongDuration = _cursor.getLong(_cursorIndexOfLinkedSongDuration);
            }
            final String _tmpCaption;
            if (_cursor.isNull(_cursorIndexOfCaption)) {
              _tmpCaption = null;
            } else {
              _tmpCaption = _cursor.getString(_cursorIndexOfCaption);
            }
            final boolean _tmpIsLiked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsLiked);
            _tmpIsLiked = _tmp != 0;
            final boolean _tmpIsManuallyAdded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsManuallyAdded);
            _tmpIsManuallyAdded = _tmp_1 != 0;
            final Long _tmpCustomDate;
            if (_cursor.isNull(_cursorIndexOfCustomDate)) {
              _tmpCustomDate = null;
            } else {
              _tmpCustomDate = _cursor.getLong(_cursorIndexOfCustomDate);
            }
            final long _tmpLastPlaybackPosition;
            _tmpLastPlaybackPosition = _cursor.getLong(_cursorIndexOfLastPlaybackPosition);
            _item = new PhotoEntry(_tmpUri,_tmpMediaStoreId,_tmpDisplayName,_tmpDateTaken,_tmpLinkedSongId,_tmpLinkedSongUri,_tmpLinkedSongTitle,_tmpLinkedSongArtist,_tmpLinkedSongAlbum,_tmpLinkedSongDuration,_tmpCaption,_tmpIsLiked,_tmpIsManuallyAdded,_tmpCustomDate,_tmpLastPlaybackPosition);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

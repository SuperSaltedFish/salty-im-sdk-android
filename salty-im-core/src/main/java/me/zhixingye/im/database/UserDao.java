package me.zhixingye.im.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.salty.protos.UserProfile;
import me.zhixingye.im.service.impl.SQLiteServiceImpl;

/**
 * 优秀的代码是它自己最好的文档。当你考虑要添加一个注释时，问问自己，“如何能改进这段代码，以让它不需要注释”
 *
 * @author zhixingye , 2020年05月01日.
 */


public class UserDao extends AbstractDao<UserProfile> {

    static final String TABLE_NAME = "User";

    static final String COLUMN_NAME_UserId = "UserId";
    static final String COLUMN_NAME_Telephone = "Telephone";
    static final String COLUMN_NAME_Email = "Email";
    static final String COLUMN_NAME_Nickname = "Nickname";
    static final String COLUMN_NAME_Avatar = "Avatar";
    static final String COLUMN_NAME_Description = "Description";
    static final String COLUMN_NAME_Location = "Location";
    static final String COLUMN_NAME_Birthday = "Birthday";
    static final String COLUMN_NAME_Sex = "Sex";


    public static final String CREATE_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                    + COLUMN_NAME_UserId + " TEXT NOT NULL, "
                    + COLUMN_NAME_Telephone + "  TEXT,"
                    + COLUMN_NAME_Email + " TEXT,"
                    + COLUMN_NAME_Nickname + " TEXT NOT NULL,"
                    + COLUMN_NAME_Avatar + " TEXT,"
                    + COLUMN_NAME_Description + " TEXT,"
                    + COLUMN_NAME_Location + " TEXT,"
                    + COLUMN_NAME_Birthday + " INTEGER,"
                    + COLUMN_NAME_Sex + " INTEGER,"
                    + "PRIMARY KEY (" + COLUMN_NAME_UserId + ")"
                    + ")";

    public UserDao(SQLiteServiceImpl.ReadWriteHelper helper) {
        super(helper);
    }

    @NonNull
    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Nullable
    @Override
    protected String getViewTableView() {
        return null;
    }

    @NonNull
    @Override
    protected String getPrimaryKeySelection() {
        return COLUMN_NAME_UserId + " = ?";
    }

    @Override
    protected String[] getPrimaryKeySelectionArgs(UserProfile entity) {
        return new String[]{entity.getUserId()};
    }

    @Override
    protected void parseToContentValues(UserProfile entity, ContentValues values) {
        values.put(COLUMN_NAME_UserId, entity.getUserId());
        values.put(COLUMN_NAME_Telephone, entity.getTelephone());
        values.put(COLUMN_NAME_Email, entity.getEmail());
        values.put(COLUMN_NAME_Nickname, entity.getNickname());
        values.put(COLUMN_NAME_Avatar, entity.getAvatar());
        values.put(COLUMN_NAME_Description, entity.getDescription());
        values.put(COLUMN_NAME_Location, entity.getLocation());
        values.put(COLUMN_NAME_Birthday, entity.getBirthday());
        values.put(COLUMN_NAME_Sex, entity.getSexValue());
    }

    @Override
    protected UserProfile toEntity(Cursor cursor) {
        return toEntityFromCursor(cursor);
    }

    static UserProfile toEntityFromCursor(Cursor cursor) {
        return UserProfile.newBuilder()
                .setUserId(getString(cursor, COLUMN_NAME_UserId))
                .setTelephone(getString(cursor, COLUMN_NAME_Telephone))
                .setEmail(getString(cursor, COLUMN_NAME_Email))
                .setNickname(getString(cursor, COLUMN_NAME_Nickname))
                .setAvatar(getString(cursor, COLUMN_NAME_Avatar))
                .setDescription(getString(cursor, COLUMN_NAME_Description))
                .setLocation(getString(cursor, COLUMN_NAME_Location))
                .setBirthday(getLong(cursor, COLUMN_NAME_Birthday))
                .setSexValue(getInt(cursor, COLUMN_NAME_Sex))
                .build();
    }

    static boolean replaceUser(SQLiteDatabase Write, UserProfile entity, ContentValues values) {
        if (entity == null) {
            return false;
        }
        values.clear();
        values.put(COLUMN_NAME_UserId, entity.getUserId());
        values.put(COLUMN_NAME_Telephone, entity.getTelephone());
        values.put(COLUMN_NAME_Email, entity.getEmail());
        values.put(COLUMN_NAME_Nickname, entity.getNickname());
        values.put(COLUMN_NAME_Avatar, entity.getAvatar());
        values.put(COLUMN_NAME_Description, entity.getDescription());
        values.put(COLUMN_NAME_Location, entity.getLocation());
        values.put(COLUMN_NAME_Birthday, entity.getBirthday());
        values.put(COLUMN_NAME_Sex, entity.getSexValue());
        return Write.replace(TABLE_NAME, null, values) >= 0;
    }
}

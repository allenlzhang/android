package com.hz17car.yema.dao.db.converter;

import android.database.Cursor;

import com.hz17car.yema.dao.db.sqlite.ColumnDbType;


/**
 * Author: wyouflf
 * Date: 13-11-4
 * Time: 下午10:51
 */
public class ShortColumnConverter implements ColumnConverter<Short> {
    @Override
    public Short getFieldValue(final Cursor cursor, int index) {
        return cursor.isNull(index) ? null : cursor.getShort(index);
    }

    @Override
    public Object fieldValue2DbValue(Short fieldValue) {
        return fieldValue;
    }

    @Override
    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}

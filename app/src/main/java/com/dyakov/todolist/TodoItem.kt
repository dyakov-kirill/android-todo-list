package com.dyakov.todolist

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class TodoItem(
    @PrimaryKey
    val id: String,
    val description: String,
    val priority: Priority,
    @ColumnInfo(name = "is_done")
    val isDone: Boolean,
    val deadline: Date?,
    @ColumnInfo(name = "creation_time")
    val creationTime: Date,
    @ColumnInfo(name = "edit_time")
    val editTime: Date?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        Priority.values()[parcel.readInt()],
        parcel.readByte() != 0.toByte(),
        Date(parcel.readLong()),
        Date(parcel.readLong()),
        Date(parcel.readLong())
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(description)
        parcel.writeInt(priority.priority)
        parcel.writeByte(if (isDone) 1 else 0)
        if (deadline != null) parcel.writeLong(deadline.time)
        parcel.writeLong(creationTime.time)
        if (editTime != null) parcel.writeLong(editTime.time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TodoItem> {
        override fun createFromParcel(parcel: Parcel): TodoItem {
            return TodoItem(parcel)
        }

        override fun newArray(size: Int): Array<TodoItem?> {
            return arrayOfNulls(size)
        }
    }
}

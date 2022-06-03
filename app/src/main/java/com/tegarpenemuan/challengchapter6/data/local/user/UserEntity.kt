package com.tegarpenemuan.challengchapter6.data.local.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "job") val job: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "image") val image: String? = null,
)


package com.movie_app_task.common.data.utils


import com.google.gson.Gson
import java.lang.reflect.Type

fun <T> String.getModelFromJSON(type: Type): T {
    return Gson().fromJson(this, type)
}
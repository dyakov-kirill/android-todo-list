package com.example.todolist.model

class Utils {
    enum class Importance(val value: Int) {
        LOW(0),
        MEDIUM(1),
        HIGH(2);

        companion object {
            fun fromInt(value: Int) = Importance.values().first { it.value == value }
        }
    }

    enum class Flag {
        NOT_DONE, DONE
    }
}
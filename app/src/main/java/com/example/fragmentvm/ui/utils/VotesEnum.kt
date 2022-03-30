package com.example.fragmentvm.ui.utils

enum class VotesEnum(val value: Int = -1) {
    UNDEFINED(-1),
    DOWN(0),
    UP(1);

    companion object {
        private val map = values().associateBy { it.value }

        // Get Type by code with check existing codes and default
        fun getByCode(code: Int, typeDefaultParam: VotesEnum = UNDEFINED): VotesEnum {
            return map[code] ?: typeDefaultParam
        }
    }
}
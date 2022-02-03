package com.example.fragmentvm.utils

enum class VotesEnum(val value: Int) {
    DOWN(0),
    UP(1)
}

enum class LikeState(val value: Int = 0){
    UNDETERMINED(0),
    LIKED(1),
    DISLIKED(2)
}
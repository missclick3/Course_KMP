package ru.missclick.core.domain.util

class DataErrorException(
    val error: DataError
): Exception()
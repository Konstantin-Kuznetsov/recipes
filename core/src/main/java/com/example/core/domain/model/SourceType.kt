package com.example.core.domain.model

sealed class DataSourceType {
    object Remote: DataSourceType()
    object Cache: DataSourceType()
}

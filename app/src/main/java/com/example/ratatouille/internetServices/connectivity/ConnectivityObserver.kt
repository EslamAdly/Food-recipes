package com.example.ratatouille.internetServices.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<Status>
    fun getCurrentNetworkStatus(): Status
    enum class Status {
        Available, Unavailable, Losing, Lost
    }
}
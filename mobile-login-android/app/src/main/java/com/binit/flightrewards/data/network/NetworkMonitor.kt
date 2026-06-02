package com.binit.flightrewards.data.network

import kotlinx.coroutines.flow.StateFlow

/**
 * Minimal network monitor.
 */
interface NetworkMonitor {
  val isOnline: StateFlow<Boolean>
}
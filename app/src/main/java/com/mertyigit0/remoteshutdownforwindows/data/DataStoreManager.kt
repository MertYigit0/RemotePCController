package com.mertyigit0.remoteshutdownforwindows.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("device_preferences")

class DataStoreManager(context: Context) {
    private val dataStore = context.dataStore

    val deviceName: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[DEVICE_NAME_KEY] ?: ""
        }

    val macAddress: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[MAC_ADDRESS_KEY] ?: ""
        }

    val ipAddress: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[IP_ADDRESS_KEY] ?: ""
        }

    val networkIpAddress: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[NETWORK_IP_ADDRESS_KEY] ?: ""
        }

    val wakeOnLanPort: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[WAKE_ON_LAN_PORT_KEY] ?: ""
        }

    suspend fun saveDevice(computerModel: ComputerModel) {
        dataStore.edit { preferences ->
            preferences[DEVICE_NAME_KEY] = computerModel.deviceName
            preferences[MAC_ADDRESS_KEY] = computerModel.macAddress
            preferences[IP_ADDRESS_KEY] = computerModel.ipAddress
            preferences[NETWORK_IP_ADDRESS_KEY] = computerModel.networkIpAddress
            preferences[WAKE_ON_LAN_PORT_KEY] = computerModel.wakeOnLanPort
        }
    }

    companion object {
        private val DEVICE_NAME_KEY = stringPreferencesKey("device_name")
        private val MAC_ADDRESS_KEY = stringPreferencesKey("mac_address")
        private val IP_ADDRESS_KEY = stringPreferencesKey("ip_address")
        private val NETWORK_IP_ADDRESS_KEY = stringPreferencesKey("network_ip_address")
        private val WAKE_ON_LAN_PORT_KEY = stringPreferencesKey("wake_on_lan_port")
    }
}
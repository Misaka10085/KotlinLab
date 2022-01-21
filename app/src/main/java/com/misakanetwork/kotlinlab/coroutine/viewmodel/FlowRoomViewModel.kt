package com.misakanetwork.kotlinlab.coroutine.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.misakanetwork.kotlinlab.coroutine.db.AppDatabase
import com.misakanetwork.kotlinlab.coroutine.db.SignerInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * Created By：Misaka10085
 * on：2022/1/20
 * package：com.misakanetwork.kotlinlab.coroutine.viewmodel
 * class name：FlowRoomViewModel
 * desc：FlowRoomViewModel
 */
class FlowRoomViewModel(app: Application) : AndroidViewModel(app) {
    fun insert(uid: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            AppDatabase.getInstance(getApplication())
                .signerDao()
                .insert(SignerInfo(uid.toInt(), firstName, lastName))
            Log.e("flowRoom", "insert signer:$uid")
        }
    }

    fun getAll(): Flow<List<SignerInfo>> {
        return AppDatabase.getInstance(getApplication())
            .signerDao()
            .getAll()
            .catch { e ->
                e.printStackTrace()
            }
            .flowOn(Dispatchers.IO)
    }
}
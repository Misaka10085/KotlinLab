package com.misakanetwork.kotlinlab.jetpack.activity.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.ObservableField
import com.misakanetwork.kotlinlab.jetpack.bean.UserBean

/**
 * Created By：Misaka10085
 * on：2021/11/26
 * package：com.misakanetwork.kotlinlab.jetpack.activity.viewmodel
 * class name：UserViewModel
 * desc：UserViewModel，DataBinding联系Bean与控件的通信逻辑
 */
// 使用ObservableField方式同步bean和控件值的操作
class UserViewModel(private var userBean: UserBean) {
    private var userBeanObservableField: ObservableField<UserBean>

    init {
        userBean = UserBean("Quin") // userName设置一个默认值
        // 关联ObservableField与UserBean
        userBeanObservableField = ObservableField()
        userBeanObservableField.set(userBean)
    }

    var userName: String?
        get() = userBeanObservableField.get()?.userName
        @SuppressLint("LongLogTag")
        set(value) {
            userBeanObservableField.get()?.userName = value
            Log.e(
                "UserBean DataBinding data", "setUserName=${userName}"
            )
        }

    // Java写法
//    // xml控件自动找到set，当Bean对应值改变时更新控件
//    fun getUserName(): String? = userBeanObservableField.get()?.userName
//
//    @SuppressLint("LongLogTag")
//    fun setUserName(userName: String?) {
//        // xml对应控件值发生变化，ObservableField自动通知Bean更新该value
//        userBeanObservableField.get()?.userName = userName
//        Log.e(
//            "UserBean DataBinding data", "setUserName=${userName}"
//        )
//    }
}

// 继承BaseObservable方式同步bean和控件值的操作
//class UserViewModel(private var userBean: UserBean) : BaseObservable() {
//
//    init {
//        userBean = UserBean("Quin") // userName设置一个默认值
//    }
//
//    @get:Bindable
//    var userName: String? // java中为自定义get set方法，并在get方法头顶加@Bindable
//        get() = userBean.userName // 数据变化时被调用，赋值给控件
//        @SuppressLint("LongLogTag")
//        set(userName) { // userName参数即控件改变时实时传入的值
//            // 控件监听变化，同步赋值给对象bean，保持控件值与数据bean同步
//            if (userName != null && userName != userBean.userName) {
//                userBean.userName = userName
//                notifyPropertyChanged(BR.userName) // 通知数据bean值变化，即setUserName操作
//                Log.e(
//                    "UserBean DataBinding data", "setUserName=${userName}"
//                )
//            }
//        }
//}
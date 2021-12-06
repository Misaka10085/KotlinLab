package com.misakanetwork.kotlinlab.jetpack.fragment

import android.app.PendingIntent
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.misakanetwork.kotlinlab.R
import com.misakanetwork.kotlinlab.jetpack.bean.IdolBean
import kotlinx.android.synthetic.main.fragment_navigation_home.*

/**
 * Created By：Misaka10085
 * on：2021/11/29
 * package：com.misakanetwork.kotlinlab.jetpack.fragment
 * class name：NavigationHomeFragment
 * desc：NavigationHomeFragment DeepLink PendingIntent
 */
class NavigationHomeFragment : Fragment(R.layout.fragment_navigation_home) {
    private var notificationId: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enter_btn.setOnClickListener {
            val args = NavigationHomeFragmentArgs.Builder()
                .setUserName("Quin")
                .setAge(22)
                .setIdol(IdolBean("Quin", 3, "QUIN", "qqq", null))
                .build().toBundle()
            val naviController = Navigation.findNavController(it) // 自动找到navigation_graph设置的路由
            naviController.navigate(
                R.id.action_navigationHomeFragment_to_navigationDetailFragment,
                args
            ) // navigation_graph对应的actionId;args中设置的参数为action中配置的传参类型
        }
        // DeepLink
        send_notification_btn.setOnClickListener {
            sendNotification()
        }
    }

    private fun sendNotification() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                activity?.packageName,
//                "MyChannel",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            channel.description = "My NotificationChannel"
//            val notificationManager = activity?.getSystemService(NotificationManager::class.java)
//            notificationManager?.createNotificationChannel(channel)
//        } else {
//
//        }
        val notification =
            NotificationCompat.Builder(requireActivity(), requireActivity().packageName)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Deep Link")
                .setContentText("Click me")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(getPendingIntent())
                .setAutoCancel(true)
                .build()
        val notificationManagerCompat = NotificationManagerCompat.from(requireActivity())
        notificationManagerCompat.notify(notificationId++, notification)
    }

    private fun getPendingIntent(): PendingIntent {
        val args = NavigationHomeFragmentArgs.Builder()
            .setUserName("Quin")
            .setAge(22)
            .setIdol(IdolBean("Quin", 3, "QUIN", "qqq", null))
            .build().toBundle()
        return Navigation.findNavController(requireActivity(), R.id.send_notification_btn)
            .createDeepLink()
            .setGraph(R.navigation.navigation_graph)
            .setDestination(R.id.navigationDetailFragment)
            .setArguments(args)
            .createPendingIntent()
    }
}
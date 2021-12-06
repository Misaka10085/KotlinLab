package com.misakanetwork.kotlinlab.jetpack.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.misakanetwork.kotlinlab.R
import kotlinx.android.synthetic.main.fragment_navigation_detail.*

/**
 * Created By：Misaka10085
 * on：2021/11/29
 * package：com.misakanetwork.kotlinlab.jetpack.fragment
 * class name：NavigationDetailFragment
 * desc：NavigationDetailFragment DeepLink uri
 */
class NavigationDetailFragment : Fragment(R.layout.fragment_navigation_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: NavigationHomeFragmentArgs =
            NavigationHomeFragmentArgs.fromBundle(requireArguments())
        val userName = args.userName
        val age = args.age
        val idol = args.idol
        arguments_tv.text = "userName: $userName age $age \n idolBean= $idol"
        back_btn.setOnClickListener {
            val naviController = Navigation.findNavController(it)
//            naviController.navigate(R.id.action_navigationDetailFragment_to_navigationHomeFragment)
            naviController.navigateUp()
        }

        // DeepLink uri ,navigation_graph.xml中定义DeepLink uri，Manifest activity中配置标签
        val arg = arguments
        if (arg != null) {
            val params = arg.getString("params")
            Log.e("Navigation DeepLink uri", params.toString())
        }
    }
}
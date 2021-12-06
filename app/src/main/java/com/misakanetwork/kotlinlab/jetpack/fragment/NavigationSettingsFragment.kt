package com.misakanetwork.kotlinlab.jetpack.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.misakanetwork.kotlinlab.R

/**
 * Created By：Misaka10085
 * on：2021/12/1
 * package：com.misakanetwork.kotlinlab.jetpack.fragment
 * class name：NavigationSettingsFragment
 * desc：状态栏Settings Fragment
 */
class NavigationSettingsFragment : Fragment(R.layout.fragment_navigation_settings) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear() // 清除掉AppBar的menu
        super.onCreateOptionsMenu(menu, inflater)
    }

}
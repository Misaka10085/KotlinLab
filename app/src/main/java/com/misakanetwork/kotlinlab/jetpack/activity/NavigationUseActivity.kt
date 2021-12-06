package com.misakanetwork.kotlinlab.jetpack.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.misakanetwork.kotlinlab.R

/**
 * Created By：Misaka10085
 * on：2021/11/29
 * package：com.misakanetwork.kotlinlab.jetpack.activity
 * class name：NavigationUseActivity
 * desc：NavigationUseActivity(res下创建navigation类型布局->创建主fragment和子fragment->Activity xml FragmentContainerView)
 * 动画、参数等由navigation_graph 右边属性栏设置
 * 传参：项目gradle添加依赖;主module上 plugins{id 'androidx.navigation.safeargs'}，不支持List，建议都使用ViewModel+DataBinding去实现
 *
 * DeepLink PendingIntent:NavigationHomeFragment
 * DeepLink uri:NavigationDetailFragment
 */
class NavigationUseActivity : AppCompatActivity(R.layout.activity_navigation_use) {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object {
        fun startThis(context: Context) {
            context.startActivity(
                Intent(
                    context.applicationContext,
                    NavigationUseActivity::class.java
                )
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置FragmentContainerView为管理
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        // 为actionbar添加controller
//        NavigationUI.setupActionBarWithNavController(this, navController)
        // 为actionbar、appbar添加controller
        appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        // 监听页面切换完成
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.e(
                "NavigationListener",
                "onDestinationChanged"
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // 重写返回按钮事件
//        return navController.navigateUp()
        return NavigationUI.navigateUp(
            navController,
            appBarConfiguration
        ) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_settings, menu) // 填充自定义menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            navController
        ) || super.onOptionsItemSelected(item) // 将导航事件交给NavigationUI
    }
}
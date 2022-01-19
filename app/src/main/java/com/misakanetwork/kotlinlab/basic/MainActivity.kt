package com.misakanetwork.kotlinlab.basic

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.misakanetwork.kotlinlab.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rangeFun()
        whenFun()
        stringTemplateFun()
        tooMuchParameterInFun(age = 0, name = "sfppp")
        发放违法了无法文化specialNameFun()
        varFun()
        println("Unit fun " + varFun()) // ==void
//        TODO("nothing") // ==throw
        anonymousFun() // 匿名函数
        functionFun() // 函数作参的函数
        passingFunReferenceFun() // 具名函数转为值参
        returnFunctionFun() // 函数作返回(高级函数) 类似java调用方法中使用匿名方法实现interface
        var str: String? = "bbb" // 可空字符串类型 可null ?.为空则跳过
        useOfLet() // 变量操作
        str!!.reversed() // 非空断言操作符 为空也执行 try catch去捕获
        str = str?.reversed()?.plus("is great") // 链式调用
        nullMerge() // 空合并操作符 为空则执行
        exceptionCatch() // ?:处理空抛出异常
        substringRange() // substring until
        splitString() // split、解构语法
        compareSymbol() // ==/===
        forEach()
        safetyTrans() // 安全转换函数
        applyFun()
        letFun()
        runFun()
        withFun()
        alsoFun()
        takeIfFun() // takeIf、takeUnless
        listFun()
        setFun()
        arrayFun()
        mapFun()
        bean()
        construction()
        lateInit()
        lazyInit() // 惰性初始化、类继承、is类型检测、as类型转换
        any()
        objectUse() // object关键字、作为对象表达式的使用
        innerClass() // 伴生对象
        dataClass() // 数据类
        copyFun() // 数据类实现的copy方法
        deconstructionStatement() // 解构声明
        reloadOperator() // 运算符重载
        enumClass()
        enumCompare() // 代数数据类型（enum的when比较使用）
        sealedClass() // 密封类 子类必须和它定义在同一个文件夹里
        interfaceDefine()
        abstractClass() // 抽象类
        magicBoxClass() // 泛型类
        magicBoxFun() // 泛型函数、vararg
        magicBoxInOut() // 泛型函数in、out类型参数
        magicBoxReified() // reified检查泛型参数是什么类型
        extensionFunction() // 定义扩展函数、泛型扩展函数、扩展属性、可空类型扩展函数
        extensionFile() // 定义扩展文件、重命名扩展
        mapFunction()
        flatMapFunction()
        filterFunction()
        zipFunction()
        foldFunction()
        sequenceList() // 序列、使用generateSequence实现未知长度序列操作
        javaKotlinTrans() // java与kotlin的互操作性与可空性、@JvmName、@JvmFiled、@JvmOverloads、@Throws、函数类型操作
        // apply plugin: 'kotlin-android-extensions'
        text_tv.setOnClickListener {
            Log.e("onClick", "clicked")
        }
    }

    private fun rangeFun() {
        val age = 0
        if (age !in 0..3) { // in x..x :全闭区间
            Log.e("rangeFun", "not in 0..3")
        } else if (age !in 3..12) {
            Log.e("rangeFun", "not in 3..12")
        }

        for (num in 0 until 1) { // 左闭右开区间
            Log.e("rangeFun", "until each in IntRange >>> $num")
        }
        val numbers = 0..4
        numbers.forEach {
            Log.e("rangeFun", "forEach in .. IntRange >>> $it")
        }
        // step 跳过
        val stepTarget = 0 until 3
        for (num in stepTarget.reversed() step 2) {
            Log.e(
                "rangeFun",
                "step in reversed until IntRange jump 2 >>> $num , total ${stepTarget.count()}"
            )
        }
    }

    private fun whenFun() {
        val school = "ppp"
        val level = when (school) { // 相当于switch case
            "ccc" -> "ccca"
            "ppp" -> "pppa"
            "lll" -> "llla"
            else -> "others"
        }
        Log.e("whenFun", level)
    }

    private fun stringTemplateFun() {
        val origin = "jppp"
        val next = "pccccc"
        Log.e("stringTemplateFun", "join $origin with $next")
        val flag = false
        Log.e("stringTemplateFun", "result is ${if (flag) origin + next else "false"}")
    }

    private fun tooMuchParameterInFun(name: String, age: Int = 2) {
    }

    private fun `发放违法了无法文化specialNameFun`() {
        // Java方法含有kotlin关键字时，kotlin调用时使用
    }

    private fun varFun() {
        val i = { x: Int, y: Int -> x + y } // 相当于fun i(x: Int, y: Int) = x + y
        val result = i(1, 2)
        Log.e("varFun", "$result")
    }

    private fun anonymousFun() {
        val total = "sfpppp".count()
        val totals = "sfpppp".count({ letter ->
            letter == 'p'
        })
        Log.e("anonymousFun", "total:$total,totals:$totals")

        val blsFun: () -> String // 匿名函数类型的变量 无参 返回String
//        val blsFun: () -> String={... ...}
//        val blsFun = {
//            ...
//            ...
//        }
        blsFun = {
            val name = "sfppp"
            "welcome $name" // 自动返回最后行结果
        }
        Log.e("anonymousFun val", blsFun())
        val bls2Fun: (String) -> String = {
            "$it xxxx"
        }
        Log.e("anonymousFun it", bls2Fun("sfpppp"))
        val bls1Fun: (String, Int) -> String = { name, num ->
            val name1 = "Quin"
            "welcome $name1,$name$num"
        }
//        val bls1Fun = { name: String, num: Int ->
//            ...
//            ...
//        }
        Log.e("anonymousFun multiple", bls1Fun("sfpppp", 1))
    }

    private fun functionFun() {
//        val getResult = { inputName: String, hour: Int ->
//            val year = 2021
//            "${year}Quin$inputName"
//        }
//        outside("sfppp", getResult)
        outside("sfppp") { inputName: String, hour: Int ->
            val year = 2021
            "${year}Quin$inputName"
        }
    }

    private fun outside(name: String, getInside: (String, Int) -> String) {
        Log.e("functionFun", getInside(name, (1..24).shuffled().last())) // 1~24乱序取最后
    }

    private fun passingFunReferenceFun() {
        outside("Quin", ::getFun)
    }

    private fun getFun(name: String, year: Int): String {
        return "${year}/$name"
    }

    private fun returnFunctionFun() {
        val getRes = getReturnFunction()
        val currentYear = 2021
        val hour = (1..24).shuffled().last()
        Log.e("returnFunctionFun", getRes("goodsName", currentYear, hour))
    }

    private fun getReturnFunction(): (String, Int, Int) -> String {
//        val currentYear = 2021
//        val hour = (1..24).shuffled().last() // 闭包：匿名函数引用定义自身的函数里的变量
        return { goodsName: String, currentYear: Int, hour: Int ->
            "goodsName:$goodsName  year:$currentYear hour:$hour"
        }
    }

    private fun useOfLet() {
        var str = "butterfly"
        str?.let { // 返回lambda最后一行结果
            if (it.isNotBlank()) { // 非空
                str = it.reversed()
            } else { // 为空
                "butterfly n"
            }
        }
        Log.e("useOfList", str)
    }

    private fun nullMerge() {
        var str: String? = null
        str = str?.let { it.reversed() } ?: "butterfly"
        Log.e("nullMerge", str)
    }

    private fun exceptionCatch() {
        var number: Int? = null
        try {
            checkException(number)
            checkNotNull(number, { "is null" }) // 先决条件函数 检查异常并抛出
            number!!.plus(1)
        } catch (e: Exception) {
            Log.e("exceptionCatch", e.message.toString())
        }
    }

    private fun checkException(number: Int?) {
        number ?: throw UnskilledException()
    }

    class UnskilledException() : IllegalArgumentException("error") // 自定义异常

    private fun substringRange() {
        val str = "abcdefg"
        val index = str.indexOf("e")
        Log.e("substring range", str.substring(0 until index)) // until 左闭右开
    }

    private fun splitString() {
        val str = "name,age,sex"
        val result: List<String> = str.split(",")
        Log.e("splitString", result.toString())
        val (origin, next, ppp) = str.split(",") // 解构语法 依次赋值
        Log.e("splitString", "$origin/$next/$ppp")

        val str1 = str.replace(Regex("[nas]")) {
            when (it.value) {
                "n" -> "p"
                "a" -> "z"
                "s" -> "q"
                else -> it.value
            }
        }
        Log.e("splitString Regex", str1)
    }

    private fun compareSymbol() {
        val str1 = "Quin"
        var str2 = "Quin"
        Log.e("compareSymbol", "== ${str1 == str2}") // 比较内容
        Log.e("compareSymbol", "=== ${str1 === str2}") // 比较是否指向内存堆同一对象
        str2 = "ppp"
        Log.e("compareSymbol", "=== ${str1 === str2}")
    }

    private fun forEach() {
        val str = "sfpppp"
        str.forEach {
            Log.e("forEach", it.toString())
        }
    }

    private fun safetyTrans() {
        val num: Int? = "0.89".toIntOrNull()
        Log.e("safetyTrans", num.toString())
        val num1: Int = 0.89545.roundToInt() // 向上
        val num2: Int = 0.89545.toInt() // 向下
        Log.e("safetyTrans roundInt()", num1.toString())
        Log.e("safetyTrans toInt()", num2.toString())
        val num3: String = "%.2f".format(0.89545) // 保留小数点后两位并最后一位向上取整
        Log.e("safetyTrans format", num3)
    }

    private fun applyFun() {
        var file =
            File("E:// I HAVE A COPY.txt").apply { // 配置对象(this) 返回当前接收者 可看作配置函数 原理结合源码以及泛型扩展函数部分 addExt()，
                // T.()->Unit即无返回值的泛型匿名函数，且扩展函数默认持有调用者，所以可以this.xx(即隐式调用)
                setReadable(true)
                setWritable(true)
                setExecutable(false)
            }
    }

    private fun letFun() {
        val result = listOf(3, 2, 1).first().let { // 返回lambda表达式执行结果
            it * it // 返回最后一行
        }
        Log.e("letFun", result.toString())
        Log.e("letFun ?. ?:", formatGreeting(null))
    }

    private fun formatGreeting(guestName: String?): String {
        return guestName?.let { // 链式调用风格
            "Welcome $it"
        } ?: "What's your name"
    }

    private fun runFun() {
        var file = "E:// I HAVE A COPY.txt"
        val result = file.run { // 返回lambda结果，不返回接受者
            file.contains("I")
        }
        Log.e("runFun", result.toString())

        val result1 = file.run(::isLong)
        Log.e("runFun", result1.toString())
        val result2 = file.run(::isLong) // 执行函数引用 逐级传递结果
            .run(::showMessage)
            .run(::println)
    }

    private fun isLong(name: String) = name.length > -10

    private fun showMessage(isLong: Boolean): String {
        return if (isLong) {
            "Name is too long"
        } else {
            "Please rename"
        }
    }

    private fun withFun() {
        // run的变体
        val result1 = "Quin is a dog".run {
            length > 10
        }
        val result = with("Quin is a dog") {
            length >= 10
        }
        Log.e("withFun", result.toString())
    }

    private fun alsoFun() {
        var fileContents: List<String>
        "E://stupid.txt".also { // 返回接收对象，适合针对同一原始对象
            println(it)
        }.also {
            fileContents = listOf(it.reversed())
        }
        Log.e("alsoFun", fileContents.toString());
    }

    private fun takeIfFun() {
        // 判断某个条件是否满足，lambda表达式结果true/false 为false不返回内容，返回null，为true返回原始接收者对象
        val result = File("E://stupid")
            .takeIf { it.exists() && it.canRead() }
            ?.readText()
        Log.e("takeIfFun", result.toString())

        // 与takeIf相反
        val result1 = "E://stupid"
            .takeUnless { it.length > 12 }?.reversed()
        Log.e("takeUnless", result1.toString())
    }

    private fun listFun() {
        val ls = listOf("Quin", "sfppp", "kenny")
        val part6 = ls.getOrElse(6) { "UNKNOWN" }
        Log.e("listFun getOrElse", part6)
        val part2 = ls.getOrNull(2) ?: "UNKNOWN"
        Log.e("listFUn getOrNull", part2)
//        ls.remove() (X)
        ls.toMutableList().add("JK")

        // 可变集合
        val mList = mutableListOf("Quin", "sfppp", "kenny")
        mList.add("JK")

        mList += "er"
        mList -= "Quin"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mList.removeIf { it.contains("sfppp") }
        }
        Log.e("listFun mutable +=", mList.toString())

        mList.forEach {
            Log.e("listFun forEach", it)
        }
        mList.forEachIndexed { index, item ->
            Log.e("listFun forEachIndexed", "index:$index item:$item")
        }

        val (quin: String, _: String, sfppp: String) = mList // _元素不被赋值
    }

    private fun setFun() {
        // 同java一样，不允许集合中有重复元素
        val sets = setOf("Quin", "sfppp", "kenny", "kenny")
//        val element3 = sets.elementAt(3) (X)
        val element2 = sets.elementAt(2)
        val element3 = sets.elementAtOrNull(3)
        sets.elementAtOrElse(3) { sets.elementAt(0) }
        Log.e("setFun", sets.toString())

        val mutableSet = mutableSetOf("Quin", "sfppp", "kenny", "kenney")
        mutableSet += "er"

        // 去重
//        val mList = listOf("Quin", "sfppp", "kenny", "kenny")
//            .toSet()
//            .toList()
        val mList = listOf("Quin", "sfppp", "kenny", "kenny").distinct()
        Log.e("setFun 去重", mList.toString())
    }

    private fun arrayFun() {
        val intArray = intArrayOf(10, 30, 40)
        val listInt = listOf(10, 20, 30).toIntArray() // list->array
        val arrays = arrayOf("ppp", "qqq", "kkk")
    }

    private fun mapFun() {
        val mapT = mapOf("Quin" to 22, "sfppp" to 11, "kenny" to 10, "JK" to 1)
        val mapS = mapOf(Pair("Quin", 22), Pair("sfppp", 11))
        val result = mapT["Quin"]
        val resultContent = mapT.getValue("Quin")
        val resultContent1 = mapT.getOrElse("Quin") { "UNKNOWN" }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val resultContent2 = mapT.getOrDefault("Quin", 0)
        }

        mapT.forEach {
            Log.e("mapFun forEach", it.value.toString())
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mapT.forEach { (key: String, value: Int) ->
                Log.e("mapFun", "key:$key value:$value")
            }
        }

        val mutableMap = mutableMapOf("Quin" to 22, "sfppp" to 11, "kenny" to 10, "JK" to 1)
        mutableMap += "Huang" to 30
        mutableMap.put("er", 1)
        mutableMap["er"] = 1
        val getResult = mutableMap.getOrPut("Men") { 123 } // 不存在就添加
    }

    private fun bean() {
        val playerBean = PlayerBean()
        val name = playerBean.name
        playerBean.name = " Quiin "
        Log.e("bean", playerBean.name.toString())
    }

    open class PlayerBean {
        var name: String? = "Quin" // 自动生成field get set
            get() = field?.replace("Q", "p")
            set(value) {
                field = value?.let { value.trim() } ?: "need a name"
            }

        val age: Int
            get() = (1..6).shuffled().first()

        open fun load() = "loading ... "
    }

    private fun construction() {
        var p = Player(age = 33, isNormal = false, nickName = "Quin33")
        var p1 = Player("Quin")
        val p2 = Player("sfppp", 8)
        Log.e("construction", p2.age.toString())
    }

    open class Player( // 主构造函数部分
        name: String = "QQQQ", // 不传时默认赋值
        age: Int, // 只用一次的使用下划线开头_age
        isNormal: Boolean,
        var nickName: String // 可直接在构造函数里定义属性
    ) {
        var name = name
            get() = field?.replace("Q", "p")
            set(value) {
                field = value?.let { value.trim() } ?: "need a name"
            }
        var age = age
            get() = field.absoluteValue
            set(value) {
                field = age.absoluteValue
            }
        var isNormal = isNormal

        constructor(name: String) : this( // 次构造函数
            name,
            age = 0,
            isNormal = false,
            nickName = ""
        )

        constructor(name: String, age: Int) : this(
            name,
            0, // 默认值
            isNormal = false,
            nickName = ""
        ) {
            this.name = name.toUpperCase()
            age.takeIf { age > 10 }?.let { this.age = age } ?: run { this.age = 0 }
        }

        init {
            // 初始化块 在构造函数前执行
//            require(age > 0) { "age must be positive" }
//            require(name.isNullOrEmpty()) { "player must have a name" }
        }

        open fun load() = "Nothing..."
    }

    private fun lateInit() {
        val m = Music()
        m.equipment = "sss"
        m.play()
    }

    class Music {
        lateinit var equipment: String

        fun play() {
            if (::equipment.isInitialized) {
                Log.e("lateInit music", equipment)
            }
        }
    }

    private fun lazyInit() {
        val e = Equipment("VIOL")
        val content = e.config
        Log.e("lazy", content)
        val p: Player = Equipment("Quin")
        Log.e("extends 多态", p.load()) // 此处结果为继承类的方法重写调用
        Log.e("is", (p is Player).toString())
        Log.e("is", (p is Equipment).toString())
        Log.e("is", (p is File).toString())
        if (p is Equipment) {
            (p as Equipment).loadConfig()
        }
    }

    class Equipment(name: String) : Player( // 继承Open修饰的类，默认类是final的无法继承
        "QQQQ", // 不传时默认赋值
        1, // 只用一次的使用下划线开头_age
        false,
        "nickName"// 可直接在构造函数里定义属性)
    ) {
        var userName = name
        val config by lazy { loadConfig() } // 惰性初始化 使用时才会初始化执行

        fun loadConfig(): String {
            return "xxx"
        }

        override fun load(): String { // 重写父类方法，父类方法也需要open修饰
//            return super.load()
            return "put"
        }
    }

    private fun any() {
        val p: Player = Equipment("Quin")
        Log.e("any", (p is Any).toString()) // Any相当于Java的Object
    }

    private fun objectUse() {
        ApplicationConfig.doSomething()
        val p = object : PlayerBean() { // 作为对象表达式的使用
            override fun load(): String = "loading obbbb..." // 匿名内部类里重载方法
        }
        p.load()
        ConfigMap.load()
    }

    object ApplicationConfig { // 单例模式，object的对象的声明用法
        init {
            Log.e("object", "ApplicationConfig loading...")
        }

        fun doSomething() = Log.e("object", "doSomething")
    }

    class ConfigMap {
        companion object { // 作为伴生对象使用，只有被调用时才会被创建
            private const val PATH = "xxx"

            fun load() = Log.e("companion object", PATH)
        }
    }

    private fun innerClass() {
        Players.Equipments("sharp knife").show()
    }

    class Players {
        class Equipments(var name: String) { // java中需要static，kotlin不需要
            fun show() = Log.e("innerClass", "equipment:$name")
        }

        fun battle() {

        }
    }

    private fun dataClass() {
        Log.e("dataClass", Coordinate(10, 10).toString()) // data类自动实现了toString
        Log.e(
            "dataClass",
            (Coordinate(10, 10) == Coordinate(10, 10)).toString()
        ) // data类自动实现了equals，不再使用Any的===比较
        val (x, y) = Coordinate(10, 20) // data类自动实现了解构声明
    }

    data class Coordinate(var x: Int, var y: Int) { // 必须至少带有一个含参构造函数;不能使用abstract、open、sealed和inner
        val isInBounds = x > 0 && y > 0
    }

    private fun copyFun() {
        val s = Student("Quin") // 构建的次构造函数对象
        val copy = s.copy("sfppp") // copy使用的主构造函数的对象，不会运行次构造函数的score=10
        Log.e("copy", s.toString())
        Log.e("copy", copy.toString())
    }

    data class Student(var name: String, val age: Int) {
        private val hobby = "music"
        val subject: String
        var score = 0

        init {
            Log.e("copy fun data class", "init")
            subject = "math"
        }

        constructor(_name: String) : this(_name, 10) {
            score = 10
        }

        override fun toString(): String {
            return "Student(name='$name', age=$age, hobby='$hobby', subject='$subject', score=$score)"
        }
    }

    private fun deconstructionStatement() {
        val (x, y) = PlayerScore(10, 20)
        val (z, e) = Coordinate(10, 20) // data类自动实现了解构声明
    }

    class PlayerScore(val experience: Int, val level: Int) {
        operator fun component1() = experience // 固定写法，解构声明需要使用operator componentX
        operator fun component2() = level
    }

    private fun reloadOperator() {
        val c1 = Coordinate2(10, 20)
        val c2 = Coordinate2(10, 20)
        Log.e("reloadOperator", (c1 + c2).toString())
    }

    data class Coordinate2(var x: Int, var y: Int) {
        val isInBounds = x > 0 && y > 0

        operator fun plus(other: Coordinate2) = Coordinate2(x + other.x, y + other.y) // 重载默认的plus方法
    }

    private fun enumClass() {
        Log.e("enum", Direction.EAST.toString())
        Log.e("enum", (Direction.EAST is Direction).toString())
        Log.e("enum fun", Direction2.EAST.updateCoordinate(Coordinate(10, 20)).toString())
    }

    enum class Direction {
        EAST,
        WEST,
        SOUTH,
        NORTH
    }

    enum class Direction2(private val coordinate: Coordinate) {
        EAST(Coordinate(1, 0)),
        WEST(Coordinate(2, 3)),
        SOUTH(Coordinate(4, 1)),
        NORTH(Coordinate(11, 2));

        fun updateCoordinate(playerCoordinate: Coordinate) =
            Coordinate(
                playerCoordinate.x + coordinate.x,
                playerCoordinate.y + coordinate.y
            ) // 枚举中定义函数
    }

    private fun enumCompare() {
        val driver: Driver = Driver(LicenseStatus.QUALIFIED)
        Log.e("enumCompare", driver.checkLicense())
    }

    enum class LicenseStatus {
        UNQUALIFIED,
        LEARNING,
        QUALIFIED;
    }

    class Driver(var status: LicenseStatus) {
        fun checkLicense(): String {
            return when (status) {
                LicenseStatus.UNQUALIFIED -> "没资格"
                LicenseStatus.LEARNING -> "在学"
                LicenseStatus.QUALIFIED -> "有资格"
            }
        }
    }

    private fun sealedClass() {
        val status = LicenseStatus2.Qualified("3093499") // 使用密封类以传参
        val driver = Driver2(status)
        Log.e("sealedClass", driver.checkLicense())
    }

    sealed class LicenseStatus2 {
        // 密封类的子类
        object Unqualified : LicenseStatus2()
        object Learning : LicenseStatus2()
        class Qualified(val licenseId: String) : LicenseStatus2()
    }

    class Driver2(var status: LicenseStatus2) {
        fun checkLicense(): String {
            return when (status) {
                is LicenseStatus2.Unqualified -> "没资格"
                is LicenseStatus2.Learning -> "在学"
                is LicenseStatus2.Qualified -> "有资格，驾驶证编号：${(this.status as LicenseStatus2.Qualified).licenseId}" // 需要指明上下文，否则无法识别
            }
        }
    }

    private fun interfaceDefine() {
        val car = Car("car")
        car.maxSpeed = 2
        Log.e("interface", car.maxSpeed.toString())
    }

    interface Movable {
        val maxSpeed: Int
            get() = (1..500).shuffled().last()
        var wheels: Int

        fun move(movable: Movable): String
    }

    class Car(_name: String, override var wheels: Int = 4) : Movable { // 所有接口属性和函数实现都要使用override
        override var maxSpeed: Int = 0
            get() = super.maxSpeed
            set(value) {
                field = maxSpeed.plus(value)
            }

        override fun move(movable: Movable): String {
            return "movable"
        }
    }

    private fun abstractClass() {
        val fn = FN57(2000)
        Log.e("abstract class", fn.pullTrigger())
    }

    abstract class Gun(val range: Int) {
        protected fun doSomething() {
        }

        abstract fun pullTrigger(): String
    }

    class FN57(val price: Int) : Gun(range = 500), Movable { // 多重继承
        override fun pullTrigger(): String {
            return (range / 2).toString()
        }

        override var wheels: Int = 0

        override fun move(movable: Movable): String {
            return super.toString()
        }
    }

    private fun magicBoxClass() {
        val boxx: MagicBoxx<Boy> = MagicBoxx(Boy("Quin", 22)) // 泛型类
//        val boxx2: MagicBoxx<Dog> = MagicBoxx(Dog(33))
    }

    class MagicBoxx<T>(item: T) {  // T:泛型类型，继承Human作为类型约束
        var avalilable = false
        private var subject: T = item

        fun fetch(): T? {
            return subject.takeIf { avalilable }
        }

        fun <R> fetch(subjectModFunction: (T) -> R): R? { // 传入匿名函数作为参数，fetch函数返回新类型(<R> ... :R)，泛型函数返回该新类型
            return subjectModFunction(subject).takeIf { avalilable }
        }
    }

    open class Human(val age: Int)

    class Boy(val name: String, age: Int) : Human(age) {
        override fun toString(): String {
            return "Boy(name='$name',age='$age')"
        }
    }

    class Dog(val weight: Int)

    class Man(val name: String, age: Int) : Human(age) {
        override fun toString(): String {
            return "Man(name='$name',age='$age')"
        }
    }

    private fun magicBoxFun() {
        val box = MagicBoxx(Boy("Quin", 22))
        box.avalilable = true
        box.fetch()?.run {
            Log.e("magicBox fun", "avalilable ${this.name}}")
        }
        // 多类型泛型函数
        val man: Man? = box.fetch {
            Man(it.name, it.age.plus(15)) // 传入Man，返回Man类型对象
        }

        // vararg
        val box1 = MagicBoxx2(
            Boy("Quin", 22),
            Boy("sfppp", 123),
            Boy("Kenny", 12),
        )
        box1.avalilable = true
        box1.fetch(1)?.run {
            Log.e("magicBox vararg", "find $name")
        }

        box1[0] // 重写泛型类的get函数后调用
    }

    class MagicBoxx2<T : Human>(vararg item: T) { // vararg:可变参数，可以传入多个，java中是int a..
        var avalilable = false
        private var subject: Array<out T> =
            item // out:协变，泛型类只将泛型类型作为函数的返回，则用out来生产指定的泛型对象.这里Human和其子类类型都可赋值给父类类型对象Human
        // in:逆变，泛型类只将泛型类型作为函数的入参，则用in来消费指定的泛型对象 interface Consumer<in T>{fun consume(item:T)}
        // 泛型类既将泛型类型作为函数参数，又作为函数的输出，二者都不使用：interface ProductionConsumer<T>{fun product():T;fun consume(item:T)}

        fun fetch(index: Int): T? { // 泛型类使用vararg后方法参数需要包含下标
            return subject[index].takeIf { avalilable }
        }

        fun <R> fetch(index: Int, subjectModFunction: (T) -> R): R? { // 传入匿名函数作为参数，返回新类型，泛型函数返回该新类型
            return subjectModFunction(subject[index]).takeIf { avalilable }
        }

        operator fun get(index: Int): T? =
            subject[index]?.takeIf { avalilable } // 重写get方法，提供magicBoxx2[index]
    }

    private fun magicBoxInOut() {
        // out:协变，泛型类只将泛型类型作为函数的返回，则用out来生产指定的泛型对象;子类泛型的对象可以赋值给父类泛型的对象
        // in:逆变，泛型类只将泛型类型作为函数的入参，则用in来消费指定的泛型对象;父类泛型的对象可以赋值给子类泛型的对象
        // 泛型类既将泛型类型作为函数参数，又作为函数的输出，二者都不使用
        val production1: Production<Food> = FoodStore()
        // 在java中，子类泛型类型对象不能赋值给父类泛型类型对象，其泛型不支持继承机制 ArrayList<CharSequence> list=new ArrayList<String>() (X)
        val production2: Production<Food> = FastFoodStore() // (Production)使用了协变，Kotlin则支持该特性
        val production3: Production<Food> = BurgerStore()

        val consumer1: Consumer<Burger> = Everybody() // (Consumer)使用了in逆变，父类泛型类型对象可以赋值给子类泛型类型对象
        val consumer2: Consumer<Burger> = ModernPeople()
        consumer2.consume(Burger()) // 实际类型是FastFood
        val consumer3: Consumer<Burger> = American()
    }

    interface Production<out T> {
        fun product(): T
    }

    interface Consumer<in T> {
        fun consume(item: T)
    }

    interface ProductConsumer<T> {
        fun product(): T
        fun consume(item: T)
    }

    open class Food

    open class FastFood : Food()

    class Burger : FastFood()

    // 生产者
    class FoodStore : Production<Food> {
        override fun product(): Food {
            Log.e("magicBoxInOut", "Produce Food.")
            return Food()
        }
    }

    class FastFoodStore : Production<FastFood> {
        override fun product(): FastFood {
            Log.e("magicBoxInOut", "Produce FastFood.")
            return FastFood()
        }
    }

    class BurgerStore : Production<Burger> {
        override fun product(): Burger {
            Log.e("magicBoxInOut", "Produce Burger.")
            return Burger()
        }
    }

    // 消费者
    class Everybody : Consumer<Food> {
        override fun consume(item: Food) {
            Log.e("magicBox in", "Eat Food")
        }
    }

    class ModernPeople : Consumer<FastFood> {
        override fun consume(item: FastFood) {
            Log.e("magicBox in", "Eat FastFood")
        }
    }

    class American : Consumer<Burger> {
        override fun consume(item: Burger) {
            Log.e("magicBox in", "Eat Burger")
        }
    }

    private fun magicBoxReified() {
        // 随机产生一个对象，若不是指定类型的对象，则通过backup()函数生成一个指定类型的对象
//        val box1: MagicBox<Human> = MagicBox()
        val box1: MagicBox<Man> = MagicBox()
        val subject: Man = box1.randomOrBackup {
            Man("Kenny", 38) // backup:()->T 由backup函数自动推断返回的类型是Man
        }
//        val subject: Boy = box1.randomOrBackup {
//            Boy("Kenny", 38)
//        }
        Log.e("reified get", subject.toString())
    }

    class MagicBox<T : Human>() { // 泛型类型会被传入的类型擦除掉，只有编译的时候才会获取到类型，所以需要reified关键字去判断
        inline fun <reified T> randomOrBackup(backup: () -> T): T { // inline:内联关键字，提高效率，reified必须与它一起使用
            val items = listOf(
                Boy("Quin", 22),
                Man("sfppp", 33)
            )
            val random: Human = items.shuffled().first() // 随机取一个
            Log.e("reified required", random.toString())
            return if (random is T) { // 被修饰了reified的泛型T可以被用来判断
                random // 若传入的T泛型类型是random，则返回random
            } else {
                backup() // 若传入的T泛型类型不是random，则通过backup()返回传入的泛型类型对象
            }
        }
    }

    private fun extensionFunction() {
        // 若某个类的方法没有open导致无法继承，可以直接用定义扩展函数的方式定义一个一模一样的方法
        Log.e("extensionFunction", "Quin".addExt(2))
        15.easyPrint()
        "eee".simplePrint().addExt(2).simplePrint()

        // 泛型扩展函数
        12.funnyPrint().toString().addExt(3).funnyPrint()
        val i = "abc".let { 50 }
        15.apply { }.also { }

        // 扩展属性
        "Quin is a fucking animal".numVowels.easyPrint()

        // 可空类型扩展函数
        val nullableString: String? = null
        nullableString.printWithDefault("Kenny")
        nullableString printWithDefault2 "JK"
    }

//    fun String.addExt(amount: Int = 1) = this + "!".repeat(amount)

    fun String.addExt(amount: Int = 1) =
        this + "!".repeat(count()) // this.count() 扩展函数自带了接收者对象的隐式调用

    private fun Any.easyPrint() = Log.e("extensionAny", this.toString()) // 定义private则作用域仅限于该文件

    private fun String.simplePrint(): String {
        Log.e("extension", this)
        return this
    }

    private fun <T> T.funnyPrint(): T { // 泛型扩展函数
        Log.e("magicBoxExtension", this.toString())
        return this
    }

    private val String.numVowels // 扩展属性
        get() = count { "aeiou".contains(it) }

    private fun String?.printWithDefault(default: String) = // 可空类型扩展函数
        Log.e("nullableExtension", this ?: default)

    private infix fun String?.printWithDefault2(default: String) = // infix的调用者可省略. ()
        Log.e("nullableExtension", this ?: default)

    private fun extensionFile() { // 将方法申明在kotlin file文件中
        val list = listOf("Quin", "sfppp", "Kenny")
        val set = setOf("Quin", "sfppp", "Kenny")
//        list.shuffled().first()
        list.randomTake()
        set.randomTake()
    }
    // 重命名扩展文件方法:import com.misakanetwork.kotlinlab.basic.randomTake as radomizer 重命名时使用该方法名

    private fun mapFunction() {
        // 用于遍历集合内容，对各元素进行操作
        val animals = listOf("Quin", "sfppp", "JK", "kenny")
        val babies =
            animals.map { animal -> "a beast $animal" } // 遍历接收者，让变换函数作用于每个元素，返回变换后的结果或作为链上下一个函数的输入
                .map { baby -> "$baby,with the little homo tail ever!" }
        Log.e("mapFunction", babies.toString())
        val babiesLength = babies.map { it.length } // map返回的元素个数必须与输入的元素个数一样，只存在元素内部的变化
        Log.e("mapFunction", "each length:$babiesLength")
    }

    private fun flatMapFunction() {
        // 用于操作一个集合中的集合，将它们内部的元素合并后返回一个包含所有元素的集合
        val result = listOf(listOf(1, 2, 3), listOf(4, 5, 6)).flatMap { it }
        Log.e("flatMapFunction", result.toString())
    }

    private fun filterFunction() {
        // 用于按条件过滤集合中的元素
        val result = listOf("Quin", "sfppp", "JK", "kenny")
            .filter { it.contains("p") } // 留下符合条件的，去除不符合条件的
        Log.e("filterFunction", result.toString())

        // filter+flatMap，按条件过滤集合中的集合的元素，返回一个合并后的集合
        val items = listOf(
            listOf("red apple", "green apple", "blue apple"),
            listOf("red fish", "blue fish"),
            listOf("yellow banana", "teal banana")
        )
        val redOrBanana =
            items.flatMap { it -> it.filter { it.contains("red") || it.contains("banana") } }
        Log.e("filter+map", redOrBanana.toString())

        // filter+map+none
        // 从集合中找元素是否是素数(除了1和它本身，不能被任何数整除的数)
        val numbers = listOf(7, 4, 8, 4, 3, 22, 18, 11)
        val primes = numbers.filter { number ->
            (2 until number).map { number % it } // 求除了满足条件的1与当前元素本身以外的数与其自身的%
                .none { it == 0 } // none:没有一个符合条件，即不满足上面%结果为0这一条件的，即素数
        }
        Log.e("filter+map+none", "素数：$primes")
    }

    private fun zipFunction() {
        // 用于合并两个集合，返回一个含键值对的集合
        val employees = listOf("Quin", "JiHuang", "OldMan")
        val shirtSize = listOf("large", "x-large", "medium")
        val result = employees.zip(shirtSize)
        Log.e("zipFunction", result.toString())
        // Map it and output each item's key with its value and new words
        val employeeShirtSizes = employees.zip(shirtSize).toMap() // 转成Map以在map操作时获取键与值
        Log.e("zipFunction toMap", employeeShirtSizes.toString())
        val eachItem = employeeShirtSizes.map { "${it.key} size is ${it.value}" }
        Log.e("zipFunction toMap->each", eachItem.toString())
        // zip again
        val price = listOf(1, 2, 3)
        val zipResult = result.zip(price)
        Log.e("zipFunction zip again", zipResult.toString())
    }

    private fun foldFunction() {
        // 用于对集合元素遍历且以初始值开始进行累加变量的操作，最后返回操作结果
        val result = listOf(
            100,
            20,
            37,
            41
        ).fold(2) { accumulator, item -> // accumulator:以2开始运算，并继承结果;item:遍历的元素
            accumulator + (item * 3) // 每个元素乘以3后累加起来
        }
        Log.e("fold", result.toString())
    }

    @SuppressLint("LongLogTag")
    private fun sequenceList() {
        // 取5000以内前1000个素数(除了1和它本身，不能被任何数整除的数)
        val toList = (1..5000).toList().filter { it.isPrime() }.take(1000) // take:获取集合前n个元素的集合
        Log.e("sequence origin way", toList.size.toString())
        // 使用generateSequence序列构造函数，为序列创建迭代器，适合数据量大的序列操作
        // 取前1000个素数，满足条件即可没有最大范围限制（java用while）
        val toList1 = generateSequence(2) { value -> // 迭代器 以2开始
            value + 1
        }.filter { it.isPrime() }.take(1000).toList() // take决定了循环次数
        Log.e("sequence generateSequence way", toList1.size.toString())
    }

    private fun Int.isPrime(): Boolean {
        (2 until this).map { // 求除了满足条件的1与当前元素本身以外的数与其自身的%
            if (this % it == 0) { // 能被整除，则不是素数
                return false
            }
        }
        return true
    }

    private fun javaKotlinTrans() {
        // java与kotlin的互操作性与可空性
        val adversary = Jhava()
        Log.e("", adversary.utterGreeting())
        val level =
            adversary.determineFriendshipLevel() // 没有@Nullable/@NotNull-- val level: String! 平台类型，可能为空也可能不为空
//        Log.e("javaKotlinTrans", level) // 寄
        // 在java的方法中，需要@Nullable/@NotNull
        level?.toLowerCase()
        level?.let { Log.e("javaKotlinTrans", it) }

        Log.e("javaKotlinTrans", adversary.hitPoints.javaClass.toString()) // 所有的类型最终都会映射回java的类型
        Log.e("javaKotlinTrans", adversary.hitPints1.toString()) // kotlin自动调用java的get()set()方法

        adversary.getKotlin() // java use kotlin's member @JvmName、@JvmField、@JvmOverloads、函数类型操作

        // 异常处理
        // java->kotlin
        // 没有强迫try catch处理
        try {
            adversary.extendHandleProblem()
        } catch (e: java.lang.Exception) {
            Log.e("javaKotlinTrans", "find problem")
        }
        // kotlin->java:异常被kotlin转换成了Throwable，需kotlin注解@Throws(异常Exception::class)
    }

    @JvmName("forYou")
    fun forJava() = "Greetings,beast!" // 类上使用@file:JvmName("XXX")以更改java调用类名

    class SpellBookForJava {
        @JvmField // Java调用时就不需要使用get() set()
        val spells = listOf("Magic book", "Physical book")
    }

    @JvmOverloads // 解决java无法实现任意传参的问题
    fun handoverFoodForJava(leftHand: String = "berries", rightHand: String = "beef") {
        Log.e(
            "javaKotlinTrans",
            "$leftHand in Quin's left hand and $rightHand in Quin's right hand"
        )
    }

    @Throws(IOException::class) // 解决kotlin把异常转换成了Throwable，导致java无法catch对应类型异常的问题
    fun acceptApologyProblem() { // 异常处理 kotlin->java
        throw IOException()
    }

    // 变量类型(匿名)函数
    val translator: (String) -> Unit = { item: String -> // java调用时，使用FunctionN名字的接口表示，N为值参的数目
        Log.e("funTypeOperation", item.toLowerCase().capitalize())
    }
}
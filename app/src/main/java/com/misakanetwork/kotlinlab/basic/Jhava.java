package com.misakanetwork.kotlinlab.basic;

import android.util.Log;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Created By：Misaka10085
 * on：2021/11/24
 * package：com.misakanetwork.kotlinlab
 * class name：Jhava
 * desc：java与kotlin的互操作性与可空性
 */
public class Jhava {
    public int hitPoints = 3232320;
    private int hitPints1 = 114514;

    @NotNull
    public String utterGreeting() {
        return "HELLO";
    }

    @Nullable
    public String determineFriendshipLevel() {
        return null;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public int getHitPints1() {
        return hitPints1;
    }

    public void setHitPints1(int hitPints1) {
        this.hitPints1 = hitPints1;
    }

    // @JvmName、@JvmField、@JvmOverloads
    public void getKotlin() {
        MainActivity activity = new MainActivity();
        Log.e("javaKotlinTrans", activity.forYou()); // @JvmName

        MainActivity.SpellBookForJava spellBookForJava = new MainActivity.SpellBookForJava();
        Log.e("javaKotlinTrans", String.valueOf(spellBookForJava.spells)); // @JvmField

        // 需要重载方法才能支持传任意参数数量，在Kotlin对应方法上添加@JvmOverloads
        activity.handoverFoodForJava("shit");

        // 异常处理
//        try { // java自己的异常，强制在编译期进行处理
//            extendHandleProblem();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            activity.acceptApologyProblem(); // kotlin的异常，java只能catch Throwable处理，或给kotlin的异常处添加@Throws(IOException::class)
        } catch (IOException e) {
            Log.e("javaKotlinTrans", "get right Exception by kotlin's Throws");
        }

        // 函数类型操作
        Function1<String, Unit> translator = activity.getTranslator(); // 使用FunctionN名字的接口表示，N为值参的数目
        translator.invoke("QUIN"); // FunctionN.invoke()传参执行
    }

    public void extendHandleProblem() throws IOException {
        throw new IOException();
    }
}

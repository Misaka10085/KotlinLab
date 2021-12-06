package com.misakanetwork.kotlinlab.jetpack;

import android.content.Context;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.misakanetwork.kotlinlab.jetpack.dao.ArticleDatabase;

/**
 * Created By：Misaka10085
 * on：2021/11/25
 * package：com.misakanetwork.kotlinlab.jetpack
 * class name：JavaTest
 * desc：JavaTest
 */
public class JavaTest extends AppCompatActivity {
    static final Migration MIGRATION_1_2=new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    public static void startThis(Context context) {

    }

    public JavaTest(Context context) {
        this.context = context;
    }

    private static final String txxx = "werewr";
    private Context context;
    private int testNum = 0;
    private MutableLiveData<Integer> m;

    public MutableLiveData<Integer> getM() {
        if (m == null) {
            m = new MutableLiveData<>();
            m.setValue(1);
        }


        SeekBar seekBar = new SeekBar(context);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return null;
    }

}

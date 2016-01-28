package net.skoumal.reflectionperformancetest;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Field;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testReflection();
    }

    public void testReflection() {
        try {
            int itemCount = (int)1e6;
            String [] strings = new String[itemCount];
            Object [] objects = new Object[itemCount];
            for(int i = 0; i < itemCount; i++) {
                strings[i] = "" + i;
                objects[i] = new Object();
            }

            // standard - write
            MyClass model = new MyClass();
            long startTime = System.currentTimeMillis();
            for(int i = 0; i < itemCount; i++) {
                model.string = strings[i];
            }
            long standardWayTime = System.currentTimeMillis() - startTime;

            // reflection - write
            Field field = MyClass.class.getField("string");
            startTime = System.currentTimeMillis();
            for(int i = 0; i < itemCount; i++) {
                field.set(model, strings[i]);
            }
            long reflectionWriteTime = System.currentTimeMillis() - startTime;

            // reflection - read
            field = MyClass.class.getField("string");
            startTime = System.currentTimeMillis();
            for(int i = 0; i < itemCount; i++) {
                field.get(model);
            }
            long reflectionReadTime = System.currentTimeMillis() - startTime;

            // --- Object ---

            // standard - write
            model = new MyClass();
            startTime = System.currentTimeMillis();
            for(int i = 0; i < itemCount; i++) {
                model.object = objects[i];
            }
            long standardWayObjectTime = System.currentTimeMillis() - startTime;

            // reflection - write
            field = MyClass.class.getField("object");
            startTime = System.currentTimeMillis();
            for(int i = 0; i < itemCount; i++) {
                field.set(model, objects[i]);
            }
            long reflectionObjectWriteTime = System.currentTimeMillis() - startTime;

            // reflection - read
            field = MyClass.class.getField("object");
            startTime = System.currentTimeMillis();
            for(int i = 0; i < itemCount; i++) {
                field.get(model);
            }
            long reflectionObjectReadTime = System.currentTimeMillis() - startTime;

            // autoboxing
            startTime = System.currentTimeMillis();
            Integer pom = 0;
            for(int i = 0; i < itemCount; i++) {
                pom = i;
            }
            long autoboxingTime = System.currentTimeMillis() - startTime;

            // string concat
            startTime = System.currentTimeMillis();
            for(int i = 0; i < itemCount; i++) {
                model.string = strings[i] + i;
            }
            long stringConcatTime = System.currentTimeMillis() - startTime;

            // new object
            startTime = System.currentTimeMillis();
            for(int i = 0; i < itemCount; i++) {
                model = new MyClass();
            }
            long newObjectTime = System.currentTimeMillis() - startTime;

            // logging
            startTime = System.currentTimeMillis();
            for(int i = 0; i < itemCount; i++) {
                Log.i("MainActivity", "Log for performance test.");
            }
            long loggingTime = System.currentTimeMillis() - startTime;

            Log.d("ReadWriteTests", "Standard-write (string)" +
                    ", Reflection-write (string)" +
                    ", Reflection-read (string)" +
                    ", Standard-write (object)" +
                    ", Reflection-write (object)" +
                    ", Reflection-read (object)" +
                    ", Autoboxing" +
                    ", Concat" +
                    ", Object" +
                    ", Logging ");
            Log.d("ReadWriteTests", standardWayTime +
                    "," + reflectionWriteTime +
                    "," + reflectionReadTime +
                    "," + standardWayObjectTime +
                    "," + reflectionObjectWriteTime +
                    "," + reflectionObjectReadTime +
                    "," + autoboxingTime +
                    "," + stringConcatTime +
                    "," + newObjectTime +
                    "," + loggingTime);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

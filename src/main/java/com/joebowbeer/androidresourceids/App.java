package com.joebowbeer.androidresourceids;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import android.R;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;

/**
 * Outputs id=name properties for Android's system resources.
 */
public class App {

  private static final List<Class<?>> CLASSES = Arrays.asList(
      R.anim.class,
      R.animator.class,
      R.array.class,
      R.attr.class,
      R.bool.class,
      R.color.class,
      R.dimen.class,
      R.drawable.class,
      R.fraction.class,
      R.id.class,
      R.integer.class,
      R.interpolator.class,
      R.layout.class,
      R.menu.class,
      R.mipmap.class,
      R.plurals.class,
      R.raw.class,
      R.string.class,
      R.style.class,
      R.transition.class, // API 21
      R.xml.class);

  public static void main(String[] args) throws IOException, IllegalAccessException {
    Properties props = new Properties();
    props.putAll(scan());
    props.store(System.out, "android-25");
  }

  static Map<String, String> scan() throws IllegalAccessException {
    Map<String, String> map = new HashMap<>();
    for (Class<?> clazz : CLASSES) {
      String typeName = clazz.getSimpleName();
      for (Field field : clazz.getDeclaredFields()) {
        int mod = field.getModifiers();
        if (isStatic(mod) && isFinal(mod) && field.getType() == int.class) {
          String idString = String.format("%#010x", field.getInt(null));
          map.put(idString, "android:" + typeName + "/" + field.getName());
        }
      }
    }
    return map;
  }
}

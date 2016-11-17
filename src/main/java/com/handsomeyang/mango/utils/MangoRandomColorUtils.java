package com.handsomeyang.mango.utils;

import android.graphics.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by HandsomeYang on 2016/11/1.
 */

public class MangoRandomColorUtils {
  private static Random random = new Random();
  //饱和度
  public static int SATURATIONTYPE_RANDOM = 1;     //饱和度随机
  public static int SATURATIONTYPE_MONOCHROME = 2; //单色，黑白色
  //明亮度
  public static int LUMINOSITY_BRIGHT = 4;
  public static int LUMINOSITY_LIGHT = 5;
  public static int LUMINOSITY_DARK = 6;
  public static int LUMINOSITY_RANDOM = 7;
  //颜色枚举
  public static String COLOR_MONOCHROME = "monochrome";
  public static String COLOR_RED = "red";
  public static String COLOR_ORANGE = "orange";
  public static String COLOR_YELLOW = "yellow";
  public static String COLOR_GREEN = "green";
  public static String COLOR_BLUE = "blue";
  public static String COLOR_PURPLE = "purple";
  public static String COLOR_PINK = "pink";

  //存储颜色集合   颜色名    颜色信息
  private static HashMap<String, ColorInfo> colors = new HashMap<>();

  public MangoRandomColorUtils() {
    loadColorBounds();
  }

  public MangoRandomColorUtils(long seed) {
    loadColorBounds();
    random.setSeed(seed);
  }

  private static int getColor(int hue, int saturation, int brightness) {
    return Color.HSVToColor(new float[] { hue, saturation, brightness });
  }

  /**
   * 返回一个随即色
   */
  public static int randomColor() {
    return randomColor(0, 0, 0);
  }

  /**
   * 返回一个指定 饱和度   指定明亮度的颜色
   */
  public static int randomColor(int value, int saturationType, int luminosity) {
    loadColorBounds();

    int hue = value;
    hue = pickHue(hue);
    int saturation = pickSaturation(hue, saturationType, luminosity);
    int brightness = pickBrightness(hue, saturation, luminosity);

    int color = getColor(hue, saturation, brightness);
    return color;
  }

  /**
   * 返回一个指定熟练的随机颜色数组
   */
  public static int[] randomColor(int count) {
    loadColorBounds();

    if (count <= 0) {
      throw new IllegalArgumentException("count must be greater than 0");
    }

    int[] colors = new int[count];
    for (int i = 0; i < count; i++) {
      colors[i] = randomColor();
    }

    return colors;
  }

  /**
   * 获取指定颜色的随机色
   */
  public static int randomColor(String color) {
    loadColorBounds();

    int hue = pickHue(color);
    int saturation = pickSaturation(color, 0, 0);
    int brightness = pickBrightness(color, saturation, 0);

    int colorValue = getColor(hue, saturation, brightness);
    return colorValue;
  }

  private static int pickSaturation(int hue, int saturationType, int luminosity) {
    return pickSaturation(getColorInfo(hue), saturationType, luminosity);
  }

  private static ColorInfo getColorInfo(int hue) {
    // Maps red colors to make picking hue easier
    if (hue >= 334 && hue <= 360) {
      hue -= 360;
    }

    for (String key : colors.keySet()) {
      ColorInfo colorInfo = colors.get(key);
      if (colorInfo.getHueRange() != null && colorInfo.getHueRange().contain(hue)) {
        return colorInfo;
      }
    }

    return null;
  }

  private static int pickSaturation(String color, int saturationType, int luminosity) {
    ColorInfo colorInfo = colors.get(color);
    return pickSaturation(colorInfo, saturationType, luminosity);
  }

  private static int pickSaturation(ColorInfo colorInfo, int saturationType, int luminosity) {
    if (saturationType != 0) {
      switch (saturationType) {
        case 1:
          return randomWithin(new Range(0, 100));
        case 2:
          return 0;
      }
    }

    if (colorInfo == null) {
      return 0;
    }

    Range saturationRange = colorInfo.getSaturationRange();

    int min = saturationRange.start;
    int max = saturationRange.end;

    if (luminosity != 0) {
      switch (luminosity) {
        case 5:
          min = 55;
          break;
        case 4:
          min = max - 10;
          break;
        case 6:
          max = 55;
          break;
      }
    }

    return randomWithin(new Range(min, max));
  }

  private static int pickBrightness(int hue, int saturation, int luminosity) {
    ColorInfo colorInfo = getColorInfo(hue);

    return pickBrightness(colorInfo, saturation, luminosity);
  }

  private static int pickBrightness(String color, int saturation, int luminosity) {
    ColorInfo colorInfo = colors.get(color);

    return pickBrightness(colorInfo, saturation, luminosity);
  }

  private static int pickBrightness(ColorInfo colorInfo, int saturation, int luminosity) {
    int min = getMinimumBrightness(colorInfo, saturation),
        max = 100;

    if (luminosity != 0) {
      switch (luminosity) {

        case 6:
          max = min + 20;
          break;

        case 5:
          min = (max + min) / 2;
          break;

        case 7:
          min = 0;
          max = 100;
          break;
      }
    }

    return randomWithin(new Range(min, max));
  }

  /**
   * 获取最小明亮度
   */
  private static int getMinimumBrightness(ColorInfo colorInfo, int saturation) {
    if (colorInfo == null) {
      return 0;
    }

    List<Range> lowerBounds = colorInfo.getLowerBounds();
    for (int i = 0; i < lowerBounds.size() - 1; i++) {

      int s1 = lowerBounds.get(i).start,
          v1 = lowerBounds.get(i).end;

      if (i == lowerBounds.size() - 1) {
        break;
      }
      int s2 = lowerBounds.get(i + 1).start,
          v2 = lowerBounds.get(i + 1).end;

      if (saturation >= s1 && saturation <= s2) {

        float m = (v2 - v1) / (float) (s2 - s1),
            b = v1 - m * s1;

        return (int) (m * saturation + b);
      }
    }

    return 0;
  }

  /**
   * 获取颜色
   */
  private static int pickHue(String hue) {
    Range hueRange = getHueRange(hue);
    return doPickHue(hueRange);
  }

  /**
   * 获取颜色
   */
  private static int pickHue(int hue) {
    Range hueRange = getHueRange(hue);
    return doPickHue(hueRange);
  }

  /**
   * 根据给出的颜色  获取一个随机饱和和明亮的该颜色
   */
  private static Range getHueRange(String name) {
    if (colors.containsKey(name)) {
      return colors.get(name).getHueRange();
    }

    return new Range(0, 360);
  }

  /**
   * 获取颜色范围
   */
  private static Range getHueRange(int number) {
    if (number < 360 && number > 0) {
      return new Range(number, number);
    }

    return new Range(0, 360);
  }

  /**
   * 取色
   */
  private static int doPickHue(Range hueRange) {
    int hue = randomWithin(hueRange);

    // Instead of storing red as two seperate ranges,
    // we group them, using negative numbers
    if (hue < 0) {
      hue = 360 + hue;
    }

    return hue;
  }

  /**
   * 在指定float范围中取最接近的整数
   */
  private static int randomWithin(Range range) {
    return (int) Math.floor(range.start + random.nextDouble() * (range.end + 1 - range.start));
  }

  /**
   * 加载颜色边界
   */
  private static void loadColorBounds() {
    List<Range> lowerBounds1 = new ArrayList<>();
    lowerBounds1.add(new Range(0, 0));
    lowerBounds1.add(new Range(100, 0));
    defineColor(COLOR_MONOCHROME, null, lowerBounds1);

    List<Range> lowerBounds2 = new ArrayList<>();
    lowerBounds2.add(new Range(20, 100));
    lowerBounds2.add(new Range(30, 92));
    lowerBounds2.add(new Range(40, 89));
    lowerBounds2.add(new Range(50, 85));
    lowerBounds2.add(new Range(60, 78));
    lowerBounds2.add(new Range(70, 70));
    lowerBounds2.add(new Range(80, 60));
    lowerBounds2.add(new Range(90, 55));
    lowerBounds2.add(new Range(100, 50));
    defineColor(COLOR_RED, new Range(-26, 18), lowerBounds2);

    List<Range> lowerBounds3 = new ArrayList<Range>();
    lowerBounds3.add(new Range(20, 100));
    lowerBounds3.add(new Range(30, 93));
    lowerBounds3.add(new Range(40, 88));
    lowerBounds3.add(new Range(50, 86));
    lowerBounds3.add(new Range(60, 85));
    lowerBounds3.add(new Range(70, 70));
    lowerBounds3.add(new Range(100, 70));
    defineColor(COLOR_ORANGE, new Range(19, 46), lowerBounds3);

    List<Range> lowerBounds4 = new ArrayList<>();
    lowerBounds4.add(new Range(25, 100));
    lowerBounds4.add(new Range(40, 94));
    lowerBounds4.add(new Range(50, 89));
    lowerBounds4.add(new Range(60, 86));
    lowerBounds4.add(new Range(70, 84));
    lowerBounds4.add(new Range(80, 82));
    lowerBounds4.add(new Range(90, 80));
    lowerBounds4.add(new Range(100, 75));

    defineColor(COLOR_YELLOW, new Range(47, 62), lowerBounds4);

    List<Range> lowerBounds5 = new ArrayList<>();
    lowerBounds5.add(new Range(30, 100));
    lowerBounds5.add(new Range(40, 90));
    lowerBounds5.add(new Range(50, 85));
    lowerBounds5.add(new Range(60, 81));
    lowerBounds5.add(new Range(70, 74));
    lowerBounds5.add(new Range(80, 64));
    lowerBounds5.add(new Range(90, 50));
    lowerBounds5.add(new Range(100, 40));

    defineColor(COLOR_GREEN, new Range(63, 178), lowerBounds5);

    List<Range> lowerBounds6 = new ArrayList<>();
    lowerBounds6.add(new Range(20, 100));
    lowerBounds6.add(new Range(30, 86));
    lowerBounds6.add(new Range(40, 80));
    lowerBounds6.add(new Range(50, 74));
    lowerBounds6.add(new Range(60, 60));
    lowerBounds6.add(new Range(70, 52));
    lowerBounds6.add(new Range(80, 44));
    lowerBounds6.add(new Range(90, 39));
    lowerBounds6.add(new Range(100, 35));

    defineColor(COLOR_BLUE, new Range(179, 257), lowerBounds6);

    List<Range> lowerBounds7 = new ArrayList<>();
    lowerBounds7.add(new Range(20, 100));
    lowerBounds7.add(new Range(30, 87));
    lowerBounds7.add(new Range(40, 79));
    lowerBounds7.add(new Range(50, 70));
    lowerBounds7.add(new Range(60, 65));
    lowerBounds7.add(new Range(70, 59));
    lowerBounds7.add(new Range(80, 52));
    lowerBounds7.add(new Range(90, 45));
    lowerBounds7.add(new Range(100, 42));

    defineColor(COLOR_PURPLE, new Range(258, 282), lowerBounds7);

    List<Range> lowerBounds8 = new ArrayList<>();
    lowerBounds8.add(new Range(20, 100));
    lowerBounds8.add(new Range(30, 90));
    lowerBounds8.add(new Range(40, 86));
    lowerBounds8.add(new Range(60, 84));
    lowerBounds8.add(new Range(80, 80));
    lowerBounds8.add(new Range(90, 75));
    lowerBounds8.add(new Range(100, 73));

    defineColor(COLOR_PINK, new Range(283, 334), lowerBounds8);
  }

  /**
   * 定义颜色
   */
  public static void defineColor(String name, Range hueRange, List<Range> lowerBounds) {
    int sMin = lowerBounds.get(0).start;
    int sMax = lowerBounds.get(lowerBounds.size() - 1).start;
    int bMin = lowerBounds.get(lowerBounds.size() - 1).end;
    int bMax = lowerBounds.get(0).end;

    colors.put(name,
        new ColorInfo(hueRange, new Range(sMin, sMax), new Range(bMin, bMax), lowerBounds));
  }

  /**
   * 标识范围内部类
   */
  static class Range {
    int start, end;

    public Range(int start, int end) {
      this.start = start;
      this.end = end;
    }

    /**
     * 该值是否包含在范围之中
     */
    public boolean contain(int value) {
      return value >= start && value <= end;
    }
  }

  /**
   * 标识颜色相关信息
   */
 static class ColorInfo {
    Range hueRange;//颜色范围
    Range saturationRange;//饱和度
    Range birghtnessRange;//明亮度
    List<Range> lowerBounds; //范围边界

    public ColorInfo(Range hueRange, Range saturationRange, Range birghtnessRange,
        List<Range> lowerBounds) {
      this.hueRange = hueRange;
      this.saturationRange = saturationRange;
      this.birghtnessRange = birghtnessRange;
      this.lowerBounds = lowerBounds;
    }

    public Range getHueRange() {
      return hueRange;
    }

    public void setHueRange(Range hueRange) {
      this.hueRange = hueRange;
    }

    public Range getSaturationRange() {
      return saturationRange;
    }

    public void setSaturationRange(Range saturationRange) {
      this.saturationRange = saturationRange;
    }

    public Range getBirghtnessRange() {
      return birghtnessRange;
    }

    public void setBirghtnessRange(Range birghtnessRange) {
      this.birghtnessRange = birghtnessRange;
    }

    public List<Range> getLowerBounds() {
      return lowerBounds;
    }

    public void setLowerBounds(List<Range> lowerBounds) {
      this.lowerBounds = lowerBounds;
    }
  }

  /**
   * 颜色操作项
   */
  public static class Options {
    int hue;              //色彩
    int saturationType;   //饱和度类型
    int luminosity;       //明亮度

    public int getHue() {
      return hue;
    }

    public void setHue(int hue) {
      this.hue = hue;
    }

    public int getSaturationType() {
      return saturationType;
    }

    public void setSaturationType(int saturationType) {
      this.saturationType = saturationType;
    }

    public int getLuminosity() {
      return luminosity;
    }

    public void setLuminosity(int luminosity) {
      this.luminosity = luminosity;
    }
  }
}

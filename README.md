# PictureSelectHelper
A util for android to crop the picture in the phone by system owned way.

一款采用了Android系统自带方式实现图片选择和裁剪的工具。

> ![](https://github.com/Moosphan/PictureSelectHelper/blob/535dc2fd0895e4bb910835e7f68a787d08a39650/SystemPictureSelector-master/art/sys_crop.gif)

### 说明：

- 适配了Android7.0系统；
- 支持拍照获取和本地图库获取图片；
- 支持自定义裁剪图片的区域比例和图片输出质量；
- 建造者模式两行代码搞定图片的获取和裁剪；
- 支持自定义图片的输出路径

### 使用

1. 创建builder：

   ```
   SystemPictureSelector.Builder builder = new SystemPictureSelector.Builder(this);
   builder.isCropped(true)
           .setCropSize(3, 2)
           .setOutputSize(1200, 1200)
           .setOutputPath(savedPath)
           .setOnSelectListener(new SystemPictureSelector.OnSystemPictureSelectListener() {
               @Override
               public void onSelectedSuccess(File file) {

                   Uri uri = Uri.fromFile(file);
                   Log.e(TAG, "onSelectedSuccess: "+uri.toString() );
               }

               @Override
               public void onSelectedMessage(String msg) {

                   Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
               }
           });
   pictureSelector = builder.create();
   ```

2. 调起系统camera或者本地图库：

   ```
   pictureSelector.getSystemPhotoByCamera(); // 通过拍照获取图片
   ......
   pictureSelector.getSystemPhotoByGallery();// 通过本地图库获取
   ```

3. 绑定onActivityResult方法：

   ```
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       pictureSelector.bindingActivityForResult(requestCode, resultCode, data);
   }
   ```

### 关于我

邮箱：moosphon@gmail.com

QQ群：601924443

### License

> ```
> The MIT License (MIT)
>
> Copyright (c) 2018 Moosphon
>
> Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sub license, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
>
> The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
> ```
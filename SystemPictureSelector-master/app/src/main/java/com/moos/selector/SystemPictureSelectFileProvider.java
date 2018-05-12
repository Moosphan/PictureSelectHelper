package com.moos.selector;

import android.support.v4.content.FileProvider;

/**
 * Created by moos on 2018/5/12.
 * 继承fileProvider，防止与项目其他的fileProvider冲突，导致无法安装应用
 */

public class SystemPictureSelectFileProvider extends FileProvider {
}

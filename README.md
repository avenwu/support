Support
========

Custom Android support library, include some useful utils and widget.

	support内是自定义的一些东西，sammple中包含support的实现demo样例，

Download
-------
Download the latest repo or grab the stable released version via Maven:

Clone the master branch:

```
	git clone https://github.com/avenwu/support.git
```

or Gradle:

```Groovy
	compile 'com.github.avenwu:support:0.1.1'
```


License
=======

    Copyright 2014 Chaobin Wu.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


---

实战案例
-------

### 卡片翻转优化

![device-2016-01-19-162559.mp4.gif](images/device-2016-01-19-162559.mp4.gif)

### TextView缩略
通过控制文本展示实现单击展开和收缩文本，并在收缩状态显示提示图标

![TextView](http://7u2jir.com1.z0.glb.clouddn.com/expand_text.gif)

### 字体设置汇总
通过不同方式实现自定义字体的设置，主要区别在于调用层

![字体设置汇总](http://7u2jir.com1.z0.glb.clouddn.com/device-2015-10-08-173228.png)


### 巧用BitmapShader
通过为Paint画笔设置Shader，可以再画布上绘制许多有意思的东西

![BitmapShader](http://7u2jir.com1.z0.glb.clouddn.com/bitmapshader_imageview.gif)


### Property自定义属性动画
基于Property的自定义属性动画

![Property动画](http://7u2jir.com1.z0.glb.clouddn.com/property_animation.gif)

### 自定义ResideMenu
residemenu是是侧滑菜单的一种，但是视觉效果更特别，此次实现是基于android v4中SlidePanelLayout扩展而来，主要是为了减少非核心代码的开发工作。

详细实现请看:[基于SlidePanelLayout实现ResideMenu](http://avenwu.net/2015/02/24/custom_slide_panel_layout_as_reside_style_on_dribble_and_qq)

对于普通侧滑菜单的实现也可以参照我之前的一片文章[自定义侧滑菜单](http://avenwu.net/customlayout/2014/12/16/sliding_menu/)

![自定义ResideMenu](http://7u2jir.com1.z0.glb.clouddn.com/custom_residemenu.gif)

### RadioGroup仿iOS Segmented Control
这个没什么好说的用的实际上是RadioGroup，但是加强了封装和配置，所以使用会方便一些，否则每次需要类似UI效果都从新写是很累的事情。

![RadioGroup仿iOS Segmented Control ](http://7u2jir.com1.z0.glb.clouddn.com/styled_radiogroup.png)

### 流式标签生成控件
这个东西还是比较有意思的，看图说话，通过EditText和TextView以及ViewGroup的有机结合，就可以做出这个效果不一般的输入交互控件。

详细技术实现请看:[流式标签生成控件](http://avenwu.net/customlayout/2015/01/18/tag_layout)

![流式标签生成控件](http://7u2jir.com1.z0.glb.clouddn.com/tag_input_layout_demo.gif)

### qq消息气泡【二次贝塞尔曲线多边形】
这个实际上是做为分析QQ红点气泡的一部分，及气泡拖拽的原理。

![多边形气泡](http://7u2jir.com1.z0.glb.clouddn.com/polygon_bezier.gif)


### 侧滑菜单
简单的侧滑效果实现并不难，需要处理两块view容器的相对关系，但是有效果并不代表能使用，可实用的组件还需要考虑很多，下面的demo实际上主要目的在于处理菜单和内容区的位置关系，用的是scroll移动的方法，结合Scroller，所以导致变化的实际上是菜单容器的内容而不是菜单,所以support里还有另外一个自定义侧滑菜单解决这个问题。更多技术实现参考对应的文章:[自定义侧滑菜单](http://avenwu.net/customlayout/2014/12/16/sliding_menu/)

![侧滑菜单](http://7u2jir.com1.z0.glb.clouddn.com/drawermenu.gif)

### 下拉刷新列表
下拉刷新的本质上是LinearLayout嵌套ListView+View（刷新的头部视图），通过TouchEvent的分发控制，动态改变视图的Top位置。

![下拉刷新列表](http://7u2jir.com1.z0.glb.clouddn.com/pulltorefresh.gif)


### 圆形排行View
这个实际上最开是的时候已经作为一个单独的项目开发，并且已经上传值maven，所以也可用gradle导入。

[IndexImageView项目首页](http://avenwu.net/IndexImageView/) 
这个项目源灵感来自搜狐视屏客户端，看好声音的时候看到人气选手的头像是一个圆形带排行数的视图，感觉有点意思，索性花了点时间自己写了一个，同时学习如何将开源项目发布到Maven Central，这样就可以通过gradle方便的获取maven依赖库。  
![Screenshot](https://github.com/avenwu/IndexImageView/raw/master/device-2014-10-21-164818.png)






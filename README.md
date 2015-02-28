#Support

Custom Android support library, include some useful utils and widget.

	support内是自定义的一些东西，sammple中包含support的实现demo样例，


##实战案例

###自定义ResideMenu
residemenu是是侧滑菜单的一种，但是视觉效果更特别，此次实现是基于android v4中SlidePanelLayout扩展而来，主要是为了减少非核心代码的开发工作。

详细实现请看:[基于SlidePanelLayout实现ResideMenu](http://avenwu.net/2015/02/24/custom_slide_panel_layout_as_reside_style_on_dribble_and_qq)

对于普通侧滑菜单的实现也可以参照我之前的一片文章[自定义侧滑菜单](http://avenwu.net/customlayout/2014/12/16/sliding_menu/)

![自定义ResideMenu](http://7u2jir.com1.z0.glb.clouddn.com/custom_residemenu.gif)

###RadioGroup仿iOS Segmented Control
这个没什么好说的用的实际上是RadioGroup，但是加强了封装和配置，所以使用会方便一些，否则每次需要类似UI效果都从新写是很累的事情。

![RadioGroup仿iOS Segmented Control ](http://7u2jir.com1.z0.glb.clouddn.com/styled_radiogroup.png)

###流式标签生成控件
这个东西还是比较有意思的，看图说话，通过EditText和TextView以及ViewGroup的有机结合，就可以做出这个效果不一般的输入交互控件。

详细技术实现请看:[流式标签生成控件](http://avenwu.net/customlayout/2015/01/18/tag_layout)

![流式标签生成控件](http://7u2jir.com1.z0.glb.clouddn.com/tag_input_layout_demo.gif)

###qq消息气泡【二次贝塞尔曲线多边形】
这个实际上是做为分析QQ红点气泡的一部分，及气泡拖拽的原理。

![多边形气泡](https://raw.githubusercontent.com/avenwu/blogs/master/blog/resources/polygon_bezier.gif)


###侧滑菜单
简单的侧滑效果实现并不难，需要处理两块view容器的相对关系，但是有效果并不代表能使用，可实用的组件还需要考虑很多，下面的demo实际上主要目的在于处理菜单和内容区的位置关系，用的是scroll移动的方法，结合Scroller，所以导致变化的实际上是菜单容器的内容而不是菜单,所以support里还有另外一个自定义侧滑菜单解决这个问题。更多技术实现参考对应的文章:[自定义侧滑菜单](http://avenwu.net/customlayout/2014/12/16/sliding_menu/)

![侧滑菜单](https://raw.githubusercontent.com/avenwu/blogs/master/blog/resources/drawermenu.gif)

###下拉刷新列表
下拉刷新的本质上是LinearLayout嵌套ListView+View（刷新的头部视图），通过TouchEvent的分发控制，动态改变视图的Top位置。

![下拉刷新列表](https://raw.githubusercontent.com/avenwu/blogs/master/blog/resources/pulltorefresh.gif)










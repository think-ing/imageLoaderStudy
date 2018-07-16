# imageLoaderStudy
一个简单的图片加载类


<p>
## 缓存<br />
1、支持内存缓存（最大内存的1/4做图片缓存）；<br />
2、支持SD卡缓存；<br />
3、支持内存、SD卡双缓存；<br />
<br />
## 使用<br />
//获得实例<br />
mImageloader = new Imageloader();<br />
//设置默认显示图片<br />
mImageloader.setDefaultImg(R.mipmap.default_img);<br />
//设置下载失败图片<br />
mImageloader.setFailedImg(R.mipmap.failed_img);<br />
//设置想使用的缓存(默认为双缓存)<br />
mImageloader.setImageCache(new DoubleCache());<br />
//加载图片<br />
mImageloader.displayImage(imageUrl,imageView,progressBar);<br />
</p>

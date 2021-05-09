# mvvm-component
    更方便实现项目MVVM架构的MVVM框架组件  
### http-ui-mapping
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ooftf/http-ui-mapping/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ooftf/http-ui-mapping)

定义了网络请求和UI响应之间的联动
#### 引用方式
```groovy
    mavenCentral()
    
    dependencies {
        implementation 'com.github.ooftf:http-ui-mapping:1.4.1'
    }  
```
#### 初始化
```kotlin
    HttpUiMapping.init(object : HttpUiMapping.Provider {
                override fun toast(string: String?) {
                    //TODO 网络错误toast
                }
    
                override fun createLoadingDialog(activity: Activity): HttpUiMapping.MyDialogInterface {
                    //TODO 创建网络加载dialog
                }
    
                override fun onTokenInvalid(baseResponse: IResponse) {
                    //TODO token 失效
                }
    
            }, BuildConfig.DEBUG)
```      
### arch-frame-mvvm
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ooftf/arch-frame-mvvm/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ooftf/arch-frame-mvvm)

基于http-ui-mapping，提供了BaseMvvmActivity和BaseMvvmFragment基础类
#### 引用方式
```groovy
    mavenCentral()
    dependencies {
        implementation 'com.github.ooftf:arch-frame-mvvm:0.1.5'
    }
```
### mapping-button
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ooftf/mapping-button/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ooftf/mapping-button)

#### 引用方式
```groovy
    mavenCentral()
    dependencies {
        implementation 'com.github.ooftf:mapping-button:1.3.8'
    }
```
### [mvvm脚手架](https://github.com/ooftf/MVVM-Generator-ooftf)
    基于arch-frame-mvvm的AndroidStudio 插件，提供了一键生成Activity ViewModel Layout Fragment 功能
        
### 混淆
    -keepclassmembers  class * extends androidx.lifecycle.AndroidViewModel {
             <init>(...);
        }
    -keepclassmembers public class * extends androidx.databinding.ViewDataBinding{
        public static  inflate(android.view.LayoutInflater);
        public static  inflate(android.view.LayoutInflater,android.view.ViewGroup,boolean);
    }
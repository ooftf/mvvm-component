# mvvm-component
    更方便实现项目MVVM架构的MVVM框架组件  
### implementation 'com.ooftf:http-ui-mapping:1.3.0'    
    定义了网络请求和UI响应之间的联动
#### 引用方式
```groovy
    maven {
        url "https://dl.bintray.com/ooftf/maven"
    }
    dependencies {
        implementation 'com.ooftf:sliding-layout:1.1.5'
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
### implementation 'com.ooftf:arch-frame-mvvm:0.0.4'
    基于http-ui-mapping，提供了BaseMvvmActivity和BaseMvvmFragment基础类
#### 引用方式
```groovy
    maven {
        url "https://dl.bintray.com/ooftf/maven"
    }
    dependencies {
        implementation 'com.ooftf:sliding-layout:1.1.5'
    }
```

### !(mvvm脚手架)[https://github.com/ooftf/MVVM-Generator-ooftf]
    基于arch-frame-mvvm的AndroidStudio 插件，提供了一键生成Activity ViewModel Layout Fragment 功能
        

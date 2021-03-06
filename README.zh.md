![](MorbidMask.png)

[![](https://jitpack.io/v/ssseasonnn/MobidMask.svg)](https://jitpack.io/#ssseasonnn/MobidMask)

# MorbidMask - 吸血面具

*Read this in other languages: [中文](README.zh.md), [English](README.md), [Change Log](CHANGELOG.md)*

一个安全快速的对Activity或者Fragment传递参数的库

> 物品介绍:
>
> 攻击者每次攻击都将根据造成的伤害回复15%的生命值。
>  吸血：15%
>
>

## Prepare

1. Add jitpack to build.gradle
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. Add dependency

```gradle
apply plugin: 'kotlin-kapt'

...

dependencies {
	implementation 'com.github.ssseasonnn.MobidMask:morbidmask:1.0.5'
    kapt 'com.github.ssseasonnn.MobidMask:compiler:1.0.5'
}
```

## Start


### First Blood

- 给Activity添加注解:

    ```kotlin
    @Params(
        Val("intParam", Int::class),
        Val("booleanParam", Boolean::class),
        Val("stringParam", String::class)
    )
    class TestActivity : AppCompatActivity() 
    ```  
    
    通过Params注解, 告诉该Activity需要的参数类型及参数名称.
    
    例如TestActivity的参数分别为:
    - intParam :Int类型 
    - booleanParam :Boolean类型 
    - stringParam :String参数
    
- 传递参数给Activity:

    Params注解编译之后,将会生成该Activity对应的Director文件,命名规则为 Activity名称+Director后缀,
    例如上面的TestActivity将会生成TestActivityDirector文件.
    
    ```kotlin
    btn_activity.setOnClickListener {
        //通过Director安全快速传递参数给TestActivity
        TestActivityDirector.of(this)
            .intParam(1123123123)
            .booleanParam(true)
            .stringParam("This is string param")
            .direct()
    }
    ```
    
- 获取传递给Activity的参数:

    Params注解编译之后, 除了会生成Director文件, 还会生成对应的Params文件, 命名规则为 Activity名称+ Params后缀,
    例如上面的TestActivity将会生成TestActivityParams文件. 
    
    ```kotlin
    class TestActivity : AppCompatActivity() {
    
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_test)
    
            //获取params参数
            val params = TestActivityParams.of(this)
          
            Log.d("TAG", params.intParam.toString())
            Log.d("TAG", params.booleanParam.toString())
            Log.d("TAG", params.stringParam)
        }
    }
    ```
    
### Double Kill 

接下来尝试给Fragment传递参数.

- 添加Params注解:

    ```kotlin
    @Params(
        Val("intParam", Int::class),
        Val("booleanParam", Boolean::class),
        Val("stringParam", String::class)
    )
    class TestFragment : Fragment()
    ```
    
- 传递参数给Fragment:

    同样的,Params注解编译之后,将会生成该Fragment对应的Director文件,命名规则为 Fragment名称+Director后缀,
    例如上面的TestFragment将会生成TestFragmentDirector文件.
    
    ```kotlin
    btn_fragment.setOnClickListener {
          TestFragmentDirector.of()
              .intParam(1123123123)
              .booleanParam(true)
              .stringParam("This is string param")
              .direct {
                  val fragmentTransaction = supportFragmentManager.beginTransaction()
                  fragmentTransaction.add(R.id.fragment_container, it)
                  fragmentTransaction.commit()
          }
    }
    ```
    
- 获取传递给Fragment的参数:

    Params注解编译之后, 除了会生成Director文件, 还会生成对应的Params文件, 命名规则为 Fragment名称+ Params后缀,
    例如上面的TestFragment将会生成TestFragmentParams文件. 
    
    ```kotlin
    class TestFragment : Fragment() {
  
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val params = TestFragmentParams.of(this)
          
            Log.d("TAG", params.intParam.toString())
            Log.d("TAG", params.booleanParam.toString())
            Log.d("TAG", params.stringParam)
        }
    }
    ```

### Triple Kill

传递自定义数据类型

除了上述基本数据类型外,还支持自定义的数据类型. 

- 添加自定义类型参数:

    ```kotlin
    //自定义数据类型    
    class CustomEntity(
        val id: Int,
        val content: String
    )
  
    //使用自定义数据类型  
    @Params(
        Val("customParam", CustomEntity::class)
    )
    class TestActivity : AppCompatActivity() 
    ```

- 传递自定义类型参数:

    ```kotlin
    btn_activity.setOnClickListener {
        TestActivityDirector.of(this)
            .customParam(CustomEntity(123, "Custom entity content"))
            .direct()
    }
    ```

> 自定义数据类型默认使用Gson进行序列化和反序列化, 因此请在release环境中Keep自定义的数据类型, 以免发生问题!


### Ultra Kill

利用**MutableParams**注解生成 **var** 类型参数.

如上所见, 通过**Params**注解只能定义**val**不可变类型的参数, 要想定义**var**可变类型的参数, 可以通过
**MutableParams**及**Var**声明可变类型的参数.

例如:

```kotlin
@Params(
    Val("charParam", Char::class),
    Val("booleanParam", Boolean::class),
    Val("stringParam", String::class)
)
@MutableParams(
    Var("test", String::class),
    Var("test1", Boolean::class)
)
class TestActivity : AppCompatActivity() 
```


### License

> ```
> Copyright 2019 Season.Zlc
>
> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this file except in compliance with the License.
> You may obtain a copy of the License at
>
>    http://www.apache.org/licenses/LICENSE-2.0
>
> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
> See the License for the specific language governing permissions and
> limitations under the License.
> ```

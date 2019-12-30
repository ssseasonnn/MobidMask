![](MorbidMask.png)

[![](https://jitpack.io/v/ssseasonnn/MobidMask.svg)](https://jitpack.io/#ssseasonnn/MobidMask)

# MorbidMask - 吸血面具

*Read this in other languages: [中文](README.zh.md), [English](README.md), [Change Log](CHANGELOG.md)*

A library for fast and safe delivery of parameters for Activities and Fragments.

> Item introduction:
>
> Each attacker will recover 15% of its health based on the damage dealt.
>
> Bloodsucking：15%
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

- Add annotations to the Activity:

    ```kotlin
    @Params(
        Val("intParam", Int::class),
        Val("booleanParam", Boolean::class),
        Val("stringParam", String::class)
    )
    class TestActivity : AppCompatActivity() 
    ```  
    
    The Params annotation tells the Activity what type of parameters it needs and the name of the parameters.
    
    For example, the parameters of TestActivity are:
    - intParam :Int 
    - booleanParam :Boolean 
    - stringParam :String
    
- Pass parameters to Activity:

    After the Params annotation is compiled, the Director file corresponding to the Activity will be generated. 
    The naming rule is the Activity name + Director suffix. 
    
    For example, the above TestActivity will generate the TestActivityDirector file..
    
    ```kotlin
    btn_activity.setOnClickListener {
        //Quickly pass parameters to TestActivity via Director Security
        TestActivityDirector.of(this)
            .intParam(1123123123)
            .booleanParam(true)
            .stringParam("This is string param")
            .direct()
    }
    ```
    
- Get the parameters passed to the Activity:

    After the Params annotation is compiled, in addition to generating the Director file, 
    the corresponding Params file will be generated. 
    The naming rule is the Activity name + Params suffix.
    
    For example, the above TestActivity will generate the TestActivityParams file.
    
    ```kotlin
    class TestActivity : AppCompatActivity() {
    
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_test)
    
            val params = TestActivityParams.of(this)
          
            Log.d("TAG", params.intParam.toString())
            Log.d("TAG", params.booleanParam.toString())
            Log.d("TAG", params.stringParam)
        }
    }
    ```
    
### Double Kill 

Next, try passing the Fragment parameters.

- Add Params annotation:

    ```kotlin
    @Params(
        Val("intParam", Int::class),
        Val("booleanParam", Boolean::class),
        Val("stringParam", String::class)
    )
    class TestFragment : Fragment()
    ```
    
- Passing parameters to Fragment:

    Similarly, after the Params annotation is compiled, the Director file corresponding to the Fragment will be generated. 
    The naming rule is the Fragment name + Director suffix. 
    
    For example, the above TestFragment will generate the TestFragmentDirector file..
    
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
    
- Get the parameters passed to the Fragment:

   After the Params annotation is compiled, in addition to generating the Director file, 
   the corresponding Params file will be generated. The naming rule is Fragment name + Params suffix. 
   
   For example, the above TestFragment will generate the TestFragmentParams file.. 
    
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

Pass the custom data type

In addition to the above basic data types, it also supports custom data types. 

- Add custom type parameters:

    ```kotlin
    //custom type    
    class CustomEntity(
        val id: Int,
        val content: String
    )
  
    @Params(
        Val("customParam", CustomEntity::class)
    )
    class TestActivity : AppCompatActivity() 
    ```

- Pass custom type parameters:

    ```kotlin
    btn_activity.setOnClickListener {
        TestActivityDirector.of(this)
            .customParam(CustomEntity(123, "Custom entity content"))
            .direct()
    }
    ```

> Custom data types are serialized and deserialized by default using Gson, 
so please keep the custom data type in the release environment to avoid problems.!


### Ultra Kill

Generate **var** type parameters with **MutableParams** annotations.

As seen above, only the **val** immutable type parameter can be defined by the **Params** annotation. 
To define the **var** variable type parameter, you can pass **MutableParams** and **Var** Declare a variable type parameter.

eg:

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

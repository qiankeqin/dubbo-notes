/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.common.compiler.support;


import com.alibaba.dubbo.common.compiler.Compiler;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.ExtensionLoader;

/**
 * AdaptiveCompiler. (SPI, Singleton, ThreadSafe)
 */
@Adaptive
public class AdaptiveCompiler implements Compiler {
    /**
     * 用户配置的 spiKey（jdk 或者 javassist）
     */
    private static volatile String DEFAULT_COMPILER;
    /**
     * 用户配置的 spiKey（jdk 或者 javassist），在 ApplicationConfig#setCompiler(String compiler) 中进行调用
     * @param compiler 用户配置的 spiKey（jdk 或者 javassist）
     */
    public static void setDefaultCompiler(String compiler) {
        DEFAULT_COMPILER = compiler;
    }

    @Override
    public Class<?> compile(String code, ClassLoader classLoader) {
        Compiler compiler;
        ExtensionLoader<Compiler> loader = ExtensionLoader.getExtensionLoader(Compiler.class);
        String name = DEFAULT_COMPILER; // copy reference
        if (name != null && name.length() > 0) {
            // 如果用户配置了 compiler，则创建指定的 Compiler 实例
            compiler = loader.getExtension(name);
        } else {
            // 如果用户没有指定 compiler，则创建默认的 JavassistCompiler 实例
            compiler = loader.getDefaultExtension();
        }
        // 使用具体实现进行编译
        return compiler.compile(code, classLoader);
    }
}

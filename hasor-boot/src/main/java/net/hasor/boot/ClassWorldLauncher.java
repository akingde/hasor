/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.boot;
import net.hasor.core.Module;
import org.codehaus.plexus.classworlds.ClassWorld;
/**
 * 使用 HasorPlugin 启动 Hasor 所使用的 main 入口
 * @version : 2018-08-04
 * @author 赵永春 (zyc@hasor.net)
 */
public final class ClassWorldLauncher {
    public static void main(String[] args) throws Throwable {
        Class<? extends Module> setupModule = (Class<? extends Module>) Thread.currentThread().getContextClassLoader().loadClass(args[0]);
        HasorLauncher.run(setupModule, args);
    }
    public static void main(String[] args, ClassWorld world) throws Throwable {
        main(args);
    }
}
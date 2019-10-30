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
package net.hasor.tconsole.binder;
import net.hasor.core.ApiBinder;
import net.hasor.core.BindInfo;
import net.hasor.tconsole.TelAttribute;
import net.hasor.tconsole.TelExecutor;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static net.hasor.tconsole.launcher.TelUtils.finalBindAddress;

/**
 * tConsole 插件接口
 * @version : 2019年10月30日
 * @author 赵永春 (zyc@hasor.net)
 */
public interface ConsoleApiBinder extends ApiBinder {
    /**
     * 创建一个 Host 模式的 Tel 命令交互界面，并使用标准输入输出流作为数据交换通道。
     * 提示：使用标准输入输出流作为消息通道的情况下会自动启用 answerExit
     */
    public default HostBuilder asHostWithSTDO() {
        return asHost(new InputStreamReader(System.in), new OutputStreamWriter(System.out, StandardCharsets.UTF_8)).answerExit();
    }

    /**
     * 创建一个 Host 模式的 Tel 命令交互界面。
     */
    public HostBuilder asHost(Reader reader, Writer writer);

    /**
     * 创建一个网络 Tel 命令交互界面，使用的协议是 Telnet。
     */
    public default TelnetBuilder asTelnet(String host, int port) throws UnknownHostException {
        return asTelnet(host, port, s -> true);
    }

    /**
     * 创建一个网络 Tel 命令交互界面，使用的协议是 Telnet。
     */
    public default TelnetBuilder asTelnet(String host, int port, Predicate<String> inBoundMatcher) throws UnknownHostException {
        return asTelnet(new InetSocketAddress(finalBindAddress(host), port), inBoundMatcher);
    }

    public TelnetBuilder asTelnet(InetSocketAddress address, Predicate<String> inBoundMatcher);

    public interface HostBuilder extends TelnetBuilder, TelAttribute {
        /** 安静模式，安静模式下不会输"tConsole>" 提示符，也不会输出欢迎信息。 */
        public HostBuilder silent();

        /** 预执行的命令，预执行命令最快会在容器启动之后异步方式执行的。*/
        public HostBuilder preCommand(String... commands);

        /** 响应 exit 指令自动退出容器 */
        public HostBuilder answerExit();
    }

    public interface TelnetBuilder {
        /** 添加新命令 */
        public CommandBindingBuilder addExecutor(String... names);
    }

    public interface CommandBindingBuilder {
        /** 绑定到一个实现类上 */
        public <T extends TelExecutor> void to(Class<? extends T> executorKey);

        /** 绑定到一个构造方法上 */
        public <T extends TelExecutor> void toConstructor(Constructor<T> constructor);

        /** 绑定到一个具体对象上 */
        public <T extends TelExecutor> void toInstance(T instance);

        /** 绑定到一个提供者上 */
        public <T extends TelExecutor> void toProvider(Supplier<? extends T> executorProvider);

        /** 绑定到一个 Info上 */
        public <T extends TelExecutor> void toInfo(BindInfo<? extends T> executorInfo);
    }
}
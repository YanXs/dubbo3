package com.alibaba.dubbo.rpc.proxy.bytebuddy;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * @author Xs.
 */
public interface MethodInclusion {

    ElementMatcher<MethodDescription> getIncludes();

}

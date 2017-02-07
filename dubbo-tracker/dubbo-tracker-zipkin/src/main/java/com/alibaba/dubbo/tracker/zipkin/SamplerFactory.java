package com.alibaba.dubbo.tracker.zipkin;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.kristofa.brave.BoundarySampler;
import com.github.kristofa.brave.CountingSampler;
import com.github.kristofa.brave.Sampler;

/**
 * @author Xs.
 */
public class SamplerFactory {

    public static Sampler create(URL url) {
        String sampler = url.getParameter("sampler");
        if (StringUtils.isEmpty(sampler)) {
            return Sampler.ALWAYS_SAMPLE;
        } else {
            String rate = url.getParameter("rate");
            if (StringUtils.isEmpty(rate)) {
                return Sampler.ALWAYS_SAMPLE;
            }
            if (sampler.equals("counting")) {
                return CountingSampler.create(Float.valueOf(rate));
            } else if (sampler.equals("boundary")) {
                return BoundarySampler.create(Float.valueOf(rate));
            } else {
                throw new IllegalArgumentException("unknown sampler type, sampler: " + sampler);
            }
        }
    }
}

package com.ctrip.soa.caravan.ribbon.rule;

import com.ctrip.soa.caravan.common.delegate.Func;
import com.ctrip.soa.caravan.common.value.StringValues;
import com.ctrip.soa.caravan.common.value.checker.NullArgumentChecker;
import com.ctrip.soa.caravan.common.value.checker.StringArgumentChecker;
import com.ctrip.soa.caravan.configuration.dynamic.PropertyChangeEvent;
import com.ctrip.soa.caravan.configuration.dynamic.PropertyChangeListener;
import com.ctrip.soa.caravan.configuration.typed.dynamic.TypedDynamicProperty;
import com.ctrip.soa.caravan.ribbon.ConfigurationKeys;
import com.ctrip.soa.caravan.ribbon.LoadBalancerRule;
import com.ctrip.soa.caravan.ribbon.LoadBalancerRuleFactoryManager;
import com.ctrip.soa.caravan.ribbon.loadbalancer.LoadBalancerContext;
import com.ctrip.soa.caravan.ribbon.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

import static com.ctrip.soa.caravan.common.value.ConcurrentHashMapValues.getOrAddWithLock;

/**
 * Created by w.jian on 2017/2/25.
 */
public final class DefaultLoadBalancerRuleFactoryManager implements LoadBalancerRuleFactoryManager {
    
    private static Logger _logger = LoggerFactory.getLogger(DefaultLoadBalancerRuleFactoryManager.class);
    
    public static final String ROUND_ROBIN_RULE_NAME = "round-robin-rule";
    
    public static final String ROUND_ROBIN_RULE_DESCRIPTION = "简单轮巡算法";

    public static final String WEIGHT_ROUND_ROBIN_RULE_NAME = "weighted-round-robin-rule";
    
    public static final String WEIGHT_ROUND_ROBIN_RULE_DESCRIPTION = "带权重的轮巡算法";
    
    private LoadBalancerContext _loadBalancerContext;
    private ConcurrentHashMap<String, Func<LoadBalancerRule>> _loadBalanceRuleFactoryMap;
    private TypedDynamicProperty<String> _ruleProperty;
    private String _ruleId;
    private Func<LoadBalancerRule> _ruleFactory;
    private ConcurrentHashMap<String, LoadBalancerRule> _loadBalancerRules;
    
    
    public DefaultLoadBalancerRuleFactoryManager(LoadBalancerContext loadBalancerContext) {
        _loadBalancerContext = loadBalancerContext;
        _loadBalanceRuleFactoryMap = new ConcurrentHashMap<>();
        _loadBalanceRuleFactoryMap.put(ROUND_ROBIN_RULE_NAME, new Func<LoadBalancerRule>() {
            @Override
            public LoadBalancerRule execute() {
                return new RoundRobinRule();
            }
        });
        _loadBalanceRuleFactoryMap.put(WEIGHT_ROUND_ROBIN_RULE_NAME, new Func<LoadBalancerRule>() {
            @Override
            public LoadBalancerRule execute() {
                return new WeightedRoundRobinRule();
            }
        });
    
        _ruleId = WEIGHT_ROUND_ROBIN_RULE_NAME;
        _ruleFactory = _loadBalanceRuleFactoryMap.get(WEIGHT_ROUND_ROBIN_RULE_NAME);
        _loadBalancerRules = new ConcurrentHashMap<>();
        
        initializeRuleProperty();
    }
    
    private void initializeRuleProperty() {
        String rulePropertyKey = String.format("%s.%s", _loadBalancerContext.loadBalancerKey(), ConfigurationKeys.LoadBalancerRuleName);
        _ruleProperty = _loadBalancerContext.properties().getStringProperty(rulePropertyKey, DefaultLoadBalancerRuleFactoryManager.WEIGHT_ROUND_ROBIN_RULE_NAME);
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void onChange(PropertyChangeEvent event) {
                setLoadBalanceByRuleId(_ruleProperty.typedValue());
            }
        };
        _ruleProperty.addChangeListener(listener);
        setLoadBalanceByRuleId(_ruleProperty.typedValue());
    }
    
    private void setLoadBalanceByRuleId(String newRuleId) {
        if (StringValues.isNullOrWhitespace(newRuleId)) {
            LogUtil.warn(_logger, String.format("Invalid rule id %s", newRuleId), _loadBalancerContext.additionalInfo());
            return;
        }
        
        if (_ruleFactory != null && newRuleId.equals(_ruleId)) {
            LogUtil.info(_logger, String.format("Not modified: %s", newRuleId), _loadBalancerContext.additionalInfo());
            return;
        }
        
        Func<LoadBalancerRule> newRuleFactory = _loadBalanceRuleFactoryMap.get(newRuleId);
        if (newRuleFactory == null) {
            LogUtil.warn(_logger, String.format("Invalid rule id %s", newRuleId), _loadBalancerContext.additionalInfo());
            return;
        }
        
        String message = String.format("LoadBalancerRule changed from %s to %s", _ruleId, newRuleId);
        LogUtil.info(_logger, message, _loadBalancerContext.additionalInfo());
        _ruleId = newRuleId;
        _ruleFactory = newRuleFactory;
        _loadBalancerRules = new ConcurrentHashMap<>();
    }
    
    @Override
    public LoadBalancerRule getLoadBalancerRule(String ruleId) {
        return getOrAddWithLock(_loadBalancerRules, ruleId, getLoadBalancerRuleFactory());
    }
    
    @Override
    public Func<LoadBalancerRule> getLoadBalancerRuleFactory() {
        return _ruleFactory;
    }
    
    @Override
    public void registerLoadBalancerRuleFactory(Func<LoadBalancerRule> ruleFactory) {
        NullArgumentChecker.DEFAULT.check(ruleFactory, "rule");
        
        LoadBalancerRule rule = ruleFactory.execute();
        StringArgumentChecker.DEFAULT.check(rule.getRuleId(), "ruleId");
        
        if (_loadBalanceRuleFactoryMap.contains(rule.getRuleId()))
            return;
        _loadBalanceRuleFactoryMap.put(rule.getRuleId(), ruleFactory);
        setLoadBalanceByRuleId(rule.getRuleId());
    }
}

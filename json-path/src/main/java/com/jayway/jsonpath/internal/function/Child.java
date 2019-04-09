package com.jayway.jsonpath.internal.function;

import java.util.Collection;

import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.Predicate.PredicateContext;
import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.path.PredicateContextImpl;

/**
 * Applies on the children of a Map or an Array
 */ 
public class Child  implements FilterFunction {

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx, Filter filter) {    	
    	Object resultArray = ctx.configuration().jsonProvider().createArray();
        int idx = 0; 
        if(ctx.configuration().jsonProvider().isArray(model)){
            Iterable<?> objects = ctx.configuration().jsonProvider().toIterable(model);
            for (Object obj : objects) {
	           	 boolean res = true;
	           	 if(filter != null) {		
	                 PredicateContext pc = new PredicateContextImpl(obj, ctx.rootDocument(), ctx.configuration(), null);
	                 res = filter.apply(pc);
	           	 }
	           	 if(res) {
	           		 ctx.configuration().jsonProvider().setArrayIndex(resultArray, idx, obj);
	           		 idx+=1;
	           	 }
            }
            return resultArray;
        } else if(ctx.configuration().jsonProvider().isMap(model)){
            Collection<String> keys = ctx.configuration().jsonProvider().getPropertyKeys(model);
            for (String key : keys) {
            	 Object obj =  ctx.configuration().jsonProvider().getMapValue(model, key);
            	 boolean res = true;
            	 if(filter != null) {		
                     PredicateContext pc = new PredicateContextImpl(obj, ctx.rootDocument(), ctx.configuration(), null);
             	     res = filter.apply(pc);
            	 }
            	 if(res) {
            		 ctx.configuration().jsonProvider().setArrayIndex(resultArray, idx, obj);
            		 idx+=1;
            	 }
            }
            return resultArray;
        }
        throw new JsonPathException("Child function attempted to be applied  value using empty array");
    }
}
package ydh.cicada.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ydh.cicada.dao.SplitStrategy;

@Target(value={ElementType.TYPE})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Split {
	@SuppressWarnings("rawtypes")
	Class<? extends SplitStrategy> value();
}
